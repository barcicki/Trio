package com.barcicki.trio.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.barcicki.trio.R;

public class SoundManager {

	public static int SOUND_BACKGROUND = 1;
	public static int SOUND_CLICK = 2;
	public static int SOUND_FAIL = 3;
	public static int SOUND_SUCCESS = 4;
	private static int MAX_STREAMS = 5;

	private static SoundManager mInstance;

	private Context mContext;
	
	private SoundPool mSoundPool;
	private SparseIntArray mSoundPoolMap = new SparseIntArray();
	private AudioManager mAudioManager;
	private MediaPlayer mBackgroundPlayer;

	private boolean mLoadedSounds = false;

	private SoundManager(Context context) {
		mContext = context;
		mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mBackgroundPlayer = null;
	}

	public static SoundManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SoundManager(context);
		}
		return mInstance;
	}

	public void loadSoundsIfNecessary() {
		if (!mLoadedSounds) {
			addSound(SOUND_CLICK, R.raw.select);
			addSound(SOUND_SUCCESS, R.raw.success);
			addSound(SOUND_FAIL, R.raw.fail);
			mLoadedSounds = true;
		}
	}

	public void addSound(int index, int soundId) {
		mSoundPoolMap.put(index, mSoundPool.load(mContext, soundId, 1));
	}

	public void playBackground() {
		if (mBackgroundPlayer == null) {
			mBackgroundPlayer = MediaPlayer.create(mContext, R.raw.music);
		}
		
		if (!mBackgroundPlayer.isPlaying()) {
			mBackgroundPlayer.setLooping(true);
			mBackgroundPlayer.start();
		}
	}

	public void pauseBackground() {
		if (mBackgroundPlayer != null && mBackgroundPlayer.isPlaying()) {
			mBackgroundPlayer.pause();
		}
	}

	public void stopBackground() {
		if (mBackgroundPlayer != null) {
			mBackgroundPlayer.stop();
		}
	}

	public boolean isBackgroundPlaying() {
		return mBackgroundPlayer != null ? mBackgroundPlayer.isPlaying() : false;
	}

	public void playSound(int index) {
		loadSoundsIfNecessary();
		
		float volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int soundId = mSoundPoolMap.get(index, -1);
		
		if (soundId > 0) {
			mSoundPool.play(soundId, volume, volume, 1, 0, 1f);	
		}
	}

	public void stopSound(int index) {
		int soundId = mSoundPoolMap.get(index, -1);
		
		if (soundId > 0) {
			mSoundPool.stop(soundId);	
		}
	}

	public void pauseSound(int index) {
		int soundId = mSoundPoolMap.get(index, -1);
		
		if (soundId > 0) {
			mSoundPool.pause(soundId);	
		}
	}

	public void pauseAll() {
		for (int i = 0, size = mSoundPoolMap.size(); i < size; i += 1) {
			pauseSound(mSoundPoolMap.keyAt(i));
		}
	}

	public void stopAll() {
		for (int i = 0, size = mSoundPoolMap.size(); i < size; i += 1) {
			stopSound(mSoundPoolMap.keyAt(i));
		}
	}

	public void release() {
		
		if (mSoundPool != null) {
			mSoundPool.release();
			mSoundPool = null;
		}
		
		mSoundPoolMap.clear();
		mLoadedSounds = false;
		
		if (mAudioManager != null) {
			mAudioManager.unloadSoundEffects();
		}
		
		if (mBackgroundPlayer != null) {
			mBackgroundPlayer.release();
			mBackgroundPlayer = null;
		}
		
		mInstance = null;
	}

}