package com.barcicki.trio;

import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGridView;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;

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
	
	CardGridView mTriosGrid;
	CardGridView mCardGrid;

	private TextView mGameStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.practice);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGridView) findViewById(R.id.cardsContainer);
		mTriosGrid = (CardGridView) findViewById(R.id.triosContainer);
		mTimer = (TextView) findViewById(R.id.gameTimer);
		mPauseOverlay = (View) findViewById(R.id.gamePause);
		mGameStatus = (TextView) findViewById(R.id.gameStatus);
		
		attachCardListeners();
		
		startPractice();
	
		hidePause();		
	}
	
	private void startPractice() {
		
		CardList set = mTrio.getSetWithTrios( NUMBER_OF_TRIOS );
		gPracticeString = set.toString();
		mGameStatus.setText("" + gTriosRemaines);
		
		mCardGrid.setCards( set );
		mCardGrid.renderGrid();
		
		mTriosGrid.setColumns( 3 );
		mTriosGrid.setPredefinedCardSize( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()) );
		mTriosGrid.setCards( set.getTrios() );
		mTriosGrid.renderGrid();
		mTriosGrid.showReverse();
				
		startTimer();
	}
	
	private void resetPracticeStatus() {
		gGameEnded = false;
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
		
//		Leaderboard leaderboard = new Leaderboard(TrioSettings.LEADERBOARD_CLASSIC_ID);
//		Score score = new Score(gElapsedTime, gElapsedTimeString);
//		score.submitTo(leaderboard, new SubmitToCB() {
//			
//			public void onSuccess(boolean newHighScore) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		// finish game achievement
//		Achievement endingAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CLASSIC_ENDURANCE);
//		if (!endingAchievement.isUnlocked) {
//			endingAchievement.unlock(new UnlockCB() {
//				@Override
//				public void onSuccess(boolean newUnlock) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		}
//		
//		// no hints used achievement
//		if (gHintsRemained == NUMBER_OF_HINTS) {
//			Achievement pureAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CLASSIC_PURE);
//			if (!pureAchievement.isUnlocked) {
//				pureAchievement.unlock(new UnlockCB() {
//					@Override
//					public void onSuccess(boolean newUnlock) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		}
//		
//		// turtle speed achievement
//		if (gElapsedTime < 10L * 60 * 1000) {
//			Achievement turtleAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CLASSIC_TURTLE);
//			if (!turtleAchievement.isUnlocked) {
//				turtleAchievement.unlock(new UnlockCB() {
//					@Override
//					public void onSuccess(boolean newUnlock) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		}
//			
//		// gepard speed achievement
//		if (gElapsedTime < 5L * 60 * 1000) {
//			Achievement gepardAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CLASSIC_GEPARD);
//			if (!gepardAchievement.isUnlocked) {
//				gepardAchievement.unlock(new UnlockCB() {
//					@Override
//					public void onSuccess(boolean newUnlock) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		}
//
//		// lighting speed achievement
//		if (gElapsedTime < 2L * 60 * 1000) {
//			Achievement blitzAchievement = new Achievement(TrioSettings.ACHIEVEMENT_CLASSIC_LIGHTING);
//			if (!blitzAchievement.isUnlocked) {
//				blitzAchievement.unlock(new UnlockCB() {
//					@Override
//					public void onSuccess(boolean newUnlock) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//			}
//		}
//		

		
	
	}
	
	private void attachCardListeners() {
		
		mCardGrid.setOnCardClickListener(new OnClickListener() {
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
					mCardGrid.deselectAll();
					gSelectedViews.clear();
					
					
				} 			
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		
		if (mPauseOverlay.getVisibility() == View.VISIBLE) {
			hidePause();
		} else {
			showPause();
		}
		
	}

	@Override
	protected void onPause() {
		showPause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		showStartPause();
		super.onResume();
	}
	
	protected void onNotTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio NOT found");
		
		Animation failAnimation = AnimationUtils.loadAnimation(this, R.anim.card_fail);
		for (CardView cv : selectedViews) {
			cv.startAnimation(failAnimation);
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
			onNotTrioFound(selectedCards, selectedViews);
		} else {
			
			gTriosFound += 1;
			gTriosRemaines -= 1;
			
			mGameStatus.setText("" + gTriosRemaines);
			
			gFoundTrios.add(new CardList(selectedCards));
			mTriosGrid.revealCard(selectedCards);
			
		}
		
		if (gTriosRemaines == 0) {
			endPractice();
		}
				
	}
//	
//	private boolean saveGame() {
//		
//		SharedPreferences.Editor ed = mPrefs.edit();
//		
//		ed.putString("game_string", mTrio.getGameString());
//		ed.putString("game", mTrio.getGame().toString() );
//		ed.putString("table", mTrio.getTable().toString() );
//		ed.putInt("trios_found", gTriosFound);
//		ed.putInt("hints", gHintsRemained);
//		ed.putLong("time", gElapsedTime );
//		ed.putBoolean("saved_game", true);
//		
//		if (ed.commit()) {
//			if (Trio.LOCAL_LOGV) Log.v("Classic Game", "saved progress");
//			return true;
//		} else {
//			Log.e("Classic Game", "failed to save progress");
//			return false;
//		}
//	}
//	
//	private boolean clearSavedGame() {
//		
//		SharedPreferences.Editor ed = mPrefs.edit();
//		ed.putBoolean("saved_game", false);
//		
//		if (ed.commit()) {
//			if (Trio.LOCAL_LOGV) Log.v("Classic Game", "unsaved progress");
//			return true;
//		} else {
//			Log.e("Classic Game", "failed to change progress");
//			return false;
//		}
//		
//	}
//
//	private boolean restoreSavedGame() {
//
//		if (mPrefs.getBoolean("saved_game", false)) {
//
//			String game = mPrefs.getString("game", "");
//			String table = mPrefs.getString("table", "");
//			String gameString = mPrefs.getString("game_string", "");
//			gHintsRemained = mPrefs.getInt("hints", NUMBER_OF_HINTS);
//			gTriosFound = mPrefs.getInt("trios_found", 0);
//			gElapsedTime = mPrefs.getLong("time", 0L);
//
//			if (!game.equals("") && !table.equals("")) {
//
//				mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
//				mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
//				mTrio.setGameString(gameString);
//				
//				if (Trio.LOCAL_LOGV)
//					Log.v("Classic Game", "Restored saved game");
//				return true;
//
//			} else {
//				Log.e("TrioState", "failed to restore saved game");
//			}
//
//		}
//
//		return false;
//	}
	
//	private void handleRestart() {
//
//		mRestart.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//
//				mTrio.newGame();
//				mTimerStart = 0L;
//
//				startTimer();
//				updateGameStatus();
//
//				mAdapter = new CardAdapter(getApplicationContext(),
//						R.layout.single_card, mTrio.getTable());
//				mGrid.setAdapter(mAdapter);
//
//				mSelectedCards.clear();
//
//			}
//
//		});
//	}
//
//	private void updateGameStatus() {
//
//		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
//			
////			mGameStatus.setText(getString(R.string.solo_game_ended));
//			clearSavedGame();
//			
//		} else {
////			mGameStatus.setText(getString(R.string.solo_game_status, mTrio
////					.getGame().size()));
//		}
//
//	}
//
//	private void handleHints() {
//
//		TextView hintButton = (TextView) findViewById(R.id.hintTextView);
//		hintButton.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//
//				ArrayList<CardList> trios = mTrio.getTable().getTrios();
//
//				int selectedSize = mSelectedCards.size();
//
//				if (trios.size() > 0) {
//
//					// if no cards were selected before, select the first card
//					// possible - it is being done in the end of method
//
//					// if one card was selected, check if it makes trio with
//					// others. If yes, show next card, if no, deselect it and
//					// select another one
//					if (selectedSize == 1) {
//
//						Card selected = mSelectedCards.get(0);
//
//						for (CardList trio : trios) {
//
//							if (trio.contains(selected)) {
//								for (Card c : trio) {
//									if (!c.equals(selected)) {
//
//										mSelectedCards.add(c);
//										markViewSelected(mGrid
//												.findViewWithTag(c));
//
//										if (Trio.LOCAL_LOGV)
//											Log.v("Single Game Hint",
//													"Hint showed second card");
//										return;
//									}
//								}
//							}
//
//						}
//
//						// if there're two cards selected check if they make any
//						// trio, if yes do not give additional hints, if no
//						// deselect them and select first one
//					} else if (selectedSize == 2) {
//						Card sel1 = mSelectedCards.get(0);
//						Card sel2 = mSelectedCards.get(1);
//						for (CardList trio : trios) {
//							if (trio.contains(sel1) && trio.contains(sel2)) {
//								Toast.makeText(getApplicationContext(),
//										getString(R.string.hints_ended),
//										Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}
//					}
//
//					mSelectedCards.clear();
//					Card firstCard = trios.get(0).get(0);
//
//					mSelectedCards.add(firstCard);
//
//					markAllViewsDeslected();
//					markViewSelected(mGrid.findViewWithTag(firstCard));
//
//					if (Trio.LOCAL_LOGV)
//						Log.v("Single Game Hint", "Hint showed first card");
//					return;
//
//				} else {
//					if (Trio.LOCAL_LOGD)
//						Log.d("Single Game Hint",
//								"Trios were empty when they shouldn't");
//				}
//
//			}
//
//		});
//
//	}

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
		showPause();
	}
	

	/**
	 * Pause Handlers
	 */
	
	public void onPauseShow() {
		pausePractice();
		
		TextView timeView = (TextView) mPauseOverlay.findViewById(R.id.gameTime);
//		TextView statusView = (TextView) mPauseOverlay.findViewById(R.id.gameStatus);
		TextView trioView = (TextView) mPauseOverlay.findViewById(R.id.gameTrioCount);
		
		timeView.setText(gElapsedTimeString);
		trioView.setText(getString(R.string.practice_trio_count, gTriosFound, NUMBER_OF_TRIOS ));
//		statusView.setText(getString(R.string.classic_status, mTrio.getGame().size()));
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(R.string.pause_continue);
		
//		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
//		buttonQuit.setText(getString(R.string.pause_save_quit));
		
	}
	
	public void showPause() {
		onPauseShow();
		mPauseOverlay.setVisibility(View.VISIBLE);
	}
	
	public void showEndingPause() {
		showPause();
		
		TextView statusView = (TextView) mPauseOverlay.findViewById(R.id.gameTrioCount);
		statusView.setText(getString(R.string.practice_end));
		
	}
	
	public void showStartPause() {
		showPause();
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(getString(R.string.pause_start));
	}
	
	public void hidePause() {
		onPauseHide();
		mPauseOverlay.setVisibility(View.INVISIBLE);
	}
	
	public void onPauseHide() {
		resumePractice();
	}
	
	public void onPressedContinue(View v) {
		hidePause();
	}
	
	public void onPressedNewGame(View v) {
		
		startPractice();
		
		resetPracticeStatus();
		
		startTimer();
		hidePause();
	}
	
	public void onPressedRestartGame(View v) {
		
		CardList set = CardList.fromString(mTrio.getDeck(), gPracticeString);
		
		mCardGrid.setCards( set );		
		mCardGrid.renderGrid();
		
		mTriosGrid.setColumns( 3 );
		mTriosGrid.setPredefinedCardSize( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()) );
		mTriosGrid.setCards( set.getTrios() );
		mTriosGrid.renderGrid();
		mTriosGrid.showReverse();
		
		resetPracticeStatus();

		startTimer();
		hidePause();
	}
	
	public void onPressedQuitGame(View v) {
		finish();
	}
	
}
