package com.barcicki.trio;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.barcicki.trio.core.CardViewResources;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.TrioSettings;

public class TrioActivity extends FragmentActivity {	
	
	private SoundManager mSoundManager;
	
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		MenuItem item = menu.findItem(R.id.mute);
		if (getSoundManager().isBackgroundPlaying()) {
			item.setTitle(R.string.settings_mute);
			item.setIcon(android.R.drawable.ic_media_pause);
		} else {
			item.setTitle(R.string.settings_unmute);
			item.setIcon(android.R.drawable.ic_media_play);
		}

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.mute:
			if (!getSoundManager().isBackgroundPlaying()) {
				TrioSettings.setMusicEnabled(true);
				playBackgroundMusic();
				item.setTitle(R.string.settings_mute);
				item.setIcon(android.R.drawable.ic_media_pause);
			} else {
				TrioSettings.setMusicEnabled(false);
				getSoundManager().pauseBackground();
				item.setTitle(R.string.settings_unmute);
				item.setIcon(android.R.drawable.ic_media_play);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		if (TrioSettings.isSoundEffectsEnabled()) {
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

//	public void onSignInFailed() {
////		Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
//	}
//
//	public void onSignInSucceeded() {
////		Toast.makeText(this, "Succeded to sign in", Toast.LENGTH_SHORT).show();
//	}
	
}
