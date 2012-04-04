package com.barcicki.trio.core;

import java.util.ArrayList;

import com.barcicki.trio.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CardGridView extends ScrollView {
	
	private CardList cards = new CardList();
	private ArrayList<CardView> cardViews = new ArrayList<CardView>();
	private OnClickListener listener;

	public CardGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public CardGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CardGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CardList getCards() {
		return cards;
	}

	public void setCards(CardList cards) {
		this.cards = cards;
	}
	
	public void renderGrid() {
		
		removeAllViews();
		cardViews.clear();
		
		LayoutParams rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LayoutParams cardParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		cardParams.setMargins(50, 10, 50, 10);
		
		
		int size = cards.size();
		if (size > 0) {
			
			LinearLayout container = new LinearLayout(getContext());
			container.setOrientation(LinearLayout.VERTICAL);
			container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			container.setGravity(Gravity.CENTER);
			addView(container);
			
			int columns = 0;
			if (size > 12) {
				columns = 5;
				
			} else {
				columns = (size > 4) ? 4 : size;
			}
			int rows = (int) Math.ceil( (double)size / columns);
			
			for (int i = 0; i < rows; i++) {
				
				LinearLayout row = new LinearLayout(getContext());
				row.setOrientation(LinearLayout.HORIZONTAL);
				row.setLayoutParams(rowParams);
				row.setGravity(Gravity.CENTER);
				
				
				for (int j = 0; j < columns; j++) {
					
					int position = i * columns + j;
					if (position >= size) break;
					
					CardView cardView = new CardView(getContext());
					cardView.setLayoutParams(cardParams);
					cardView.setCard(cards.get(position));
					cardView.setBackgroundResource(R.drawable.square_reverse);
					
					cardViews.add(cardView);
					row.addView(cardView);
					
				}
				
				container.addView(row);
				
			}
			
			updateListener();
			
		}
		
		
	}

	public void setOnCardClickListener(OnClickListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
		updateListener();
	}

	private void updateListener() {
		// TODO Auto-generated method stub
		for (CardView cv : cardViews) {
			cv.setOnClickListener(this.listener);
		}
		
	}

	public void deselectAll() {
		
		for (CardView cv : cardViews) {
			cv.setSelected(false);
		}
		
	}

}
