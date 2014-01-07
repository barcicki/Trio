package com.barcicki.trio.core;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class GameTime {

	final static long SECOND = 1000L;

	GameTimeListener mListener = null;

	long mTimerStart = 0L;
	long mSavedTime = 0L;
	long mCheckpointTime = 0L;

	boolean mIsCountdownEnabled = false;
	long mCountdown = 0L;

	boolean mIsTicking = false;

	private Handler mTimeHandler = new Handler();
	private Runnable mTimeTick = new Runnable() {
		public void run() {

			// update time
			long elapsedTime = System.currentTimeMillis() - mTimerStart;

			// schedule event for next second
			mTimeHandler.postAtTime(this, SystemClock.uptimeMillis() + SECOND);

			// inform listener about the time changed
			if (mListener != null) {
				mListener.onTimerTick();

				if (mIsCountdownEnabled && elapsedTime >= mCountdown) {
					mListener.onCountdownFinish();
				}
			}
		}
	};

	public GameTime(GameTimeListener listener) {
		mListener = listener;
	}

	public void start(long previouslyElapsedTime) {
		mTimerStart = mCheckpointTime = System.currentTimeMillis() - previouslyElapsedTime;
		mSavedTime = 0L;

		// ensure that no callback is assigned
		mTimeHandler.removeCallbacks(mTimeTick);

		// schedule first tick
		long delay = (SECOND - (previouslyElapsedTime % SECOND) + 1) % SECOND;

		mTimeHandler.postDelayed(mTimeTick, delay);
		mIsTicking = true;

		if (Trio.LOCAL_LOGD) 
			Log.d("TIMER", "Starting at " + delay + " elapsed: " + previouslyElapsedTime);
	}

	public void start() {
		start(0L);
	}

	public void unpause() {
		start(mSavedTime);
	}

	public void pause() {
		mSavedTime = getElapsedTime();
		mTimeHandler.removeCallbacks(mTimeTick);
		mIsTicking = false;
	}

	public void addTime(long time) {
		mTimerStart -= time;

		// inform listener about the time changed
		if (mListener != null) {
			mListener.onTimerTick();

			if (mIsCountdownEnabled && getElapsedTime() >= mCountdown) {
				mListener.onCountdownFinish();
			}
		}

		if (mIsTicking) {
			start(getElapsedTime());
		}
	}

	public long checkpoint() {
		long elapsedTime = System.currentTimeMillis() - mCheckpointTime;
		mCheckpointTime = System.currentTimeMillis();

		return elapsedTime;
	}

	public void setCountdown(Long time) {
		if (time != null) {
			mIsCountdownEnabled = true;
			mCountdown = time;
		} else {
			mIsCountdownEnabled = false;
		}
	}

	public void setListener(GameTimeListener listener) {
		mListener = listener;
	}

	public void setElapsedTime(long value) {
		mSavedTime = value;
	}

	public long getElapsedTime() {
		return mIsTicking ? System.currentTimeMillis() - mTimerStart : mSavedTime;
	}

	public String getElapsedTimeAsString(boolean withMiliseconds) {
		return timeToString(getElapsedTime(), withMiliseconds);
	}

	public long getRemainingTime() {
		return mCountdown - getElapsedTime() + SECOND;
	}

	public String getRemainingTimeAsString(boolean withMiliseconds) {
		return timeToString(getRemainingTime(), withMiliseconds);
	}

	public interface GameTimeListener {
		public void onTimerTick();

		public void onCountdownFinish();
	}

	public static String timeToString(long value, boolean withMiliseconds) {
		StringBuilder timeString = new StringBuilder();
		long seconds = value / 1000;
		long minutes = seconds / 60;
		seconds %= 60;
		long miliseconds = value % 1000;

		timeString.append(minutes);
		timeString.append(seconds < 10 ? ":0" : ":");
		timeString.append(seconds);

		if (withMiliseconds) {
			timeString.append(miliseconds < 100 ? (miliseconds < 10 ? ".00" : ".0") : ".");
			timeString.append(miliseconds);
		}

		return timeString.toString();
	}
}
