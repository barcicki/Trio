package com.barcicki.trio;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGrid;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioGameActivity;
import com.barcicki.trio.core.TrioSettings;

public class SpeedGameActivity extends TrioGameActivity {
	
	final static long TIME_LIMIT = 120000L;
	
	SharedPreferences mPrefs;
	Trio mTrio = new Trio();

	TextView mDeckStatus;
	CardList gSelectedCards = new CardList();
	ArrayList<CardView> gSelectedViews = new ArrayList<CardView>();

	int gTriosFound = 0;
	boolean gRestoredGame = false;

	CardGrid mCardGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.speed);
		super.onCreate(savedInstanceState);

		setHelpFragments(
				R.layout.help_speed_fragment1,
				R.layout.help_speed_fragment2
				);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mDeckStatus = (TextView) findViewById(R.id.gameDeckStatus);
		
		attachCardListeners();
		
		setCountdown(TIME_LIMIT);
		
		if (savedInstanceState != null) {
			restoreGame(savedInstanceState);
		} else {
			startGame();
		}

		hideOverlays();
		
		if (!TrioSettings.hasSeenSpeedHelp()) {
			showHelpOverlay();
			TrioSettings.setSeenSpeedHelp(true);
		}

	}

	private void startGame() {
		mTrio.newGame();
		mCardGrid.setCards(mTrio.getTable());
		mCardGrid.hideReverse();
		mDeckStatus.setText(Integer.toString(0));
		startTimer();
	}

	private void resumeGame() {
		mCardGrid.hideReverse();
		startTimer();
	}

	private void pauseGame() {
		mCardGrid.showReverse();
		pauseTimer();
	}

	
	@Override
	public void onGameFinished() {
		showEndingPause();
	}
	
	@Override
	public void onGameReset() {
		gRestoredGame = false;	
		gTriosFound = 0;
		gSelectedCards = new CardList();
		gSelectedViews = new ArrayList<CardView>();
		mDeckStatus.setText(Integer.toString(0));
	}

	@Override
	protected void onShowPauseOverlay() {
		pauseGame();

		TextView timeView = (TextView) getPauseOverlay().findViewById(
				R.id.gameTime);
		TextView trioView = (TextView) getPauseOverlay().findViewById(
				R.id.gameTrioCount);

		timeView.setText(getString(R.string.speed_time_left, (int) Math.ceil(getRemainingTime() / 1000)));
		trioView.setText(getString(R.string.speed_found, gTriosFound));

		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(getString(R.string.pause_continue));

		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_quit));
	}

	@Override
	protected void onHidePauseOverlay() {
		resumeGame();
	}

	@Override
	protected void onShowHelpOverlay() {
		pauseGame();
	}

	@Override
	protected void onHideHelpOverlay() {
		resumeGame();
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
					makeClickSound();
				}
			}
		});
	}

	@Override
	protected void onPause() {
		showPauseOverlay();
		super.onPause();
	}

	protected void onNotTrioFound(CardList selectedCards,
			ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio NOT found");

		makeFailSound();

		for (CardView cv : selectedViews) {
			cv.animateFail();
		}

		if (TrioSettings.displaysWhatIsWrong()) {
			displayWhatIsWrong(selectedCards);
		}

	}

	protected void onTrioFound(CardList selectedCards,
			ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio found");

		makeSuccessSound();

		mTrio.foundTrio(selectedCards);
		mCardGrid.updateGrid(mTrio.getTable());
		gTriosFound += 1;
		
		mDeckStatus.setText(Integer.toString(gTriosFound));

		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
			finishGame();
		} 
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("game_string", mTrio.getGameString());
		outState.putString("game", mTrio.getGame().toString());
		outState.putString("table", mTrio.getTable().toString());
		outState.putInt("trios_found", gTriosFound);
		outState.putLong("time", getElapsedTime());
		super.onSaveInstanceState(outState);
	}
	
	private void restoreGame(Bundle savedInstanceState) {
		resetGame();
				
		String game = savedInstanceState.getString("game");
		String table = savedInstanceState.getString("table");
		String gameString = savedInstanceState.getString("game_string");
		gTriosFound = savedInstanceState.getInt("trios_found");
		
		setElapsedTime(savedInstanceState.getLong("time"));
		
		onTimerTick();
		
		if (!game.equals("") && !table.equals("")) {

			mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
			mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
			mTrio.setGameString(gameString);

		} else {
			Log.e("TrioState", "failed to restore saved game");
		}
		
	}
	
	/**
	 * Button handlers
	 */

	public void showEndingPause() {
		showPauseOverlay();

		TextView statusView = (TextView) getPauseOverlay().findViewById(
				R.id.gameTime);
		statusView.setText(getString(R.string.speed_end));

		Button buttonContinue = (Button) getPauseOverlay().findViewById(
				R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);

		Button buttonNewGame = (Button) getPauseOverlay().findViewById(
				R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) getPauseOverlay().findViewById(
				R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_quit));
	}

	public void onPressedContinue(View v) {
		makeClickSound();
		hidePauseOverlay();
	}

	public void onPressedNewGame(View v) {
		makeClickSound();
		mTrio.newGame();

		// if (gGameEnded) {
		mCardGrid.setCards(mTrio.getTable());
		// mCardGrid.render();
		// } else {
		// mCardGrid.updateGrid(mTrio.getTable());
		// }

		resetGame();
		// gGameStarted = true;
		// hidePause();

		hidePauseOverlay();
	}

	// public void onPressedRestartGame(View v) {
	// makeClickSound();
	// mTrio.restartGame(mTrio.getGameString());
	//
	// // if (gGameEnded) {
	// mCardGrid.setCards(mTrio.getTable());
	// // mCardGrid.render();
	// // } else {
	// // mCardGrid.updateGrid(mTrio.getTable());
	// // }
	//
	// resetGameStatus();
	// showStartPause();
	// }

	public void onPressedQuitGame(View v) {
		makeClickSound();
		startHomeActivity();
		finish();
	}
	
	@Override
	public void onCountdownFinish() {
		super.onCountdownFinish();
		finishGame();
	}

}