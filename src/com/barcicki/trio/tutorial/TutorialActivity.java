package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.R;

public class TutorialActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tutorial_step1);
		
//		CardView card1 = (CardView) findViewById(R.id.card1);
//		CardView card2 = (CardView) findViewById(R.id.card2);
//		CardView card3 = (CardView) findViewById(R.id.card3);
//		
//		card1.setCard(new Card(Card.SHAPE_CIRCLE, Card.COLOR_BLUE, Card.FILL_FULL, Card.NUMBER_ONE));
//		card2.setCard(new Card(Card.SHAPE_SQUARE, Card.COLOR_RED, Card.FILL_EMPTY, Card.NUMBER_TWO));
//		card3.setCard(new Card(Card.SHAPE_TRIANGLE, Card.COLOR_GREEN, Card.FILL_HALF, Card.NUMBER_THREE));
	}
	
	public void onNextClicked(View v) {
		Intent intent = new Intent(TutorialActivity.this, TutorialStep2Activity.class);
		startActivity(intent);
	}
	
	public void onBackClicked(View v) {
		finish();
	}
}
