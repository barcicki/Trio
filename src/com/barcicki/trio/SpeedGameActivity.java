package com.barcicki.trio;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGrid;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.views.CardView;

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
	
	private final int[] HELP_FRAGMENTS = { 
		R.layout.help_speed_fragment1, 
		R.layout.help_speed_fragment2
	};

	@Override
	public int getContentView() {
		return R.layout.speed;
	}
	
	@Override
	public int[] getHelpFragments() {
		return HELP_FRAGMENTS;
	}
	
	@Override
	public void initGame() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mDeckStatus = (TextView) findViewById(R.id.gameDeckStatus);
		
		attachCardListeners();
	}
	
	@Override
	public void onGameStarted() {
		super.onGameStarted();
		
		if (!TrioSettings.hasSeenSpeedHelp()) {
			pauseGameForHelp();
			TrioSettings.setSeenSpeedHelp(true);
		} else {
			mCardGrid.hideReverse();
		}
	}
	
	@Override
	public Bundle storeGame(Bundle outState) {
		outState.putString("game_string", mTrio.getGameString());
		outState.putString("game", mTrio.getGame().toString());
		outState.putString("table", mTrio.getTable().toString());
		outState.putInt("trios_found", gTriosFound);
		outState.putLong("time", getElapsedTime());
		return outState;
	}
	
	@Override
	public void restoreGame(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			String game = savedInstanceState.getString("game");
			String table = savedInstanceState.getString("table");
			String gameString = savedInstanceState.getString("game_string");
			
			gTriosFound = savedInstanceState.getInt("trios_found");
			
			setElapsedTime(savedInstanceState.getLong("time"));
			setCountdown(TIME_LIMIT);
			
			if (!game.equals("") && !table.equals("")) {
	
				mTrio.setGameString(gameString);
				mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
				mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
				mCardGrid.setCards(mTrio.getTable());
	
			} else {
				Log.e("TrioState", "failed to restore saved game");
			}
		} else {
			setElapsedTime(0);
			setCountdown(TIME_LIMIT);
			
			gTriosFound = 0;
			
			gSelectedCards = new CardList();
			gSelectedViews = new ArrayList<CardView>();
			
			mTrio.newGame();
		}
	}
	
	@Override
	public void onGameReset() {
		super.onGameReset();
		
		if (mCardGrid != null) {
			mCardGrid.setCards(mTrio.getTable());
		}
	}
	
	@Override
	public void onGamePaused() {
		super.onGamePaused();
		mCardGrid.showReverse();
	}
	
	@Override
	public void onGameUpdate() {
		super.onGameUpdate();
		mDeckStatus.setText("" + gTriosFound);
	}
	
	@Override
	public void onGameEnded(boolean won) {
		super.onGameEnded(won);
		
		if (isSignedIn() && won) {
			getGamesClient().incrementAchievement(getString(R.string.achievement_speed_amateur), 1);
			getGamesClient().incrementAchievement(getString(R.string.achievement_speed_pro), 1);
			getGamesClient().incrementAchievement(getString(R.string.achievement_speed_guru), 1);
			
			getGamesClient().submitScore(getString(R.string.leaderboard_speed_trio), gTriosFound);
		}
	}

	private void updateOverlayLabels() {
		TextView timeView = (TextView) getPauseOverlay().findViewById(R.id.gameTime);
		timeView.setText(getString(R.string.speed_time_left, (int) Math.ceil(getRemainingTime() / 1000)));
		
		TextView trioView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		trioView.setText(getString(R.string.speed_found, gTriosFound));
	}
	
	@Override
	public void onPauseOverlayShow() {
		updateOverlayLabels();
		
		Button buttonContinue = (Button) findViewById(R.id.gameContinue);
		buttonContinue.setText(getString(R.string.pause_continue));
		buttonContinue.setVisibility(View.VISIBLE);

		Button buttonNewGame = (Button) findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) findViewById(R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_quit));
	}
	
	@Override
	public void onEndingOverlayShow() {
		updateOverlayLabels();
		
		TextView statusView = (TextView) getPauseOverlay().findViewById(R.id.gameTime);
		statusView.setText(getString(R.string.speed_end));

		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);

		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);

		Button buttonQuit = (Button) getPauseOverlay().findViewById(R.id.gameQuit);
		buttonQuit.setText(getString(R.string.pause_quit));
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
		
		saveFoundTrio();

		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
			endGame(true);
		} 
	}

	
//	private void restoreGame(Bundle savedInstanceState) {
//		resetGame();
//		
//		if (savedInstanceState != null) {
//			String game = savedInstanceState.getString("game");
//			String table = savedInstanceState.getString("table");
//			String gameString = savedInstanceState.getString("game_string");
//			gTriosFound = savedInstanceState.getInt("trios_found");
//			
//			setElapsedTime(savedInstanceState.getLong("time"));
//			
//			if (!game.equals("") && !table.equals("")) {
//	
//				mTrio.setGameString(gameString);
//				mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
//				mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
//				mCardGrid.setCards(mTrio.getTable());
//	
//			} else {
//				Log.e("TrioState", "failed to restore saved game");
//				
//				resetGame();
//			}
//		}
//		
//		setGame();
//	}
	
	/**
	 * Button handlers
	 */
	public void onPressedNewGame(View v) {
		makeClickSound();
		resetGame();
		startGame();
	}

	public void onPressedQuitGame(View v) {
		makeClickSound();
		startHomeActivity();
		finish();
	}
	
	@Override
	public void onCountdownFinish() {
		super.onCountdownFinish();
		endGame(true);
	}

}