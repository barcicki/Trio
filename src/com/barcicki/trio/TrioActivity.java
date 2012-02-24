package com.barcicki.trio;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardAdapter;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.widgets.TrioCardTableView;

public class TrioActivity extends Activity {
	final private Trio mTrio = new Trio();
	
	private Handler mHandler = new Handler();
	private long mTimerStart = 0L;
	private TextView mTimer;
	TrioCardTableView mGrid;
	CardAdapter mAdapter;
	TextView mGameStatus;
	TextView mRestart;
	
	final CardList mSelectedCards = new CardList();
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
//        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
                
        mTrio.newGame();
        mGrid = (TrioCardTableView) findViewById(R.id.cardContainer);
        mGameStatus = (TextView) findViewById(R.id.gameStatusTextView);
        mRestart = (TextView) findViewById(R.id.restartTextView);
        
        mAdapter = new CardAdapter(this, R.layout.single_card, mTrio.getTable());
    	mGrid.setAdapter(mAdapter);
        
        startTimer();
        handleCards();
        handleHints();
        handleRestart();
        updateGameStatus();
           
    }

	private void handleRestart() {
		
		
		mRestart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mTrio.newGame();
				mTimerStart = 0L;
				
				
				startTimer();
				updateGameStatus();

				mAdapter = new CardAdapter(getApplicationContext(), R.layout.single_card, mTrio.getTable());
				mGrid.setAdapter(mAdapter);
				
				mSelectedCards.clear();
				
			}
			
		});
	}

	private void updateGameStatus() {
    	
		if (!mTrio.getTable().hasTrio() && !mTrio.getGame().hasNext()) {
			mGameStatus.setText(getString(R.string.solo_game_ended));
		} else {
			mGameStatus.setText(getString(R.string.solo_game_status, mTrio.getGame().size()));
		}
		
	}

	private void handleHints() {
		
		TextView hintButton = (TextView) findViewById(R.id.hintTextView);
		hintButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ArrayList<CardList> trios = mTrio.getTable().getTrios();
				
				int selectedSize = mSelectedCards.size();
								
				if (trios.size() > 0) {

					// if no cards were selected before, select the first card possible - it is being done in the end of method
					
					// if one card was selected, check if it makes trio with others. If yes, show next card, if no, deselect it and select another one
					if (selectedSize == 1) {
						
						Card selected = mSelectedCards.get(0);
						
						for (CardList trio : trios) {
							
							if (trio.contains(selected)) {
								for (Card c : trio) {
									if (!c.equals(selected)) {
										
										mSelectedCards.add(c);
										markViewSelected(mGrid.findViewWithTag(c));
										
										if (Trio.LOCAL_LOGV) Log.v("Single Game Hint", "Hint showed second card");
										return;
									}
								}
							}
							
						}
							
					// if there're two cards selected check if they make any trio, if yes do not give additional hints, if no deselect them and select first one
					} else if (selectedSize == 2) {
						Card sel1 = mSelectedCards.get(0);
						Card sel2 = mSelectedCards.get(1);
						for (CardList trio : trios) {
							if (trio.contains(sel1) && trio.contains(sel2)) {
								Toast.makeText(getApplicationContext(), getString(R.string.hints_ended), Toast.LENGTH_SHORT).show();
								return;
							}
						}
					}
					
					mSelectedCards.clear();
					Card firstCard = trios.get(0).get(0);
					
					mSelectedCards.add(firstCard);
					
					markAllViewsDeslected();
					markViewSelected(mGrid.findViewWithTag(firstCard));

					if (Trio.LOCAL_LOGV) Log.v("Single Game Hint", "Hint showed first card");
					return;
				
				} else {
					if (Trio.LOCAL_LOGD) Log.d("Single Game Hint", "Trios were empty when they shouldn't");
				}
				
			}

		});
		
	}

	private void startTimer() {
		if (mTimerStart == 0L) {
        	mTimerStart = System.currentTimeMillis();
        	mTimer = (TextView) findViewById(R.id.timePassedTextView);
        	mHandler.removeCallbacks(mUpdateTimer);
            mHandler.postDelayed(mUpdateTimer, 100);
            if (Trio.LOCAL_LOGV) Log.v("Single Game", "Timer started");
        }
	}
    
    private Runnable mUpdateTimer = new Runnable() {
		
		@Override
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
	
	private void markViewSelected(View view) {
		view.setBackgroundColor(getResources().getColor(R.color.card_selected));
	}
	
	private void markViewDeselected(View view) {
		view.setBackgroundColor(getResources().getColor(R.color.card_standby));
	}
	
	private void markAllViewsDeslected() {
		for (Card c : mTrio.getTable()) {
			markViewDeselected(mGrid.findViewWithTag(c));
		}
	}
	
	private void onTrioFound() {
		Toast.makeText(getApplicationContext(), "Trio found", Toast.LENGTH_SHORT).show();
		mTrio.foundTrio(mSelectedCards);
		
		updateGameStatus();
		
	}
	
	private void onTrioNotFound() {
		Toast.makeText(getApplicationContext(), "You're wrong!", Toast.LENGTH_SHORT).show();
	}
    
    private void handleCards() {
    	mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Card card = (Card) arg1.getTag();
				
				if (!mSelectedCards.contains(card)) {
					
					mSelectedCards.add(card);
					markViewSelected(arg1);
					if (Trio.LOCAL_LOGV) Log.v("Single Game", "Selected " + card.toString());
					
				} else {
					mSelectedCards.remove(card);
					markViewDeselected(arg1);
					if (Trio.LOCAL_LOGV) Log.v("Single Game", "Deselected " + card.toString());
				}
				
				if (mSelectedCards.size() == 3) {
					
					if (mSelectedCards.hasTrio()) {
						onTrioFound();						
					} else {
						onTrioNotFound();
					}
					
					mSelectedCards.clear();
					mAdapter.notifyDataSetChanged();
				}
				
			}
    		
		});
    }
}