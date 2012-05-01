package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.barcicki.trio.R;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.TrioActivity;

public class TutorialStep2Activity extends TrioActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_step2);
	}
	
	public void onNextClicked(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(TutorialStep2Activity.this, TutorialStep3Activity.class);
		startActivityForResult(intent, 1);
	}
	
	public void onBackClicked(View v) {
		makeClickSound();
		setMusicContinue(true);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		if (resultCode == TutorialActivity.EXIT_CODE) {
			setResult(TutorialActivity.EXIT_CODE);
			setMusicContinue(true);
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, intent);
	}
	
}
