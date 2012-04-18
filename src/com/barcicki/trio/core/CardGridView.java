package com.barcicki.trio.core;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.barcicki.trio.R;

public class CardGridView extends LinearLayout {
	
	private CardList cards = new CardList();
	private OnClickListener listener;
	
	private LinearLayout container;
	private ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();
	private ArrayList<CardView> cardViews = new ArrayList<CardView>();
	private int columns = 0;
	private float cardSize = 0f;
	private AnimationListener mRevealCardAnimationListener = null;

	public CardGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
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
	
	public void setCards(ArrayList<CardList> cards) {
		this.cards.clear();
		for (CardList cl : cards) {
			this.cards.addAll(cl);
		}
	}
	
	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	private CardView addCardView(Card card) {
	
		int size = cardViews.size();
		int row = getRow(size);
		
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT, 1f);
		 
		if (container == null) {
			container = new LinearLayout(getContext());
			container.setOrientation(LinearLayout.VERTICAL);
			container.setLayoutParams(rowParams);
			container.setGravity(Gravity.CENTER);
			addView(container);
		}
		
		while (rows.size() <= row) {
			
			LinearLayout rowView = new LinearLayout(getContext());
			
			rowView.setOrientation(LinearLayout.HORIZONTAL);
			rowView.setLayoutParams(rowParams);
			rowView.setGravity(Gravity.CENTER);
			rowView.setTag(R.id.tagCards, new CardList());
			rowView.setTag(R.id.tagViews, new ArrayList<CardView>());
			
			
			container.addView(rowView);
			rows.add(rowView);
		}
		
		
		CardView cardView = new CardView(getContext());
		if (cardSize > 0) {
			cardView.setLayoutParams(new LayoutParams((int) cardSize, (int) cardSize)); 
		} else {
			cardView.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT, 1f));
		}
		cardView.setScaleType(ScaleType.FIT_CENTER);
		cardView.setImageResource(R.drawable.square_reverse);
		cardView.setCard(card);
		cardView.setOnClickListener(this.listener);
		
		LinearLayout rowView = rows.get(row);
		((CardList) rowView.getTag(R.id.tagCards)).add(card);
		((ArrayList<CardView>) rowView.getTag(R.id.tagViews)).add(cardView);
		
		cardView.setTag(rowView);
		rowView.addView(cardView);
				
		cardViews.add(cardView);
		
		return cardView;
	
	}
	
	private int getRow(int position) {
		if (columns > 0) {
			return position / columns;
		} else {
			if (position < 12) {
				return position / 4;
			} else if (position < 15) {
				return position % 4;
			} else {
				return position / 5;
			}
		}
	}
	
	public void revealCard(CardList cardList) {
		int size = cardList.size();
		
		for (LinearLayout row : rows) {
			final CardList rowCards = (CardList) row.getTag(R.id.tagCards);
			if (CardList.areEqual( rowCards, cardList )) {
//				final Animation reflipAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_reflip);
				final ArrayList<CardView> views = (ArrayList<CardView>) row.getTag(R.id.tagViews);
				
				views.get(0).setRevealAnimationListener(mRevealCardAnimationListener);
				for (int i = 0; i < views.size(); i++) {
					views.get(i).animateReveal();
					
				}
				
			}
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
		
		if (Trio.LOCAL_LOGV) {
			Log.v("Classic Game", "Updated " + updated.size() + " " + updated.toString() + "; Replaceable " + replaceable.size());
		}
		
		while (updated.size() > 0) {
			final Card c = updated.get(0);
			
			if (replaceable.size() > 0) {
				CardView cv = replaceable.get(0);

				cv.animateSwitchCard(c);
				
				replaceable.remove(cv);
			} else {
				
				final CardView cv = addCardView(c);
				Animation showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_appear);
				
				cv.startAnimation(showAnimation);
			
			}
			
			updated.remove(c);
		}
		
		while (replaceable.size() > 0) {
			final CardView cv = replaceable.get(0);
			
			Animation hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_remove);
			cv.startAnimation(hideAnimation);
			
			hideAnimation.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					cv.setVisibility(View.GONE);
				}
			});
			
			replaceable.remove(cv);
			cardViews.remove(cv);	
			
		}
		
	}
	
	public void renderGrid() {
		
		removeAllViews();
		cardViews.clear();
		rows.clear();
		container = null;
		
		int size = cards.size();
		if (size > 0) {
			
			for (Card card : cards) {
				addCardView(card).refreshDrawableState();
			}
			
			updateListener();
			
		}
		
		refreshDrawableState();
		
		
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
			cv.invalidate();
			cv.refreshDrawableState();
		}
		
	}
	
	public void setResourceImageForAll(int resId) {
		for (CardView cv : cardViews) {
			cv.setImageResource(resId);
			cv.invalidate();
			cv.refreshDrawableState();
		}
	}

	public void showReverse() {
		for (CardView cv : cardViews) {
			cv.setOverdraw(false);
			cv.invalidate();
			cv.refreshDrawableState();
			cv.setOnClickListener(null);
		}
	}
	
	public void hideReverse() {
		for (CardView cv : cardViews) {
			cv.setOverdraw(true);
			cv.invalidate();
			cv.refreshDrawableState();
			cv.setOnClickListener(listener);
		}
	}

	public CardView select(Card card) {
		for (CardView cv : cardViews) {
			if (cv.getCard().equals(card)) {
				cv.setSelected(true);
				cv.invalidate();
				cv.refreshDrawableState();
				return cv;
			}
		}
		return null;
	}

	public void setPredefinedCardSize(float cardSize) {
		// TODO Auto-generated method stub
		this.cardSize = cardSize;
	}
	
	public float getPredefinedCardSize() {
		return cardSize;
	}

	public void setRevealCardListener(AnimationListener animationListener) {
		mRevealCardAnimationListener  = animationListener;
	}

}
