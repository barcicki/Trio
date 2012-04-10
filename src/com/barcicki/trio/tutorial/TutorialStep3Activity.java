package com.barcicki.trio.tutorial;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;

public class TutorialStep3Activity extends Activity {
	private static final int NUMBER_OF_ADDITIONAL_CARDS = 4;
	private int currentSet = 0;
	private ArrayList<TrioSet> sets = new ArrayList<TrioSet>();

	private Button mPrev;
	private Button mNext;
	
	private CardView mCardA;
	private CardView mCardB;
	private CardView mCardToGuess;
	
	private CardView mOptionA;
	private CardView mOptionB;
	private CardView mOptionC;
	private CardView mOptionD;
	private CardView mOptionE;
	
	private Trio mTrio = new Trio();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial_step3);
		
		mPrev = (Button) findViewById(R.id.buttonPrev);
		mNext = (Button) findViewById(R.id.buttonNext);
		
		mCardA = (CardView) findViewById(R.id.card1);
		mCardB = (CardView) findViewById(R.id.card2);
		mCardToGuess = (CardView) findViewById(R.id.card3);
		mOptionA = (CardView) findViewById(R.id.card4);
		mOptionB = (CardView) findViewById(R.id.card5);
		mOptionC = (CardView) findViewById(R.id.card6);
		mOptionD = (CardView) findViewById(R.id.card7);
		mOptionE = (CardView) findViewById(R.id.card8);

		prepareSets();
		showSet();

	}

	public void onNextClicked(View v) {

	}

	public void onBackClicked(View v) {
		finish();
	}

	public void onSelectCard(View v) {

		TrioSet set = sets.get(currentSet);
		CardList selection = new CardList();
		selection.add( set.getCardA() );
		selection.add( set.getCardB() );
		selection.add( ((CardView) v).getCard() );
		
		if (selection.hasTrio()) {
			
			set.setSolved(true);
			
			Animation flipAnimation = AnimationUtils.loadAnimation(this, R.anim.card_flip);
			final Animation reflipAnimation = AnimationUtils.loadAnimation(this, R.anim.card_reflip);
			mCardToGuess.startAnimation(flipAnimation);
			
			flipAnimation.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				public void onAnimationEnd(Animation animation) {
					mCardToGuess.setCard( Trio.getTrioCard(sets.get(currentSet).getCardA(), sets.get(currentSet).getCardB()));
					mCardToGuess.invalidate();
					mCardToGuess.refreshDrawableState();
					mCardToGuess.startAnimation(reflipAnimation);
					reflipAnimation.setAnimationListener(new AnimationListener() {
						
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							
							onNextSetClicked(null);
						}
					});
				}
			});
			
		} else {
			Animation failAnimation = AnimationUtils.loadAnimation(this, R.anim.card_fail);
			v.startAnimation(failAnimation);
		}
		
	}

	private void prepareSets() {
		sets.add(new TrioSet(
				new Card(Card.SHAPE_SQUARE, Card.COLOR_RED,	Card.FILL_EMPTY, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_SQUARE,	Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO), 
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_TWO)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_TRIANGLE,	Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_TWO)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_SQUARE,	Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_TWO)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_THREE)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_THREE)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_HALF, Card.NUMBER_THREE), 
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_THREE)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_EMPTY, Card.NUMBER_TWO), 
				new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_THREE)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_HALF, Card.NUMBER_ONE)));
		sets.add(new TrioSet(
				new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_HALF, Card.NUMBER_ONE), 
				new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_THREE)));

	}
	
	public void showSet() {
		int size = sets.size();
		if (currentSet <= 0) {
			currentSet = 0;
			mPrev.setVisibility(View.INVISIBLE);
		} else {
			mPrev.setVisibility(View.VISIBLE);
		}
		
		if (currentSet >= size - 1) {
			currentSet = size - 1;
			mNext.setVisibility(View.INVISIBLE);
		} else {
			mNext.setVisibility(View.VISIBLE);
		}
		
		TextView summary = (TextView) findViewById(R.id.summary);
		summary.setText( (currentSet+1) + "/" + size );
		
		TrioSet quiz = sets.get( currentSet );
		
		mCardA.setCard( quiz.getCardA() );
		mCardB.setCard( quiz.getCardB() );
		
		if ( quiz.isSolved() ) {
			mCardToGuess.setCard( quiz.getSolution() );
		} else {
			mCardToGuess.setCard( null );
		}
		
		CardList cards = quiz.getTrioQuiz(mTrio.getDeck(),  NUMBER_OF_ADDITIONAL_CARDS );
		
		if (Trio.LOCAL_LOGV) {
			Log.v("Trio Tutorial", "Quiz set: " + cards.toString());
		}
		
		cards.shuffle();
		mOptionA.setCard( cards.get(0) );
		mOptionB.setCard( cards.get(1) );
		mOptionC.setCard( cards.get(2) );
		mOptionD.setCard( cards.get(3) );
		mOptionE.setCard( cards.get(4) );
		
		
		if (Trio.LOCAL_LOGV) {
			Log.v("Trio Tutorial", "CurrentSet" + currentSet);
		}
	}
	
	public void onPrevSetClicked(View v) {
		currentSet -= 1;
		showSet();
	}
	
	public void onNextSetClicked(View v) {
		currentSet += 1;
		showSet();
	}
}
