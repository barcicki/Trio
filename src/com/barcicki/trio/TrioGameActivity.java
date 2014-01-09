package com.barcicki.trio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
	
	private static final String	IS_GAME_FINISHED_KEY	= "game_has_finished";
	private View mPauseOverlay;
	private View mHelpOverlay;
	private ViewPager mHelpViewPager;
	private CirclePageIndicator mHelpIndicator;
	private HelpAdapter mHelpAdapter;
	
	private TextView mTimer;
	private TextView mCountdown;
	
	private GameTime mTime = new GameTime(this);
	
	private ArrayList<Long> mTrios = new ArrayList<Long>();
	
	private boolean mIsGamePaused = true;
	private boolean mIsGameFinished = false;
	
	public final static String START_GAME_IMMEDIATELY = "START_GAME_IMMEDIATELY";
				
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(getContentView());
		
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
		
		setHelpFragments(getHelpFragments());
		
		initGame();
		
		hideOverlays();
		
		resetGame();
		
		if (!hasSeenHelp()) {
			pauseGameForHelp();
			setSeenHelp();
		} else if (shouldStartGame()) {
			startGame();
		} else {
			pauseGame();
		}
	}
	
	abstract public int getContentView();
	abstract public int[] getHelpFragments();
	
	public boolean shouldStartGame() {
		Bundle params = getIntent().getExtras();
		if (params != null) {
			return params.getBoolean(START_GAME_IMMEDIATELY, false);
		}
		return false;
	}
	
	abstract protected boolean hasSeenHelp();
	abstract protected void setSeenHelp();
	
	private void setHelpFragments(int[] resId) {
		if (mHelpOverlay != null) {
			 for (int id : resId) {
				 mHelpAdapter.addFragment(id);
			 }
		}
		mHelpAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		pauseGame();
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public void onBackPressed() {
		if (isHelpOverlayVisible()) {
			startGame();
			return;
		}
		
		if (isPauseOverlayVisible()) {
			startHomeActivity();
			finish();
		} else {
			pauseGame();
		}
	}
	
	@Override
	protected void onPause() {
		pauseGame();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		pauseGame();
		super.onStop();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		pauseGame();
		outState.putBoolean(IS_GAME_FINISHED_KEY, mIsGameFinished);
		super.onSaveInstanceState(storeGame(outState));
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		resetGame(savedInstanceState);
		
		if (savedInstanceState != null) {
			mIsGameFinished = savedInstanceState.getBoolean(IS_GAME_FINISHED_KEY, false);
		}
		
		pauseGame();
		super.onRestoreInstanceState(savedInstanceState);
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

	private void showPauseOverlay() {
		if (!isHelpOverlayVisible()) {
			if (!isPauseOverlayVisible()) {
				mPauseOverlay.setVisibility(View.VISIBLE);
			}
			
			if (isGameFinished()) {
				onEndingOverlayShow();
			} else {
				onPauseOverlayShow();
			}
		}
	}
	
	private void hidePauseOverlay() {
		if (isPauseOverlayVisible()) {
			mPauseOverlay.setVisibility(View.INVISIBLE);
		}
		
		onPauseOverlayHide();
	}
	
	private void showHelpOverlay() {
		if (!isHelpOverlayVisible()) {
			mHelpOverlay.setVisibility(View.VISIBLE);
		}

		onHelpOverlayShow();
	}
	
	private void hideHelpOverlay() {
		if (isHelpOverlayVisible()) {
			mHelpOverlay.setVisibility(View.INVISIBLE);
		}
		
		onHelpOverlayHide();
	}
	
	/* Game controls */
	
	/**
	 * Initialization of game - creating view references etc.
	 */
	abstract public void initGame();
	
	/**
	 * Create game from saved state if possible, otherwise creates new game
	 * @param game
	 */
	abstract public void restoreGame(Bundle game);
	
	/**
	 * Saves game into a bundle
	 * @return
	 */
	abstract public Bundle storeGame(Bundle stateToModify);
	
	final public void resetGame(Bundle savedInstanceState) {
		resetTimer();
		mIsGameFinished = false;
		mIsGamePaused = true;
		
		restoreGame(savedInstanceState);
		
		updateGame();
		
		onGameReset();
	}
	
	final public void resetGame() {
		resetGame(null);
	}
	
	final public void startGame() {
		startTimer();
		mIsGamePaused = false;
		
		onGameStarted();

		updateGame();
		hideOverlays();		
	}
	
	final public void pauseGame() {
		pauseTimer();
		mIsGamePaused = true;
		
		showPauseOverlay();
		
		onGamePaused();
		
		submitFoundTrios();
	}
	
	final public void pauseGameForHelp() {
		pauseTimer();
		mIsGamePaused = true;
		
		showHelpOverlay();
		
		onGamePaused();
	}
	
	final public void endGame(boolean won) {
		pauseTimer();
		mIsGameFinished = true;
		mIsGamePaused = true;
		
		onGameEnded(won);
		
		showPauseOverlay();
		
		submitFoundTrios();
	}
	
	final public void updateGame() {
		if (mTimer != null) {
			mTimer.setText(getElapsedTimeAsString(false));
		}
		
		if (mCountdown != null) {
			mCountdown.setText(getRemainingTimeAsString(false));
		}
		
		onGameUpdate();
	}
	
	final public boolean isGameFinished() {
		return mIsGameFinished;
	}
	
	final public boolean isGamePaused() {
		return mIsGamePaused;
	}
	
	/* Overlay triggers */
	
	public void onPausePressed(View v) {
		makeClickSound();
		pauseGame();
	}
	
	public void onHelpPressed(View v) {
		makeClickSound();
		pauseGameForHelp();
	}
	
	/* Default help overlay actions */
	
	public void onContinuePressed(View v) {
		makeClickSound();
		startGame();
	}
	
	public void onTutorialPressed(View v) {
		makeClickSound();
		Intent intent = new Intent(this, TutorialActivity.class);
		startActivity(intent);
	}
	
	/* Timer */
	
	public void onCountdownFinish() {
		
	}
	
	public void onTimerTick() {
		updateGame();
	}
	
	private void resetTimer() {
		mTime.setElapsedTime(0L);
		mTime.pause();
	}
	
	private void startTimer() {
		mTime.start(mTime.getElapsedTime());
	}
	
	public void setCountdown(Long time) {
		mTime.setCountdown(time);
	}
	
	private void pauseTimer() {
		mTime.pause();
	}
	
	public void addTime(long time) {
		mTime.addTime(time);
	}
	
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
		
		if (triosFound > 0 && isSignedIn()) {
			
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
	
	/* Events */
	public void onPauseOverlayShow() {}
	public void onPauseOverlayHide() {}
	public void onHelpOverlayShow() {}
	public void onHelpOverlayHide() {}
	public void onEndingOverlayShow() {}

	public void onGameReset() {}
	public void onGameStarted() {}
	public void onGameEnded(boolean won) {}
	public void onGameUpdate() {}
	public void onGamePaused() {}
	
}
