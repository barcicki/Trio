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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
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

public class ClassicGameActivity extends Activity {
	private static int NUMBER_OF_HINTS = 10;

	SharedPreferences mPrefs;
	Trio mTrio = new Trio();
	View mPauseOverlay;

	Handler mHandler = new Handler();
	long mTimerStart = 0L;

	TextView mTimer;
	Button mHintButton;

	CardList gSelectedCards = new CardList();
	ArrayList<CardView> gSelectedViews = new ArrayList<CardView>();

	String gElapsedTimeString = "00:00";
	long gElapsedTime = 0L;
	int gTriosFound = 0;
	int gHintsRemained = NUMBER_OF_HINTS;
	boolean gGameEnded = false;
	boolean gRestoredGame = false;
	boolean gGameStarted = false;

	CardGrid mCardGrid;

	SoundManager mSoundManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classic);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mTimer = (TextView) findViewById(R.id.gameTimer);
		mPauseOverlay = findViewById(R.id.gamePause);
		mHintButton = (Button) findViewById(R.id.gameHintButton2);

		mSoundManager = SoundManager.getInstance(this);

		if (restoreSavedGame()) {
			gRestoredGame = true;
		} else {
			mTrio.newGame();
		}

		attachCardListeners();
		startGame();

		hidePause();
	}

	private void startGame() {

		mCardGrid.setCards(mTrio.getTable());
		mHintButton.setText(getString(R.string.classic_hint, gHintsRemained));

		startTimer();
	}

	private void resetGameStatus() {
		gRestoredGame = false;
		gGameEnded = false;
		gGameStarted = false;
		gElapsedTime = 0L;
		gTriosFound = 0;
		gHintsRemained = NUMBER_OF_HINTS;
		gSelectedCards = new CardList();
		gSelectedViews = new ArrayList<CardView>();
		mHintButton.setText(getString(R.string.classic_hint, gHintsRemained));
	}

	private void pauseGame() {
		mCardGrid.showReverse();
		pauseTimer();
	}

	private void resumeGame() {
		mCardGrid.hideReverse();
		// if (gRestoredGame && !gGameStarted) {
		// gGameStarted = true;
		// }
		startTimer();
	}

	private void endGame() {
		showEndingPause();

		gGameEnded = true;

		if (TrioSettings.usesOpenFeint(this)) {
			submitToOpenFeint();
		}

	}

	private void submitToOpenFeint() {

		TrioSettings.submitToOpenFeint(TrioSettings.LEADERBOARD_CLASSIC_ID,
				gElapsedTime, gElapsedTimeString);
		// TrioSettings.submitToOpenFeint(TrioSettings.LEADERBOARD_TOTAL_ID,
		// TrioSettings.getClassicGamePoints(gElapsedTime));

		// finish game achievement
		Achievement endingAchievement = new Achievement(
				TrioSettings.ACHIEVEMENT_CLASSIC_FINISH);
		if (!endingAchievement.isUnlocked) {
			endingAchievement.unlock(new UnlockCB() {
				@Override
				public void onSuccess(boolean newUnlock) {
					// TODO Auto-generated method stub

				}
			});
		}

		// no hints used achievement
		if (gHintsRemained == NUMBER_OF_HINTS) {
			Achievement pureAchievement = new Achievement(
					TrioSettings.ACHIEVEMENT_CLASSIC_FAIR);
			if (!pureAchievement.isUnlocked) {
				pureAchievement.unlock(new UnlockCB() {
					@Override
					public void onSuccess(boolean newUnlock) {
						// TODO Auto-generated method stub

					}
				});
			}
		}

		// turtle speed achievement
		if (gElapsedTime < 10L * 60 * 1000) {
			Achievement turtleAchievement = new Achievement(
					TrioSettings.ACHIEVEMENT_CLASSIC_GOOD);
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
		if (gElapsedTime < 5L * 60 * 1000) {
			Achievement gepardAchievement = new Achievement(
					TrioSettings.ACHIEVEMENT_CLASSIC_QUICK);
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
		if (gElapsedTime < 2L * 60 * 1000) {
			Achievement blitzAchievement = new Achievement(
					TrioSettings.ACHIEVEMENT_CLASSIC_BLITZ);
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
		// super.onBackPressed();

		if (mPauseOverlay.getVisibility() == View.VISIBLE && !gGameEnded
				&& gGameStarted) {
			hidePause();
		} else if (gGameEnded) {
			showEndingPause();
		} else if (!gGameStarted && !gRestoredGame) {
			showStartPause();
		} else {
			showPause();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		if (mPauseOverlay.getVisibility() == View.VISIBLE) {
			hidePause();
		} else {
			showPause();
		}
	}

	@Override
	protected void onPause() {

		pauseTimer();

		if (!gGameEnded) {
			saveGame();
		}

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

	protected void onNotTrioFound(CardList selectedCards,
			ArrayList<CardView> selectedViews) {
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

	protected void onTrioFound(CardList selectedCards,
			ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio found");

		mSoundManager.playSound(SoundManager.SOUND_SUCCESS);

		mTrio.foundTrio(selectedCards);
		mCardGrid.updateGrid(mTrio.getTable());

		gTriosFound += 1;

		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
			endGame();
		} else {

		}

	}

	private boolean saveGame() {

		SharedPreferences.Editor ed = mPrefs.edit();

		ed.putString("game_string", mTrio.getGameString());
		ed.putString("game", mTrio.getGame().toString());
		ed.putString("table", mTrio.getTable().toString());
		ed.putInt("trios_found", gTriosFound);
		ed.putInt("hints", gHintsRemained);
		ed.putLong("time", gElapsedTime);
		ed.putBoolean("saved_game", true);

		if (ed.commit()) {
			if (Trio.LOCAL_LOGV)
				Log.v("Classic Game", "saved progress");
			return true;
		} else {
			Log.e("Classic Game", "failed to save progress");
			return false;
		}
	}

	private boolean clearSavedGame() {

		SharedPreferences.Editor ed = mPrefs.edit();
		ed.putBoolean("saved_game", false);

		if (ed.commit()) {
			if (Trio.LOCAL_LOGV)
				Log.v("Classic Game", "unsaved progress");
			return true;
		} else {
			Log.e("Classic Game", "failed to change progress");
			return false;
		}

	}

	private boolean restoreSavedGame() {

		if (mPrefs.getBoolean("saved_game", false)) {

			String game = mPrefs.getString("game", "");
			String table = mPrefs.getString("table", "");
			String gameString = mPrefs.getString("game_string", "");
			gHintsRemained = mPrefs.getInt("hints", NUMBER_OF_HINTS);
			gTriosFound = mPrefs.getInt("trios_found", 0);
			gElapsedTime = mPrefs.getLong("time", 0L);

			int seconds = (int) gElapsedTime / 1000;
			int minutes = seconds / 60;
			seconds %= 60;

			if (seconds < 10) {
				gElapsedTimeString = minutes + ":0" + seconds;
			} else {
				gElapsedTimeString = minutes + ":" + seconds;
			}

			if (!game.equals("") && !table.equals("")) {

				mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
				mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
				mTrio.setGameString(gameString);

				if (Trio.LOCAL_LOGV)
					Log.v("Classic Game", "Restored saved game");
				return true;

			} else {
				Log.e("TrioState", "failed to restore saved game");
			}

		}

		return false;
	}


	private void startTimer() {

		mTimerStart = System.currentTimeMillis() - gElapsedTime;

		mHandler.removeCallbacks(mUpdateTimer);
		// mHandler.post(mUpdateTimer);
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
	 * Button handlers
	 */

	public void onPauseClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		showPause();
	}

	public void onHintClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		if (gHintsRemained > 0) {
			ArrayList<CardList> trios = mTrio.getTable().getTrios();
			int selectedSize = gSelectedCards.size();

			if (trios.size() > 0) {

				// if no cards were selected before, select the first card
				// possible - it is being done in the end of method

				// if one card was selected, check if it makes trio with
				// others. If yes, show next card, if no, deselect it and
				// select another one
				if (selectedSize == 1) {

					Card selected = gSelectedCards.get(0);

					for (CardList trio : trios) {

						if (trio.contains(selected)) {
							for (Card c : trio) {
								if (!c.equals(selected)) {

									gSelectedCards.add(c);
									gSelectedViews.add(mCardGrid.select(c));
									// markViewSelected(mGrid
									// .findViewWithTag(c));

									useHint();

									if (Trio.LOCAL_LOGV)
										Log.v("Single Game Hint",
												"Hint showed second card");
									return;
								}
							}
						}

					}

					// if there're two cards selected check if they make any
					// trio, if yes do not give additional hints, if no
					// deselect them and select first one
				} else if (selectedSize == 2) {
					Card sel1 = gSelectedCards.get(0);
					Card sel2 = gSelectedCards.get(1);
					for (CardList trio : trios) {
						if (trio.contains(sel1) && trio.contains(sel2)) {
							Toast.makeText(getApplicationContext(),
									getString(R.string.hints_ended),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}

				gSelectedCards.clear();
				Card firstCard = trios.get(0).get(0);

				mCardGrid.deselectAll();
				gSelectedCards.add(firstCard);
				gSelectedViews.add(mCardGrid.select(firstCard));

				useHint();

				if (Trio.LOCAL_LOGV)
					Log.v("Single Game Hint", "Hint showed first card");
				return;
			} else {
				if (Trio.LOCAL_LOGD)
					Log.d("Single Game Hint",
							"Trios were empty when they shouldn't");
			}
		} else {
			Toast.makeText(this, getString(R.string.classic_hints_finished), Toast.LENGTH_SHORT).show();
		}
	}

	public void useHint() {
		gHintsRemained -= 1;
		gElapsedTime += 30000;
		startTimer();
		mHintButton.setText(getString(R.string.classic_hint, gHintsRemained));
	}

	/**
	 * Pause Handlers
	 */

	public void onPauseShow() {
		pauseGame();

		TextView timeView = (TextView) mPauseOverlay
				.findViewById(R.id.gameTime);
		TextView hintsView = (TextView) mPauseOverlay
				.findViewById(R.id.gameHintCount);
		TextView statusView = (TextView) mPauseOverlay
				.findViewById(R.id.gameStatus);
		TextView trioView = (TextView) mPauseOverlay
				.findViewById(R.id.gameTrioCount);

		timeView.setText(gElapsedTimeString);
		hintsView
				.setText(getString(R.string.classic_hint_count, gHintsRemained));
		trioView.setText(getString(R.string.classic_trio_count, gTriosFound));
		statusView.setText(getString(R.string.classic_status, mTrio.getGame()
				.size()));

		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(getString(R.string.pause_continue));

		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_save_quit));

	}

	public void showStartPause() {
		showPause();

		if (!gRestoredGame) {
			Button buttonContinue = (Button) findViewById(R.id.gameContinue);
			buttonContinue.setVisibility(View.VISIBLE);
			buttonContinue.setText(getString(R.string.pause_start));

			Button buttonNewGame = (Button) findViewById(R.id.gameNew);
			buttonNewGame.setVisibility(View.GONE);

			TextView timeView = (TextView) mPauseOverlay
					.findViewById(R.id.gameTime);
			TextView hintsView = (TextView) mPauseOverlay
					.findViewById(R.id.gameHintCount);
			TextView statusView = (TextView) mPauseOverlay
					.findViewById(R.id.gameStatus);
			TextView trioView = (TextView) mPauseOverlay
					.findViewById(R.id.gameTrioCount);

			timeView.setText(getString(R.string.classic_objective));
			hintsView.setText(getString(R.string.classic_hints));
			trioView.setText(getString(R.string.classic_subobjective));
			statusView.setText(getString(R.string.classic_ends));

		}

		// TextView timeView = (TextView)
		// mPauseOverlay.findViewById(R.id.gameTime);
		// timeView.setText(getString(R.string.practice_objective));
		//
		// TextView trioView = (TextView)
		// mPauseOverlay.findViewById(R.id.gameTrioCount);
		// trioView.setText(getString(R.string.practice_subobjective));
	}

	public void showPause() {
		onPauseShow();
		mPauseOverlay.setVisibility(View.VISIBLE);
	}

	public void showEndingPause() {
		showPause();

		TextView statusView = (TextView) mPauseOverlay
				.findViewById(R.id.gameStatus);
		statusView.setText(getString(R.string.classic_end));

		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);

		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_quit));
	}

	public void hidePause() {
		onPauseHide();
		mPauseOverlay.setVisibility(View.INVISIBLE);
	}

	public void onPauseHide() {
		resumeGame();
	}

	public void onPressedContinue(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		gGameStarted = true;
		hidePause();
	}

	public void onPressedNewGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		mTrio.newGame();

		// if (gGameEnded) {
		mCardGrid.setCards(mTrio.getTable());
		// mCardGrid.render();
		// } else {
		// mCardGrid.updateGrid(mTrio.getTable());
		// }

		resetGameStatus();
		showStartPause();

	}

	public void onPressedRestartGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		mTrio.restartGame(mTrio.getGameString());

		// if (gGameEnded) {
		mCardGrid.setCards(mTrio.getTable());
		// mCardGrid.render();
		// } else {
		// mCardGrid.updateGrid(mTrio.getTable());
		// }

		resetGameStatus();
		showStartPause();
	}

	public void onPressedQuitGame(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		if (gGameEnded) {
			clearSavedGame();
		} else {
			saveGame();
		}

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

}
