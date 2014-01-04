package com.barcicki.trio;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.views.CardView;

public class PracticeGameActivity extends TrioGameActivity {
	private static final String	TRIO_LIST_DELIMITER	= ";";
	private static final int NUMBER_OF_TRIOS = 3;
	
	SharedPreferences mPrefs;
	Trio mTrio = new Trio();
	
	CardList mGame = new CardList();
	CardList gSelectedCards = new CardList();
	ArrayList<CardView> gSelectedViews = new ArrayList<CardView>();
	ArrayList<CardList> gFoundTrios = new ArrayList<CardList>();
	
	int gTriosFound = 0;
	int gTriosRemaines = NUMBER_OF_TRIOS;
	
	CardGrid mTriosGrid;
	CardGrid mCardGrid;

	private TextView mGameStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.practice);
		super.onCreate(savedInstanceState);
		
		setHelpFragments(R.layout.help_practice_fragment1, R.layout.help_practice_fragment2, R.layout.help_practice_fragment3);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mTriosGrid = (CardGrid) findViewById(R.id.triosContainer);
		mGameStatus = (TextView) findViewById(R.id.gameStatus);
		
		attachCardListeners();
		
		if (savedInstanceState != null) {
			restoreGame(savedInstanceState);
		} else {
			startPractice();
		}
	
		hideOverlays();		
		
		if (!TrioSettings.hasSeenTripleHelp()) {
			showHelpOverlay();
			TrioSettings.setSeenTripleHelp(true);
		}
	}
	
	private void restoreGame(Bundle savedInstanceState) {
		resetGame();

		setElapsedTime(savedInstanceState.getLong("time"));

		String foundTrios = savedInstanceState.getString("found");
		String challenge = savedInstanceState.getString("challenge");
		CardList three = new CardList();

		if (foundTrios != null && !foundTrios.equals("")) {
			for (String card : TextUtils.split(foundTrios, TRIO_LIST_DELIMITER)) {
				three.addAll(CardList.fromString(mTrio.getDeck(), card));
				if (three.size() == 3) {
					gFoundTrios.add(new CardList(three));
					three.clear();
				}
			}
		} else {
			Log.d("TripleTrio", "Could not restore found trios");
		}

		gTriosFound = gFoundTrios.size();
		gTriosRemaines = NUMBER_OF_TRIOS - gTriosFound;

		if (mGameStatus != null) {
			mGameStatus.setText("" + gTriosRemaines);
		}

		gSelectedCards = new CardList();
		gSelectedViews = new ArrayList<CardView>();

		mGame = CardList.fromString(mTrio.getDeck(), challenge);

		if (mCardGrid != null) {
			mCardGrid.setCards(mGame);
		}
		if (mTriosGrid != null) {
			mTriosGrid.setEmptyCardList(9);
			mTriosGrid.setResourceImageForAll(R.drawable.square_question);
			mTriosGrid.showReverse();
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			restoreGame(savedInstanceState);
		} else {
			startPractice();
		}
		
		showPauseOverlay();
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		List<String> foundTrio = new ArrayList<String>();
		for (CardList trio : gFoundTrios) {
			foundTrio.add(trio.toString());
		}
		outState.putString("challenge", mGame.toString());
		outState.putString("found", TextUtils.join(TRIO_LIST_DELIMITER, foundTrio));
		outState.putLong("time", getElapsedTime());
		super.onSaveInstanceState(outState);
	}
	
	private void startPractice() {
		
		mGame = mTrio.getSetWithTrios( NUMBER_OF_TRIOS );
		mGameStatus.setText("" + gTriosRemaines);
		
		mCardGrid.setCards(mGame);
		
		mTriosGrid.setEmptyCardList(9);
		mTriosGrid.setResourceImageForAll(R.drawable.square_question);
		mTriosGrid.showReverse();
				
		startTimer();
	}
	
	@Override
	public void onGameReset() {
		setElapsedTime(0);
		
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
	
	@Override
	public void onGameFinished() {
		showEndingPause();
		
		if (isSignedIn()) {
			getGamesClient().incrementAchievement(getString(R.string.achievement_triple_amateur), 1);
			getGamesClient().incrementAchievement(getString(R.string.achievement_triple_pro), 1);
			getGamesClient().incrementAchievement(getString(R.string.achievement_triple_guru), 1);
			
			getGamesClient().submitScore(getString(R.string.leaderboard_triple_trio), getElapsedTime());
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
					makeClickSound();
				}
				
			}
		});
	}
	
	protected void onNotTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
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
	
	protected void onTrioAlreadyFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio is already found");
		
		makeFailSound();
		
		for (CardView cv : selectedViews) {
			cv.animateFail();
		}
		
		if (TrioSettings.displaysWhatIsWrong()) {
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
			
			makeSuccessSound();
			gFoundTrios.add(new CardList(selectedCards));
			
			mTriosGrid.setCardsToCardViews(selectedCards, gTriosFound * 3);
			
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
			
			gTriosFound += 1;
			gTriosRemaines -= 1;
			
			mGameStatus.setText("" + gTriosRemaines);
			
			saveFoundTrio();
		}
		
		if (gTriosRemaines == 0) {
			finishGame();
		}
				
	}
	
	
	@Override
	protected void onShowPauseOverlay() {
		pausePractice();
		
		TextView timeView = (TextView) getPauseOverlay().findViewById(R.id.gameTime);
		timeView.setText(getElapsedTimeAsString(true));
		
		TextView trioView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		trioView.setText(getString(R.string.practice_trio_count, gTriosFound, NUMBER_OF_TRIOS ));
//		statusView.setText(getString(R.string.classic_status, mTrio.getGame().size()));
		
		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setText(R.string.pause_continue);
		buttonContinue.setVisibility(View.VISIBLE);
		
		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		CardGrid notFoundHolder = (CardGrid) getPauseOverlay().findViewById(R.id.gameNotFound);
		notFoundHolder.setVisibility(View.INVISIBLE);
		
	}
	
	@Override
	protected void onHidePauseOverlay() {
		resumePractice();
	}

	@Override
	protected void onShowHelpOverlay() {
		pausePractice();
	}

	@Override
	protected void onHideHelpOverlay() {
		resumePractice();
	}
	
	public void showEndingPause() {
		showPauseOverlay();
		
		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);
		
		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		TextView statusView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		statusView.setText(getString(R.string.practice_end));
		
		CardGrid notFoundHolder = (CardGrid) getPauseOverlay().findViewById(R.id.gameNotFound);
		notFoundHolder.setVisibility(View.INVISIBLE);		
		
	}
	
	public void showResignPause() {
		showPauseOverlay();
		
		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);
		
		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		TextView statusView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		statusView.setText(getString(R.string.practice_resigned, gTriosRemaines));
		
		CardGrid notFoundHolder = (CardGrid) getPauseOverlay().findViewById(R.id.gameNotFound);
		CardList cards = mCardGrid.getCardsFromViews();
		
		Log.i("Trio", cards.toString());
		
		ArrayList<CardList> trios = cards.getTrios();
		
		notFoundHolder.setVisibility(View.VISIBLE);
		notFoundHolder.setCards(CardList.difference(trios, gFoundTrios));
		notFoundHolder.forceColumnSize(3);
		notFoundHolder.render();
		
		
	}
	
	public void onPressedContinue(View v) {
		makeClickSound();
		hidePauseOverlay();
	}
	
	public void onPressedNewGame(View v) {
		makeClickSound();
		resetGame();
		startPractice();
		hidePauseOverlay();
	}
	
	public void onPressedQuitGame(View v) {
		makeClickSound();
		startHomeActivity();
		finish();
	}
	
	public void onResignPressed(View v) {
		makeClickSound();
		resign();
	}
	
	@Override
	public void onGameResign() {
		showResignPause();
	}

}
