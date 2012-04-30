package com.barcicki.trio.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.barcicki.trio.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {

	public static int SOUND_BACKGROUND = 1;
	public static int SOUND_CLICK = 2;
	public static int SOUND_FAIL = 3;
	public static int SOUND_SUCCESS = 4;
	private static int MAX_STREAMS = 5;

	private static SoundManager mInstance;

	private Context mContext;
	private static boolean mMute;
	//
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private MediaPlayer mBackgroundPlayer;

	private boolean loadedSounds = false;
	// private boolean initialized = false;

	private SoundManager(Context context) {
		mContext = context;
		mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mBackgroundPlayer = null;
	}

	public static SoundManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SoundManager(context);
			mInstance.loadSounds();
		}
		return mInstance;
	}
	
	public void loadSounds() {
		mBackgroundPlayer = MediaPlayer.create(mContext, R.raw.music);
		addSound(SOUND_CLICK, R.raw.select);
		addSound(SOUND_SUCCESS, R.raw.success);
		addSound(SOUND_FAIL, R.raw.fail);
	}

	public void addSound(int index, int soundId) {
		mSoundPoolMap.put(index, mSoundPool.load(mContext, soundId, 1));
	}
	
	public void playBackground() {
		if (!mBackgroundPlayer.isPlaying()) {
			mBackgroundPlayer.setLooping(true);
			mBackgroundPlayer.start();
		}
	}
	
	public void pauseBackground() {
		if (mBackgroundPlayer.isPlaying()) {
			mBackgroundPlayer.pause();
		}
	}
	
	public void stopBackground() {
		mBackgroundPlayer.stop();
	}
	
	public boolean isBackgroundPlaying() {
		return mBackgroundPlayer.isPlaying();
	}

	public void playSound(int index) {
		if (TrioSettings.readBooleanPreference(mContext, "play_sounds", true)) {
			float streamVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume
					/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
					1, 0, 1f);
		}
	}

	public void playLoopedSound(int index) {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
				1, -1, 1f);
	}
	
	public void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
	
	public void pauseSound(int index)
	{
		mSoundPool.pause(mSoundPoolMap.get(index));
	}
	
	public void pauseAll() {
		Iterator iterator = mSoundPoolMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Integer> pair = (Entry<Integer, Integer>) iterator.next();
			pauseSound(pair.getKey());
		}
	}
	
	public void stopAll() {
		Iterator iterator = mSoundPoolMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Integer> pair = (Entry<Integer, Integer>) iterator.next();
			stopSound(pair.getKey());
		}
	}
	
	public void release() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		mAudioManager.unloadSoundEffects();
		mBackgroundPlayer.release();
		mBackgroundPlayer = null;
		mInstance = null;
	}

}