package com.barcicki.trio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardGridView;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;

public class ClassicGameActivity extends Activity {
	SharedPreferences mPrefs;
	Trio mTrio = new Trio();
	
	Handler mHandler = new Handler();
	long mTimerStart = 0L;
	
//	TrioCardTableView mGrid;
//	RelativeLayout mContainer;
//	CardAdapter mAdapter;
//	TextView mGameStatus;
	
	TextView mTimer;
	
	CardGridView mCardGrid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classic);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mCardGrid = (CardGridView) findViewById(R.id.cardsContainer);
		mTimer = (TextView) findViewById(R.id.gameTimer);
		
		if (!restoreSavedGame()) {
			mTrio.newGame();
		}
		
		applyAnimations();
		
		attachCardListeners();
		attachPauseListener();
		attachHintListener();
		
		startGame();
	
				
	}
	
	private void startGame() {
		
		mCardGrid.setCards(mTrio.getTable());
		mCardGrid.renderGrid();
		
		startTimer();
	}
	
	private void attachHintListener() {
		// TODO Auto-generated method stub
		
	}

	private void attachPauseListener() {
		// TODO Auto-generated method stub
		
	}

	private void attachCardListeners() {
		
		final CardList selectedCards = new CardList();
		final ArrayList<CardView> selectedViews = new ArrayList<CardView>();
		
		mCardGrid.setOnCardClickListener(new OnClickListener() {
			public void onClick(View v) {
				CardView cv = (CardView) v;
				Card card = cv.getCard();
				
				if (!selectedCards.contains(card)) {
					
					selectedCards.add(card);
					selectedViews.add(cv);
					
					cv.setSelected(true);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Selected " + card.toString());
				} else {
					
					selectedCards.remove(card);
					selectedViews.remove(cv);
					
					cv.setSelected(false);
					
					if (Trio.LOCAL_LOGV)
						Log.v("Classic Game", "Deselected " + card.toString());
				}
				
				if (selectedCards.size() == 3) {
					
					if (selectedCards.hasTrio()) {
						onTrioFound(selectedCards, selectedViews);
					} else {
						onNotTrioFound(selectedCards, selectedViews);
					}
					
					selectedCards.clear();
					mCardGrid.deselectAll();
					
					
				} 			
			}
		});
	}

	@Override
	protected void onPause() {
		
		if (mTrio.getTable().hasTrio() || mTrio.getGame().hasNext()) {
			saveGame();
		}
		
		super.onPause();
	}
	
	protected void onNotTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio NOT found");
	}

	protected void onTrioFound(CardList selectedCards, ArrayList<CardView> selectedViews) {
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Trio found");
		
		mTrio.foundTrio(selectedCards);
		
		mCardGrid.updateGrid(mTrio.getTable());
	}

	private void applyAnimations() {
		ImageView clouds = (ImageView) findViewById(R.id.layoutClouds);
		Animation cloudsAnimation = AnimationUtils.loadAnimation(this, R.anim.clouds);
		clouds.startAnimation(cloudsAnimation);
	}
	
	private boolean saveGame() {
		
		SharedPreferences.Editor ed = mPrefs.edit();
		
		ed.putString("game", mTrio.getGame().toString() );
		ed.putString("table", mTrio.getTable().toString() );
		ed.putLong("time", System.currentTimeMillis() - mTimerStart );
		ed.putBoolean("saved_game", true);
		
		if (ed.commit()) {
			if (Trio.LOCAL_LOGV) Log.v("Classic Game", "saved progress");
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
			if (Trio.LOCAL_LOGV) Log.v("Classic Game", "unsaved progress");
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
			long timer = mPrefs.getLong("time", 0L);

			if (!game.equals("") && !table.equals("")) {

				mTrio.setGame(CardList.fromString(mTrio.getDeck(), game));
				mTrio.setTable(CardList.fromString(mTrio.getDeck(), table));
				mTimerStart = System.currentTimeMillis() - timer;

				if (Trio.LOCAL_LOGV)
					Log.v("Classic Game", "Restored saved game");
				return true;

			} else {
				Log.e("TrioState", "failed to restore saved game");
			}

		}

		return false;
	}
	
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
	private void updateGameStatus() {

		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
			
//			mGameStatus.setText(getString(R.string.solo_game_ended));
			clearSavedGame();
			
		} else {
//			mGameStatus.setText(getString(R.string.solo_game_status, mTrio
//					.getGame().size()));
		}

	}
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
		
		if (mTimerStart == 0L) {
			mTimerStart = System.currentTimeMillis();
		}
		
		mHandler.removeCallbacks(mUpdateTimer);
		mHandler.postDelayed(mUpdateTimer, 100);
		if (Trio.LOCAL_LOGV)
			Log.v("Classic Game", "Timer started");
	}

	private Runnable mUpdateTimer = new Runnable() {

		public void run() {
			final long start = mTimerStart;
			long millis = System.currentTimeMillis() - start;
			int seconds = (int) millis / 1000;
			int minutes = seconds / 60;
			seconds %= 60;

			if (seconds < 10) {
				mTimer.setText("" + minutes + ":0" + seconds);
			} else {
				mTimer.setText("" + minutes + ":" + seconds);
			}

			mHandler.postAtTime(this, SystemClock.uptimeMillis() + 1000);
		}
	};
//
//	private void markViewSelected(View view) {
//		view.setBackgroundColor(getResources().getColor(R.color.card_selected));
//	}
//
//	private void markViewDeselected(View view) {
//		view.setBackgroundColor(getResources().getColor(R.color.card_standby));
//	}
//
//	private void markAllViewsDeslected() {
//		for (Card c : mTrio.getTable()) {
//			markViewDeselected(mGrid.findViewWithTag(c));
//		}
//	}
}
