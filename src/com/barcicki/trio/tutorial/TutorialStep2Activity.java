package com.barcicki.trio.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardView;

public class TutorialStep2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tutorial_step2);
		
		CardView card1 = (CardView) findViewById(R.id.card1);
		CardView card2 = (CardView) findViewById(R.id.card2);
		CardView card3 = (CardView) findViewById(R.id.card3);
		CardView card4 = (CardView) findViewById(R.id.card4);
		CardView card5 = (CardView) findViewById(R.id.card5);
		CardView card6 = (CardView) findViewById(R.id.card6);
		CardView card7 = (CardView) findViewById(R.id.card7);
		CardView card8 = (CardView) findViewById(R.id.card8);
		CardView card9 = (CardView) findViewById(R.id.card9);
		CardView card10 = (CardView) findViewById(R.id.card10);
		CardView card11 = (CardView) findViewById(R.id.card11);
		CardView card12 = (CardView) findViewById(R.id.card12);
		CardView card13 = (CardView) findViewById(R.id.card13);
		CardView card14 = (CardView) findViewById(R.id.card14);
		CardView card15 = (CardView) findViewById(R.id.card15);
		CardView card16 = (CardView) findViewById(R.id.card16);
		CardView card17 = (CardView) findViewById(R.id.card17);
		CardView card18 = (CardView) findViewById(R.id.card18);
		
		// 1 common attribute
		card1.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
		card2.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO));
		card3.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE));
		
		// 2 common attributes
		card10.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_THREE));
		card11.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_THREE));
		card12.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE));
		
		// 3 common attributes
		card7.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_TWO));
		card8.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_TWO));
		card9.setCard(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_TWO));
		
		// no common attributes
		card4.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
		card5.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO));
		card6.setCard(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE));
		
		// wrong shape
		card13.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_HALF, Card.NUMBER_TWO));
		card14.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_ONE));
		card15.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_EMPTY, Card.NUMBER_THREE));
		
		// wrong color
		card16.setCard(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_ONE));
		card17.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_RED, Card.FILL_FULL, Card.NUMBER_THREE));
		card18.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_TWO));
	}
	
	public void onNextClicked(View v) {
		Intent intent = new Intent(TutorialStep2Activity.this, TutorialStep3Activity.class);
		startActivity(intent);
	}
	
	public void onBackClicked(View v) {
		finish();
	}
	
	
}
