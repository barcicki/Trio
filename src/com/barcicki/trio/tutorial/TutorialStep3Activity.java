package com.barcicki.trio.tutorial;

import android.os.Bundle;
import android.view.View;

import com.barcicki.trio.R;
import com.barcicki.trio.core.TrioActivity;

public class TutorialStep3Activity extends TrioActivity {

	Tutorial3Fragment quizFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_step3);
		quizFragment = (Tutorial3Fragment) getSupportFragmentManager()
				.findFragmentById(R.id.quizFragment);
	}

	public void onBackClicked(View v) {
		makeClickSound();
		setMusicContinue(true);
		finish();
	}

	public void onSelectCard(View v) {
		// mSoundManager.playSound(SoundManager.SOUND_CLICK);
		quizFragment.onSelectCard(v);
	}

	public void onPrevSetClicked(View v) {
		makeClickSound();
		quizFragment.onPrevSetClicked(v);
	}

	public void onNextSetClicked(View v) {
		makeClickSound();
		quizFragment.onNextSetClicked(v);
	}

	public void onQuitClicked(View v) {
		makeClickSound();
		setMusicContinue(true);
		setResult(TutorialActivity.EXIT_CODE);
		finish();
	}

}
