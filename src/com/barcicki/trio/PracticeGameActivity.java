package com.barcicki.trio;

import java.util.ArrayList;
import java.util.EnumSet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGrid;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.Trio.TrioStatus;
import com.barcicki.trio.core.TrioSettings;
import com.openfeint.api.resource.Achievement;
import com.openfeint.api.resource.Achievement.UnlockCB;

public class PracticeGameActivity extends Activity {
	private static int NUMBER_OF_TRIOS = 3;
	
	SharedPreferences mPrefs;
	Trio mTrio = new Trio();
	View mPauseOverlay;
	
	Handler mHandler = new Handler();
	long mTimerStart = 0L;
	TextView mTimer;

	CardList gSelectedCards = new CardList();
	ArrayList<CardView> gSelectedViews = new ArrayList<CardView>();
	ArrayList<CardList> gFoundTrios = new ArrayList<CardList>();
	
	String gElapsedTimeString = "00:00";
	long gElapsedTime = 0L;
	String gPracticeString;
	int gTriosFound = 0;
	int gTriosRemaines = NUMBER_OF_TRIOS;
	boolean gGameEnded = false;
	boolean gGameStarted = false;
	
	CardGrid mTriosGrid;
	CardGrid mCardGrid;

	private TextView mGameStatus;
	private SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.practice);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mTriosGrid = (CardGrid) findViewById(R.id.triosContainer);
		mTimer = (TextView) findViewById(R.id.gameTimer);
		mPauseOverlay = findViewById(R.id.gamePause);
		mGameStatus = (TextView) findViewById(R.id.gameStatus);
		
		mSoundManager = SoundManager.getInstance(this);
		
		attachCardListeners();
		
		startPractice();
	
//		hidePause();		
	}
	
	private void startPractice() {
		
		CardList set = mTrio.getSetWithTrios( NUMBER_OF_TRIOS );
		gPracticeString = set.toString();
		mGameStatus.setText("" + gTriosRemaines);
		
		mCardGrid.setCards( set );
		
		mTriosGrid.setCards( set.getTrios() );
		mTriosGrid.setResourceImageForAll(R.drawable.square_question);
		mTriosGrid.showReverse();
				
		startTimer();
		
		
	}
	
	private void resetPracticeStatus() {
		gGameEnded = false;
		gGameStarted = false;
		gElapsedTime = 0L;
		gTriosFound = 0;
		gTriosRemaines = NUMBER_OF_TRIOS;
		mGameStatus.setText("" + gTriosRemaines);
		gFoundTrios = new ArrayList<CardList>();
		gSelectedCards = new CardList();
		gSelectedViews = new ArrayList<CardView>();
	}
	
	private void pausePractice() {
		mCardGrid.showReverse();
		pauseTimer();
	}
	
	private void resumePractice() {
		mCardGrid.hideReverse();
		startTimer();
	}
	
	private void endPractice() {
		showEndingPause();
		
		gGameEnded = true;

		if (TrioSettings.usesOpenFeint(this)) {
			submitToOpenFeint();
		}
	
	}
	
	private void attachCardListeners() {
		
		mCardGrid.setOnItemClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				CardView cv = (CardView) v;
				Card card = cv.getCard();
				
				if (!gSelectedCards.contains(card)) {
					
					gSelectedCards.add(card);
					gSelectedViews.add(cv);
					
					cv.setSelected(true);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Selected " + card.toString());
				} else {
					
					gSelectedCards.remove(card);
					gSelectedViews.remove(cv);
					
					cv.setSelected(false);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Deselected " + card.toString());
				}
				
				if (gSelectedCards.size() == 3) {
					
					if (gSelectedCards.hasTrio()) {
						onTrioFound(gSelectedCards, gSelectedViews);
					} else {
						onNotTrioFound(gSelectedCards, gSelectedViews);
					}
					
					gSelectedCards.clear();
					gSelectedViews.clear();
					
					
				} else {
					mSoundManager.playSound(SoundManager.SOUND_CLICK);
				}
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		
		if (mPauseOverlay.getVisibility() == View.VISIBLE && !gGameEnded && gGameStarted) {
			hidePause();
		} else if (gGameEnded) {
			showEndingPause();
		} else if (!gGameStarted) {
			showStartPause();
		} else {
			showPause();
		}
		
	}

	@Override
	protected void onPause() {
		pauseTimer();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if (gElapsedTime > 0) {
			showPause();
		} else {
			showStartPause();
		}
		super.onResume();
	}
	
	protected void onNotTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio NOT found");
		
		mSoundManager.playSound(SoundManager.SOUND_FAIL);
		
		for (CardView cv : selectedViews) {
			cv.animateFail();
		}
		
		if (TrioSettings.displaysWhatIsWrong(this)) {
			displayWhatIsWrong(selectedCards);
		}
	}
	
	protected void onTrioAlreadyFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio is already found");
		
		mSoundManager.playSound(SoundManager.SOUND_FAIL);
		
		for (CardView cv : selectedViews) {
			cv.animateFail();
		}
		
		if (TrioSettings.displaysWhatIsWrong(this)) {
			Toast.makeText(this, getString(R.string.practice_already_found), Toast.LENGTH_SHORT).show();
		}
	}

	protected void onTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio found");
		
		boolean alreadyFound = false;
		for (CardList list : gFoundTrios) {
			if (CardList.areEqual(selectedCards, list)) {
				alreadyFound = true;
			}
		}
		
		if (alreadyFound) {
			onTrioAlreadyFound(selectedCards, selectedViews);
		} else {
			
			mSoundManager.playSound(SoundManager.SOUND_SUCCESS);
			
			gTriosFound += 1;
			gTriosRemaines -= 1;
			
			mGameStatus.setText("" + gTriosRemaines);
			
			gFoundTrios.add(new CardList(selectedCards));
			final ArrayList<CardView> views = new ArrayList<CardView>(selectedViews);
			mTriosGrid.setRevealCardListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					for (CardView cv : views) {
						cv.setSelected(false);
						cv.invalidate();
						cv.refreshDrawableState();
					}
				}
			});
			mTriosGrid.revealCard(selectedCards);
		}
		
		if (gTriosRemaines == 0) {
			endPractice();
		}
				
	}
	
	private void submitToOpenFeint() {
		
		TrioSettings.submitToOpenFeint(TrioSettings.LEADERBOARD_CHALLENGE_ID, gElapsedTime, gElapsedTimeString);
//		TrioSettings.submitToOpenFeint(TrioSettings.LEADERBOARD_TOTAL_ID, TrioSettings.getChallengeGamePoints(gElapsedTime));
		
//		ServerTimestamp.get(new GetCB() {
//			@Override
//			public void onSuccess(ServerTimestamp currentTime) {
//				long score = Long.MAX_VALUE - (currentTime.secondsSinceEpoch * 1000 / TrioSettings.DAY) * TrioSettings.DAY + gElapsedTime;
//				TrioSettings.submitToOpenFeint(TrioSettings.LEADERBOARD_DAILY_CHALLENGE_ID, score, DateFormat.format("MM/dd/yy", new Date()) + " " + gElapsedTimeString);				
//			}
//		});
//		
		
		// finish game achievement
		Achievement endingAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CHALLENGE_FINISH);
		if (!endingAchievement.isUnlocked) {
			endingAchievement.unlock(new UnlockCB() {
				@Override
				public void onSuccess(boolean newUnlock) {
					// TODO Auto-generated method stub
					
				}
			});
		}
				
		// turtle speed achievement
		if (gElapsedTime < 2L * 60 * 1000) {
			Achievement turtleAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CHALLENGE_GOOD);
			if (!turtleAchievement.isUnlocked) {
				turtleAchievement.unlock(new UnlockCB() {
					@Override
					public void onSuccess(boolean newUnlock) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
			
		// gepard speed achievement
		if (gElapsedTime <= 30 * 1000) {
			Achievement gepardAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CHALLENGE_QUICK);
			if (!gepardAchievement.isUnlocked) {
				gepardAchievement.unlock(new UnlockCB() {
					@Override
					public void onSuccess(boolean newUnlock) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}

		// lighting speed achievement
		if (gElapsedTime <= 10 * 1000) {
			Achievement blitzAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CHALLENGE_BLITZ);
			if (!blitzAchievement.isUnlocked) {
				blitzAchievement.unlock(new UnlockCB() {
					@Override
					public void onSuccess(boolean newUnlock) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}

	private void startTimer() {	
		
		mTimerStart = System.currentTimeMillis() - gElapsedTime;
		
		mHandler.removeCallbacks(mUpdateTimer);
//		mHandler.post(mUpdateTimer);
		mHandler.postDelayed(mUpdateTimer, 50);
		
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Timer started");
	}
	
	private void pauseTimer() {
		mHandler.removeCallbacks(mUpdateTimer);
	}

	private Runnable mUpdateTimer = new Runnable() {

		public void run() {
			
			
			final long start = mTimerStart;
			long millis = System.currentTimeMillis() - start;
			
			gElapsedTime = millis;
			int seconds = (int) millis / 1000;
			int minutes = seconds / 60;
			seconds %= 60;

			if (seconds < 10) {
				gElapsedTimeString = minutes + ":0" + seconds;
			} else {
				gElapsedTimeString = minutes + ":" + seconds;
			}

			mTimer.setText(gElapsedTimeString);
			mHandler.postAtTime(this, SystemClock.uptimeMillis() + 1000);
		}
	};
	
	/**
	 *  Button handlers
	 */
	
	public void onPauseClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		showPause();
	}
	

	/**
	 * Pause Handlers
	 */
	
	public void onPauseShow() {
		pausePractice();
		
//		TextView statusView = (TextView) mPauseOverlay.findViewById(R.id.gameStatus);
		
		
		TextView timeView = (TextView) mPauseOverlay.findViewById(R.id.gameTime);
		timeView.setText(gElapsedTimeString);
		
		TextView trioView = (TextView) mPauseOverlay.findViewById(R.id.gameTrioCount);
		trioView.setText(getString(R.string.practice_trio_count, gTriosFound, NUMBER_OF_TRIOS ));
//		statusView.setText(getString(R.string.classic_status, mTrio.getGame().size()));
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(R.string.pause_continue);
		buttonContinue.setVisibility(View.VISIBLE);
		
		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
//		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
//		buttonQuit.setText(getString(R.string.pause_save_quit));
		
	}
	
	public void showPause() {
		onPauseShow();
		mPauseOverlay.setVisibility(View.VISIBLE);
	}
	
	public void showEndingPause() {
		showPause();
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);
		
		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		TextView statusView = (TextView) mPauseOverlay.findViewById(R.id.gameTrioCount);
		statusView.setText(getString(R.string.practice_end));
		
	}
	
	public void showStartPause() {
		showPause();
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.VISIBLE);
		buttonContinue.setText(getString(R.string.pause_start));
		
		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.GONE);
		
		TextView timeView = (TextView) mPauseOverlay.findViewById(R.id.gameTime);
		timeView.setText(getString(R.string.practice_objective));
		
		TextView trioView = (TextView) mPauseOverlay.findViewById(R.id.gameTrioCount);
		trioView.setText(getString(R.string.practice_subobjective));
	}
	
	public void hidePause() {
		onPauseHide();
		mPauseOverlay.setVisibility(View.INVISIBLE);
	}
	
	public void onPauseHide() {
		resumePractice();
	}
	
	public void onPressedContinue(View v) {
		gGameStarted = true;
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		hidePause();
	}
	
	public void onPressedNewGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		startPractice();
		
		resetPracticeStatus();
		
//		showStartPause();
		startTimer();
		hidePause();
	}
	
	public void onPressedRestartGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		CardList set = CardList.fromString(mTrio.getDeck(), gPracticeString);
		
		mCardGrid.setCards( set );				
		mTriosGrid.setCards( set.getTrios() );
		mTriosGrid.showReverse();
		
		resetPracticeStatus();

		startTimer();
		hidePause();
	}
	
	public void onPressedQuitGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	
	public void displayWhatIsWrong(CardList threeCards) {
		EnumSet<TrioStatus> status = Trio.getTrioStatus(threeCards);
		ArrayList<String> errors = new ArrayList<String>();

		if (status.contains(TrioStatus.WRONG_COLOR)) {
			errors.add(getString(R.string.tutorial_colour));
		}

		if (status.contains(TrioStatus.WRONG_SHAPE)) {
			errors.add(getString(R.string.tutorial_shape));
		}

		if (status.contains(TrioStatus.WRONG_FILL)) {
			errors.add(getString(R.string.tutorial_fill));
		}

		if (status.contains(TrioStatus.WRONG_NUMBER)) {
			errors.add(getString(R.string.tutorial_number));
		}

		Toast.makeText(
				this,
				getString(R.string.tutorial_wrong_message,
						TextUtils.join(", ", errors)), Toast.LENGTH_SHORT)
				.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
