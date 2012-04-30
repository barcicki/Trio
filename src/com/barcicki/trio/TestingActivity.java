package com.barcicki.trio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.barcicki.trio.core.CardGrid;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.Trio;

public class TestingActivity extends Activity {
	
	SoundManager mSoundManager;
	CardGrid mGrid;
	Trio mTrio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testing);

		mGrid = (CardGrid) findViewById(R.id.cardsContainer);
		mTrio = new Trio();
		
		CardList deck = new CardList(mTrio.getDeck());
		mGrid.setCards( deck.getNext(Trio.DEFUALT_TABLE_SIZE));
//		mGrid.showReverse();
		
		mSoundManager = SoundManager.getInstance(this);
	}
	
	public void doPopulate(View v) {
		
		CardList deck = new CardList(mTrio.getDeck());
		mGrid.updateGrid(deck.getRandomRange(13));
		
	}
	
	public void doClear(View v) {
		
		CardList deck = new CardList(mTrio.getDeck());
		mGrid.updateGrid(deck.getNext(15));
	}
	
	public void doAdd(View v) {
		
	}
	
	public void doRemove(View v) {
		
	}
	
	

}