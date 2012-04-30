package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;
import com.barcicki.trio.core.SoundManager;

public class TutorialStep3Activity extends FragmentActivity {
	
	Tutorial3Fragment quizFragment;
	SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_step3);
		
		mSoundManager = SoundManager.getInstance(this);
		quizFragment = (Tutorial3Fragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

	}

	public void onBackClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		finish();
	}

	public void onSelectCard(View v) {
//		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		quizFragment.onSelectCard(v);
	}
	
	public void onPrevSetClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		quizFragment.onPrevSetClicked(v);
	}
	
	public void onNextSetClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		quizFragment.onNextSetClicked(v);
	}
	
	public void onQuitClicked(View v) {
		mSoundManager.playSound(SoundManager.SOUND_CLICK);
		setResult(TutorialActivity.EXIT_CODE);
		finish();
	}
}
