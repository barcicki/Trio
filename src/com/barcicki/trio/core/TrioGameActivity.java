package com.barcicki.trio.core;

import java.util.ArrayList;
import java.util.EnumSet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.R;
import com.barcicki.trio.TutorialActivity;
import com.barcicki.trio.core.Trio.TrioStatus;
import com.viewpagerindicator.CirclePageIndicator;

public class TrioGameActivity extends TrioActivity {

	private View mPauseOverlay;
	private View mHelpOverlay;
	private ViewPager mHelpViewPager;
	private CirclePageIndicator mHelpIndicator;
	private HelpAdapter mHelpAdapter;
	
	private TextView mTimer;
	private TextView mCountdown;
	private Handler mHandler = new Handler();
	private long mTimerStart = 0L;
	
	protected String gElapsedTimeString = "00:00";
	protected String gRemainingTimeString = "00:00";
	protected long gElapsedTime = 0L;
	protected long gRemainingTime = 60000L;
	protected boolean gGameEnded = false;
	protected boolean gTimerFinishCalled = false;
	
				
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
			mHelpIndicator.setRadius(5 * density);
		}
		
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
	
	public void onTimerFinish() {
		
	}
	
	private Runnable mUpdateTimer = new Runnable() {

		public void run() {

			final long start = mTimerStart;
			long millis = System.currentTimeMillis() - start;
			 

			gElapsedTime = millis;
			int seconds = (int) millis / 1000;
			int minutes = seconds / 60;
			seconds %= 60;

			if (seconds < 10) {
				gElapsedTimeString = minutes + ":0" + seconds;
			} else {
				gElapsedTimeString = minutes + ":" + seconds;
			}
			
			long remainingTime = gRemainingTime - gElapsedTime;
			
			if (remainingTime < 0) {
				if (!gTimerFinishCalled) {
					onTimerFinish();
					gTimerFinishCalled = true;
				}
				
				if (mCountdown!= null) {
					mCountdown.setText("00:00");
				}
			} else {
				seconds = (int) remainingTime / 1000;
				minutes = seconds / 60;
				seconds %= 60;
	
				if (seconds < 10) {
					gRemainingTimeString = minutes + ":0" + seconds;
				} else {
					gRemainingTimeString = minutes + ":" + seconds;
				}
	
				if (mCountdown!= null) {
					mCountdown.setText(gRemainingTimeString);
				}
			}
			mHandler.postAtTime(this, SystemClock.uptimeMillis() + 1000);
		}
	};
	
	public void startTimer() {
		mTimerStart = System.currentTimeMillis() - gElapsedTime;
		mHandler.removeCallbacks(mUpdateTimer);
		mHandler.postDelayed(mUpdateTimer, 50);
		if (Trio.LOCAL_LOGV)
			Log.i("Classic Game", "Timer started");
	}
	
	public void pauseTimer() {
		mHandler.removeCallbacks(mUpdateTimer);
	}

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
}
