package com.barcicki.trio.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TableLayout;

public class CardTableView extends TableLayout {
	
	private CardList cards;

	public CardTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CardTableView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	public CardList getCards() {
		return cards;
	}

	public void setCards(CardList cards) {
		this.cards = cards;
		
		populateViews();
		
	}
	
	public void populateViews() {
		removeAllViews();
		
		if (cards != null) {
			
			int size = cards.size();
			int columns = (size > 12) ? 5 : 4;
			int rows = (int) Math.ceil( (double) size / columns);
			
			Log.d("trio", "Counts: " + size + " " + columns + " " + rows);
			
		}
		
	}
	
	 
	

}
