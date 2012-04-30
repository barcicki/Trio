package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.R;
import com.barcicki.trio.core.SoundManager;

public class TutorialActivity extends FragmentActivity {
	static int EXIT_CODE = 9;
	private SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mSoundManager = SoundManager.getInstance(this);
		
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
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		Intent intent = new Intent(TutorialActivity.this, TutorialStep2Activity.class);
		startActivityForResult(intent, 1);
	}
	
	public void onBackClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		if (resultCode == TutorialActivity.EXIT_CODE) {
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, intent);
	}
	

}
