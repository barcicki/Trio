package com.barcicki.trio.core;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.barcicki.trio.R;

public class CardGridView extends ScrollView {
	
	private CardList cards = new CardList();
	private OnClickListener listener;
	
	private LinearLayout container;
	private ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();
	private ArrayList<CardView> cardViews = new ArrayList<CardView>();

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
		this.cards.clear();
		this.cards.addAll(cards);
	}
	
	private void addCardView(Card card) {
	
		int size = cardViews.size();
		int row = getRow(size);
		 
		if (container == null) {
			container = new LinearLayout(getContext());
			container.setOrientation(LinearLayout.VERTICAL);
			container.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			container.setGravity(Gravity.CENTER);
			addView(container);
		}
		
		while (rows.size() <= row) {
			LayoutParams rowParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			LinearLayout rowView = new LinearLayout(getContext());
			rowView.setOrientation(LinearLayout.HORIZONTAL);
			rowView.setLayoutParams(rowParams);
			rowView.setGravity(Gravity.CENTER);
			
			container.addView(rowView);
			rows.add(rowView);
		}
		
		
		CardView cardView = new CardView(getContext());
		cardView.setBackgroundResource(R.drawable.square_reverse);
		cardView.setCard(card);
		
		rows.get(row).addView(cardView);
		cardViews.add(cardView);
	
	}
	
	private int getRow(int position) {
		if (position < 12) {
			return position / 4;
		} else if (position < 15) {
			return position % 4;
		} else {
			return position / 5;
		}
	}
	
	
	public void updateGrid(CardList updatedCards) {
		CardList updated = new CardList(updatedCards);
		ArrayList<CardView> replaceable = new ArrayList<CardView>();
		for (CardView cv : cardViews) {
			Card c = cv.getCard();
			if (!updated.contains(c)) {
				replaceable.add(cv);
			} else {
				updated.remove(c);
			}
		}
		
		while (updated.size() > 0) {
			Card c = updated.get(0);
			
			if (replaceable.size() > 0) {
				final CardView cv = replaceable.get(0);
				cv.setTag(c);
				
				Animation flipAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_flip);
				cv.startAnimation(flipAnimation);
				
				flipAnimation.setAnimationListener(new AnimationListener() {
					
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						cv.setCard((Card) cv.getTag());
					}
					
					public void onAnimationEnd(Animation animation) {
						cv.setCard((Card) cv.getTag());
					}
				});
				
				
				replaceable.remove(cv);
			} else {
				addCardView(c);
			}
			
			updated.remove(c);
		}
		
		while (replaceable.size() > 0) {
			CardView cv = replaceable.get(0);
			
			removeView(cv);
			replaceable.remove(cv);
			cardViews.remove(cv);			
		}
	
	}
	
	public void renderGrid() {
		
		removeAllViews();
		cardViews.clear();
		
		int size = cards.size();
		if (size > 0) {
			
			for (Card card : cards) {
				addCardView(card);
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
