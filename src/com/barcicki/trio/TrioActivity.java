package com.barcicki.trio;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.barcicki.trio.core.CardViewResources;
import com.barcicki.trio.core.SoundManager;
import com.barcicki.trio.core.TrioSettings;
import com.google.example.games.basegameutils.BaseGameActivity;

public class TrioActivity extends BaseGameActivity {	
	
	private SoundManager mSoundManager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		overridePendingTransition(R.anim.pull_bottom, R.anim.push_top);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		CardViewResources.initialize(this);
		TrioSettings.initialize(this);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			getSoundManager().registerActivityForBackgroundPlayback(this);
			updateMusicButtonStatus();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	protected void onStop() {
		getSoundManager().unregisterActivtyForBackgroundPlayback(this);
		super.onStop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		MenuItem item = menu.findItem(R.id.mute);
		updateMenuMusicButtonStatus(item);

		return true;
	}
	
	public void openSettingsActvity() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			openSettingsActvity();
			return true;
		case R.id.mute:
			TrioSettings.setMusicEnabled(!TrioSettings.isMusicEnabled());
			getSoundManager().updateBackgroundPlaybackStatus();
			updateMusicButtonStatus();
			updateMenuMusicButtonStatus(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void startHomeActivity() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	public SoundManager getSoundManager() {
		if (mSoundManager == null) {
			mSoundManager = SoundManager.getInstance(this);
		}
		return mSoundManager;
	}
	
	public void makeSound(int type) {
		if (TrioSettings.isSoundEffectsEnabled()) {
			getSoundManager().playSound(type);
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
	
	private void updateMenuMusicButtonStatus(MenuItem item) {
		if (getSoundManager().isBackgroundPlaying()) {
			item.setTitle(R.string.settings_mute);
			item.setIcon(android.R.drawable.ic_media_pause);
		} else {
			item.setTitle(R.string.settings_unmute);
			item.setIcon(android.R.drawable.ic_media_play);
		}
	}
	
	private void updateMusicButtonStatus() {
		Button musicButton = (Button) findViewById(R.id.buttonMusicSwitch);
		if (musicButton != null) {
			if (getSoundManager().isBackgroundPlaying()) {
				musicButton.setBackgroundResource(R.drawable.nice_button);
			} else {
				musicButton.setBackgroundResource(R.drawable.no_music_button);
			}
		}
	}
	
	public void onMusicButtonPressed(View v) {
		TrioSettings.setMusicEnabled(!TrioSettings.isMusicEnabled());
		getSoundManager().updateBackgroundPlaybackStatus();
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
	
	public void onSignInFailed() {
	}

	public void onSignInSucceeded() {
	}
	
}
