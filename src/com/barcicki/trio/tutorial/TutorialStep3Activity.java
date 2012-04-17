package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;

public class TutorialStep3Activity extends FragmentActivity {
	
	Tutorial3Fragment quizFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial_step3);
		quizFragment = (Tutorial3Fragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

	}

	public void onNextClicked(View v) {
		
	}

	public void onBackClicked(View v) {
		finish();
	}

	public void onSelectCard(View v) {
		quizFragment.onSelectCard(v);
	}
	
	public void onPrevSetClicked(View v) {
		quizFragment.onPrevSetClicked(v);
	}
	
	public void onNextSetClicked(View v) {
		quizFragment.onNextSetClicked(v);
	}
	
	public void onQuitClicked(View v) {
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
}
