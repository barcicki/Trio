package com.barcicki.trio.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.barcicki.trio.R;

public class TutorialStep2Activity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tutorial_step2);
		
	}
	
	public void onNextClicked(View v) {
		Intent intent = new Intent(TutorialStep2Activity.this, TutorialStep3Activity.class);
		startActivityForResult(intent, 1);
	}
	
	public void onBackClicked(View v) {
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
