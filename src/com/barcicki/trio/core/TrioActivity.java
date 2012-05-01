package com.barcicki.trio.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;

public class TrioActivity extends FragmentActivity {	
	private SoundManager mSoundManager;
	private boolean mSoundContinue = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mSoundManager = SoundManager.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		playBackgroundMusic();
		mSoundContinue = false;
		updateMusicButtonStatus();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if (mSoundManager.isBackgroundPlaying() && !mSoundContinue) {
			mSoundManager.pauseBackground();
		}
		super.onPause();
	}
	
	public void playBackgroundMusic() {
		if (TrioSettings.readBooleanPreference(this, "play_music", true)) {
			mSoundManager.playBackground();
		}
	}
	
	public void setMusicContinue(boolean value) {
		mSoundContinue = value;
	}
	
	public void startHomeActivity() {
		setMusicContinue(true);
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	public SoundManager getSoundManager() {
		return mSoundManager;
	}
	
	public void makeSound(int type) {
		if (TrioSettings.readBooleanPreference(this, "play_sounds", true)) {
			mSoundManager.playSound(type);
		}
	}
	
	public void makeClickSound() {
		makeSound(SoundManager.SOUND_CLICK);
	}
	
	public void makeFailSound() {
		makeSound(SoundManager.SOUND_FAIL);
	}
	
	public void makeSuccessSound() {
		makeSound(SoundManager.SOUND_SUCCESS);
	}
	
	private void updateMusicButtonStatus() {
		Button musicButton = (Button) findViewById(R.id.musicSwitch);
		if (musicButton != null) {
			if (mSoundManager.isBackgroundPlaying()) {
				musicButton.setBackgroundResource(R.drawable.nice_button);
			} else {
				musicButton.setBackgroundResource(R.drawable.no_music_button);
			}
		}
	}
	
	public void onMusicButtonPressed(View v) {
		boolean musicStatus = TrioSettings.readBooleanPreference(this, "play_music", true);
		
		if (musicStatus) {
			if (getSoundManager().isBackgroundPlaying()) getSoundManager().pauseBackground();
		} else if (!getSoundManager().isBackgroundPlaying()){
			getSoundManager().playBackground();
		}
		
		TrioSettings.writeBooleanPreference(this, "play_music", !musicStatus);
		updateMusicButtonStatus();
	}
	
	@Override
	protected void onDestroy() {
//		mSoundManager.release();
		super.onDestroy();
	}
	
	// onDestroy -> release
}
