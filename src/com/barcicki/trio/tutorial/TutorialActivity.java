package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TrioSettings;

public class TutorialActivity extends TrioActivity {
	static int EXIT_CODE = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_step1);
	}

	public void onNextClicked(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(TutorialActivity.this,
				TutorialStep2Activity.class);
		startActivityForResult(intent, 1);
	}

	public void onBackClicked(View v) {
		makeClickSound();
		startHomeActivity();
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		if (resultCode == TutorialActivity.EXIT_CODE) {
			startHomeActivity();
			finish();
		}

		super.onActivityResult(requestCode, resultCode, intent);
	}

}
