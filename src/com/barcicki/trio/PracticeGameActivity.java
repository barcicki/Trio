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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class PracticeGameActivity extends TrioGameActivity {
	private static final String	TRIO_LIST_DELIMITER	= ";";
	private static final int NUMBER_OF_TRIOS = 3;
	
	SharedPreferences mPrefs;
	Trio mTrio = new Trio();
	
	CardList mGame = new CardList();
	CardList gSelectedCards = new CardList();
	ArrayList<CardView> gSelectedViews = new ArrayList<CardView>();
	ArrayList<CardList> gFoundTrios = new ArrayList<CardList>();
	
	CardGrid mTriosGrid;
	CardGrid mCardGrid;

	private TextView mGameStatus;
	private boolean mHasWon = false;
	
	private final int[] HELP_FRAGMENTS = { 
		R.layout.help_practice_fragment1, 
		R.layout.help_practice_fragment2, 
		R.layout.help_practice_fragment3 
	};
	
	@Override
	public int getContentView() {
		return R.layout.practice;
	}
	
	@Override
	public int[] getHelpFragments() {
		return HELP_FRAGMENTS;
	}
	
	@Override
	public void initGame() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mTriosGrid = (CardGrid) findViewById(R.id.triosContainer);
		mGameStatus = (TextView) findViewById(R.id.gameStatus);
		
		attachCardListeners();
	}
	
	@Override
	protected boolean hasSeenHelp() {
		return TrioSettings.hasSeenTripleHelp();
	}
	
	@Override
	protected void setSeenHelp() {
		TrioSettings.setSeenTripleHelp(true);
	}
	
	@Override
	public void onGameStarted() {
		super.onGameStarted();
		mCardGrid.hideReverse();
	}
	
	@Override
	public void onGamePaused() {
		mCardGrid.showReverse();
	}
	
	@Override
	public Bundle storeGame(Bundle stateToModify) {
		List<String> foundTrio = new ArrayList<String>();
		for (CardList trio : gFoundTrios) {
			foundTrio.add(trio.toString());
		}
		stateToModify.putString("challenge", mGame.toString());
		stateToModify.putString("found", TextUtils.join(TRIO_LIST_DELIMITER, foundTrio));
		stateToModify.putLong("time", getElapsedTime());
		return stateToModify;
	}
	
	@Override
	public void restoreGame(Bundle savedInstanceState) {
		if (savedInstanceState != null) {

			setElapsedTime(savedInstanceState.getLong("time"));
			
			String foundTrios = savedInstanceState.getString("found");
			String challenge = savedInstanceState.getString("challenge");
			CardList three = new CardList();

			gFoundTrios.clear();
			
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
			
			gSelectedCards = new CardList();
			gSelectedViews = new ArrayList<CardView>();
	
			mGame = CardList.fromString(mTrio.getDeck(), challenge);
	
		} else {
			setElapsedTime(0);
			
			gFoundTrios.clear();
			
			gSelectedCards = new CardList();
			gSelectedViews = new ArrayList<CardView>();
			
			mGame = mTrio.getSetWithTrios(NUMBER_OF_TRIOS);
		}
	}
	
	@Override
	public void onGameReset() {
		super.onGameReset();
		
		if (mCardGrid != null) {
			mCardGrid.setCards(mGame);
		}
		
		if (mTriosGrid != null) {
			mTriosGrid.setEmptyCardList(9);
			mTriosGrid.setResourceImageForAll(R.drawable.square_question);
			mTriosGrid.showReverse();
			
			int index = 0;
			for (CardList trio : gFoundTrios) {
				mTriosGrid.setCardsToCardViews(trio, index);
				mTriosGrid.revealCard(trio);
				index += trio.size();
			}
		}
		
		mHasWon = false;
	}
	
	@Override
	public void onGameUpdate() {
		super.onGameUpdate();
		mGameStatus.setText("" + (NUMBER_OF_TRIOS - gFoundTrios.size()));
	}
	
	@Override
	public void onGameEnded(boolean won) {
		super.onGameEnded(won);
		
		mHasWon = won;
		
		if (isSignedIn() && mHasWon) {
			GoogleApiClient client = getApiClient();
			Games.Achievements.increment(client, getString(R.string.achievement_triple_amateur), 1);
			Games.Achievements.increment(client, getString(R.string.achievement_triple_pro), 1);
			Games.Achievements.increment(client, getString(R.string.achievement_triple_guru), 1);

			Games.Leaderboards.submitScore(client, getString(R.string.leaderboard_triple_trio), getElapsedTime());
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
			
			mTriosGrid.setCardsToCardViews(selectedCards, gFoundTrios.size() * 3);
			gFoundTrios.add(new CardList(selectedCards));
			
			final ArrayList<CardView> views = new ArrayList<CardView>(selectedViews);
			mTriosGrid.setRevealCardListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					for (CardView cv : views) {
						cv.setSelected(false);
						cv.invalidate();
						cv.refreshDrawableState();
					}
				}
			});
			mTriosGrid.revealCard(selectedCards);
			
			saveFoundTrio();
			
			updateGame();
		}
		
		if (gFoundTrios.size() == NUMBER_OF_TRIOS) {
			endGame(true);
		}
				
	}
	
	private void updateOverlayLabels() {
		TextView timeView = (TextView) getPauseOverlay().findViewById(R.id.gameTime);
		timeView.setText(getElapsedTimeAsString(true));
	}
	
	@Override
	public void onPauseOverlayShow() {
		updateOverlayLabels();
		
		TextView trioView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		trioView.setText(getString(R.string.practice_trio_count, gFoundTrios.size(), NUMBER_OF_TRIOS ));
		
		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setText(R.string.pause_continue);
		buttonContinue.setVisibility(View.VISIBLE);
		
		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		CardGrid notFoundHolder = (CardGrid) getPauseOverlay().findViewById(R.id.gameNotFound);
		notFoundHolder.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onEndingOverlayShow() {
		updateOverlayLabels();
		
		Button buttonContinue = (Button) getPauseOverlay().findViewById(R.id.gameContinue);
		buttonContinue.setVisibility(View.GONE);
		
		Button buttonNewGame = (Button) getPauseOverlay().findViewById(R.id.gameNew);
		buttonNewGame.setVisibility(View.VISIBLE);
		
		TextView statusView = (TextView) getPauseOverlay().findViewById(R.id.gameTrioCount);
		CardGrid notFoundHolder = (CardGrid) getPauseOverlay().findViewById(R.id.gameNotFound);
		
		if (mHasWon) {
			statusView.setText(getString(R.string.practice_end));	
			notFoundHolder.setVisibility(View.INVISIBLE);
		} else {
			statusView.setText(getString(R.string.practice_resigned, NUMBER_OF_TRIOS - gFoundTrios.size()));
			
			CardList cards = mCardGrid.getCardsFromViews();
			
			ArrayList<CardList> trios = cards.getTrios();
			
			notFoundHolder.setVisibility(View.VISIBLE);
			notFoundHolder.setCards(CardList.difference(trios, gFoundTrios));
			notFoundHolder.forceColumnSize(3);
			notFoundHolder.render();
		}
	}
		
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
	
	public void onResignPressed(View v) {
		makeClickSound();
		endGame(false);
	}
	
}