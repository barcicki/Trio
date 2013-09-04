package com.barcicki.trio;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.HelpAdapter;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.Trio.TrioStatus;
import com.barcicki.trio.core.TrioSettings;
import com.viewpagerindicator.CirclePageIndicator;

abstract public class TrioGameActivity extends TrioActivity {
	
	private static final long TIME_LIMIT = 60000L;
	private static final long REMAINING_EXTRA_SECOND = 1000L;

	private View mPauseOverlay;
	private View mHelpOverlay;
	private ViewPager mHelpViewPager;
	private CirclePageIndicator mHelpIndicator;
	private HelpAdapter mHelpAdapter;
	
	private TextView mTimer;
	private TextView mCountdown;
	private Handler mHandler = new Handler();
	
	private long mTimerStart = 0L;
	private long mElapsedTime = 0L;
	
	private boolean mCountdownEnabled = false;
	private long mCountdownStart = TIME_LIMIT;
//	private long mRemainingTime = 0L;
	
	private boolean mIsGameFinished = false;
	
				
	@Override
	protected void onCreate(Bundle arg0) {
		
		mPauseOverlay = findViewById(R.id.gamePause);
		mHelpOverlay = findViewById(R.id.gameHelp);
		mTimer = (TextView) findViewById(R.id.gameTimer);
		mCountdown = (TextView) findViewById(R.id.gameCountdown);
		
		if (mHelpOverlay != null) {
			mHelpAdapter = new HelpAdapter(this);
			
			mHelpViewPager = (ViewPager) findViewById(R.id.helpHolder);
			mHelpViewPager.setAdapter(mHelpAdapter);
			
			mHelpIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
			mHelpIndicator.setViewPager(mHelpViewPager);
			final float density = getResources().getDisplayMetrics().density;
			mHelpIndicator.setRadius(10 * density);
		}
		
		TrioSettings.setHavePlayed(true);
		
		super.onCreate(arg0);
	}
	
	public void setHelpFragments(Integer... resId) {
		if (mHelpOverlay != null) {
			 for (int id : resId) {
				 mHelpAdapter.addFragment(id);
			 }
		}
		mHelpAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		pauseTimer();
		showPauseOverlay();
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public void onBackPressed() {
		
		if (isHelpOverlayVisible()) {
			hideHelpOverlay();
			hidePauseOverlay();
			return;
		}
		
		if (isPauseOverlayVisible()) {
			startHomeActivity();
			finish();
		} else {
			showPauseOverlay();
		}
		
	}
	
	/* Overlays */
	
	public View getPauseOverlay() {
		return mPauseOverlay;
	}
	
	public View getHelpOverlay() {
		return mHelpOverlay;
	}
	
	public boolean isPauseOverlayVisible() {
		return mPauseOverlay != null && mPauseOverlay.getVisibility() == View.VISIBLE;
	}
	
	public boolean isHelpOverlayVisible() {
		return mHelpOverlay != null && mHelpOverlay.getVisibility() == View.VISIBLE;
	}
	
	public void hideOverlays() {
		hideHelpOverlay();
		hidePauseOverlay();
	}

	public void showPauseOverlay() {
		if (!isPauseOverlayVisible()) {
			mPauseOverlay.setVisibility(View.VISIBLE);
			onShowPauseOverlay();
		}
	}
	
	public void hidePauseOverlay() {
		if (isPauseOverlayVisible()) {
			mPauseOverlay.setVisibility(View.INVISIBLE);
			onHidePauseOverlay();
		}
	}
	
	public void showHelpOverlay() {
		if (!isHelpOverlayVisible()) {
			mHelpOverlay.setVisibility(View.VISIBLE);
			onShowHelpOverlay();
		}
	}
	
	public void hideHelpOverlay() {
		if (isHelpOverlayVisible()) {
			mHelpOverlay.setVisibility(View.INVISIBLE);
			onHideHelpOverlay();
		}
	}
	
	protected void onShowPauseOverlay() {
		pauseTimer();
	}
	
	protected void onShowHelpOverlay() {
		pauseTimer();
	}
	
	protected void onHidePauseOverlay() {
		startTimer();
	}
	
	protected void onHideHelpOverlay() {
		startTimer();
	}
	
	/* Overlay triggers */
	
	public void onPausePressed(View v) {
		makeClickSound();
		showPauseOverlay();
	}
	
	public void onHelpPressed(View v) {
		makeClickSound();
		showHelpOverlay();
	}
	
	/* Default help overlay actions */
	
	public void onContinuePressed(View v) {
		makeClickSound();
		hideOverlays();
	}
	
	public void onTutorialPressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(this, TutorialActivity.class);
		startActivity(intent);
	}
	
	/* Timer */
	
	public void onCountdownFinish() {
		
	}
	
	public void onTimerTick() {
		if (mTimer != null) {
			mTimer.setText(getElapsedTimeAsString());
		}
		
		if (mCountdown != null) {
			mCountdown.setText(getRemainingTimeAsString());
		}
	}
	
	private Runnable mUpdateTimer = new Runnable() {

		public void run() {

			mElapsedTime = System.currentTimeMillis() - mTimerStart;
			
			if (mCountdownEnabled) {
				if (getRemainingTime() < 0) {
					onCountdownFinish();
					mCountdownEnabled = false;
				}
			}
			
			onTimerTick();
			
			mHandler.postAtTime(this, SystemClock.uptimeMillis() + 1000);
		}
	};
	
	public void startTimer() {
		mTimerStart = System.currentTimeMillis() - mElapsedTime;
		mHandler.removeCallbacks(mUpdateTimer);
		mHandler.postDelayed(mUpdateTimer, 50);
		if (Trio.LOCAL_LOGV)
			Log.i("Classic Game", "Timer started");
	}
	
	public void setCountdown(long time) {
		mCountdownStart = time;
		mCountdownEnabled = true;
	}
	
	public void pauseTimer() {
		mHandler.removeCallbacks(mUpdateTimer);
	}
	
	/* Game Triggers */
	
	final public void resetGame() {
		mIsGameFinished = false;
		onGameReset();
	}
	
	final public void finishGame() {
		mIsGameFinished = true;
		onGameFinished();
	}
	
	final public void resign() {
		mIsGameFinished = true;
		onGameResign();
	}
	
	final public boolean isGameFinished() {
		return mIsGameFinished;
	}
	
	public void onGameResign() {
		
	}
	
	abstract public void onGameFinished();
	abstract public void onGameReset();

	/* Utils */
	
	public void displayWhatIsWrong(CardList threeCards) {
		EnumSet<TrioStatus> status = Trio.getTrioStatus(threeCards);
		ArrayList<String> errors = new ArrayList<String>();

		if (status.contains(TrioStatus.WRONG_COLOR)) {
			errors.add(getString(R.string.tutorial_colour));
		}

		if (status.contains(TrioStatus.WRONG_SHAPE)) {
			errors.add(getString(R.string.tutorial_shape));
		}

		if (status.contains(TrioStatus.WRONG_FILL)) {
			errors.add(getString(R.string.tutorial_fill));
		}

		if (status.contains(TrioStatus.WRONG_NUMBER)) {
			errors.add(getString(R.string.tutorial_number));
		}

		Toast.makeText(
				this,
				getString(R.string.tutorial_wrong_message,
						TextUtils.join(", ", errors)), Toast.LENGTH_SHORT)
				.show();
	}
	
	public long getElapsedTime() {
		return mElapsedTime;		
	}
	
	public void setElapsedTime(long time) {
		mElapsedTime = time;
	}
	
	public String getElapsedTimeAsString() {
		return timeToString(mElapsedTime);
	}
	
	public long getRemainingTime() {
		return mCountdownStart - mElapsedTime;
	}
	
	public String getRemainingTimeAsString() {
		return timeToString(getRemainingTime() + REMAINING_EXTRA_SECOND);
	}
	
	private String timeToString(long value) {
		StringBuilder timeString = new StringBuilder();
		int seconds = (int) value / 1000;
		int minutes = seconds / 60;
		seconds %= 60;
		
		timeString.append(minutes);
		timeString.append(seconds < 10 ? ":0" : ":");
		timeString.append(seconds);
		
		return timeString.toString();
	}
}
