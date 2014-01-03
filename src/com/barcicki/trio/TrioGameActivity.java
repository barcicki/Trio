package com.barcicki.trio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.CardList;
import com.barcicki.trio.core.GameTime;
import com.barcicki.trio.core.GameTime.GameTimeListener;
import com.barcicki.trio.core.HelpAdapter;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.Trio.TrioStatus;
import com.barcicki.trio.core.TrioSettings;
import com.google.android.gms.games.GamesClient;
import com.viewpagerindicator.CirclePageIndicator;

abstract public class TrioGameActivity extends TrioActivity implements GameTimeListener {
	
	private View mPauseOverlay;
	private View mHelpOverlay;
	private ViewPager mHelpViewPager;
	private CirclePageIndicator mHelpIndicator;
	private HelpAdapter mHelpAdapter;
	
	private TextView mTimer;
	private TextView mCountdown;
	
	private GameTime mTime = new GameTime(this);
	
	private ArrayList<Long> mTrios = new ArrayList<Long>();
	
	private boolean mIsGameFinished = false;
				
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
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
			submitFoundTrios();
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
		continueTime();
	}
	
	protected void onHideHelpOverlay() {
		continueTime();
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
			mTimer.setText(getElapsedTimeAsString(false));
		}
		
		if (mCountdown != null) {
			mCountdown.setText(getRemainingTimeAsString(false));
		}
	}
	
	public void startTimer() {
		mTime.start(mTime.getElapsedTime());
	}
	
	public void continueTime() {
		mTime.unpause();
	}
	
	public void setCountdown(Long time) {
		mTime.setCountdown(time);
	}
	
	public void pauseTimer() {
		mTime.pause();
	}
	
	public void addTime(long time) {
		mTime.addTime(time);
	}
	
	/* Game Triggers */
	
	final public void resetGame() {
		mIsGameFinished = false;
		onGameReset();
	}
	
	final public void finishGame() {
		mIsGameFinished = true;
		onGameFinished();
		submitFoundTrios();
	}
	
	final public void resign() {
		mIsGameFinished = true;
		onGameResign();
		submitFoundTrios();
	}
	
	final public boolean isGameFinished() {
		return mIsGameFinished;
	}
	
	public void onGameResign() {
		
	}
	
	abstract public void onGameFinished();
	abstract public void onGameReset();

	/* Utils */
	
	public void inform(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 10);
		toast.show();
	}
	
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

		inform(getString(R.string.tutorial_wrong_message, TextUtils.join(", ", errors)));
	}
	
	public long getElapsedTime() {
		return mTime.getElapsedTime();		
	}
	
	public void setElapsedTime(long time) {
		mTime.setElapsedTime(time);
	}
	
	public String getElapsedTimeAsString(boolean withMiliseconds) {
		return mTime.getElapsedTimeAsString(withMiliseconds);
	}
	
	public long getRemainingTime() {
		return mTime.getRemainingTime();
	}
	
	public String getRemainingTimeAsString(boolean withMiliseconds) {
		return mTime.getRemainingTimeAsString(withMiliseconds);
	}
	
	/**
	 * Play Game Events
	 */
	public void saveFoundTrio() {
		mTrios.add(mTime.checkpoint());
	}
	
	public void submitFoundTrios() {
		int triosFound = mTrios.size();
		
		if (isSignedIn() && triosFound > 0) {
			
			GamesClient client = getGamesClient();
			
			Collections.sort(mTrios);
			long bestTime = mTrios.get(0);
			
			client.incrementAchievement(getString(R.string.achievement_novice), triosFound);
			client.incrementAchievement(getString(R.string.achievement_amateur), triosFound);
			client.incrementAchievement(getString(R.string.achievement_professional), triosFound);
			client.incrementAchievement(getString(R.string.achievement_expert), triosFound);
			
			client.submitScore(getString(R.string.leaderboard_fastest_trio), bestTime);
			
			if (bestTime < 1000L) {
				client.unlockAchievement(getString(R.string.achievement_faster_then_light));				
			} 
			
			if (bestTime < 3000L) {
				client.unlockAchievement(getString(R.string.achievement_faster_than_lightning));
			}
			
			if (bestTime < 5000L) {
				client.unlockAchievement(getString(R.string.achievement_faster_than_rocket));
			}
		}
		
		mTrios.clear();
	}
	
}
