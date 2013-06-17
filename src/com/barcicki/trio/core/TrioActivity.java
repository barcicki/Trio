package com.barcicki.trio.core;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.barcicki.trio.HomeActivity;
import com.barcicki.trio.R;
import com.google.example.games.basegameutils.BaseGameActivity;

public class TrioActivity extends BaseGameActivity {	
	
	private SoundManager mSoundManager;
	private TrioSettings mSettings;
	
	private boolean mSoundContinue = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		overridePendingTransition(R.anim.pull_bottom, R.anim.push_top);
		
		mSoundManager = SoundManager.getInstance(this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		CardViewResources.initialize(this);
		TrioSettings.initialize(this);
	}
	
	@Override
	protected void onResume() {
		mSoundManager = SoundManager.getInstance(this);
		playBackgroundMusic();
		mSoundContinue = false;
		updateMusicButtonStatus();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if (mSoundManager != null && mSoundManager.isBackgroundPlaying() && !mSoundContinue) {
			mSoundManager.pauseBackground();
		}
		super.onPause();
	}
	
	public void playBackgroundMusic() {
		if (TrioSettings.isMusicEnabled()) {
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
		if (TrioSettings.isMusicEnabled()) {
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
		Button musicButton = (Button) findViewById(R.id.buttonMusicSwitch);
		if (musicButton != null) {
			if (mSoundManager.isBackgroundPlaying()) {
				musicButton.setBackgroundResource(R.drawable.nice_button);
			} else {
				musicButton.setBackgroundResource(R.drawable.no_music_button);
			}
		}
	}
	
	public void onMusicButtonPressed(View v) {
		boolean musicStatus = TrioSettings.isMusicEnabled();
		
		if (musicStatus) {
			if (getSoundManager().isBackgroundPlaying()) getSoundManager().pauseBackground();
		} else if (!getSoundManager().isBackgroundPlaying()){
			getSoundManager().playBackground();
		}
		
		TrioSettings.setMusicEnabled(!musicStatus);
		updateMusicButtonStatus();
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.pull_top, R.anim.push_bottom);
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.pull_bottom, R.anim.push_top);
	}
	
	@Override
	protected void onDestroy() {
//		mSoundManager.release();
		super.onDestroy();
	}

	public void onSignInFailed() {
//		Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
	}

	public void onSignInSucceeded() {
//		Toast.makeText(this, "Succeded to sign in", Toast.LENGTH_SHORT).show();
	}
	
}
