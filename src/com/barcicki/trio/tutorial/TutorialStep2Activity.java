package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.R;
import com.barcicki.trio.core.SoundManager;

public class TutorialStep2Activity extends FragmentActivity {
	private SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mSoundManager = SoundManager.getInstance(this);
		setContentView(R.layout.tutorial_step2);
		
	}
	
	public void onNextClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		Intent intent = new Intent(TutorialStep2Activity.this, TutorialStep3Activity.class);
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
			setResult(TutorialActivity.EXIT_CODE);
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, intent);
	}
	
}
