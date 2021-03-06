package com.barcicki.trio;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.core.Utils;
import com.barcicki.trio.views.MenuDescription;
import com.barcicki.trio.views.MenuDescription.MenuDescriptionListener;
import com.barcicki.trio.views.MenuDescription.MenuDescriptionType;
import com.barcicki.trio.views.MenuDescriptionButton;
import com.barcicki.trio.views.MenuDescriptionPlaceholder;
import com.barcicki.trio.views.MenuDescriptionPlaceholder.MenuDescriptionGestureListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.games.Games;

public class HomeActivity extends TrioActivity implements OnClickListener,
		MenuDescriptionListener, MenuDescriptionGestureListener {

	private ImageView mTrioLogo;
	private HashMap<MenuDescriptionType, MenuDescription> mMenu = new HashMap<MenuDescription.MenuDescriptionType, MenuDescription>();
	private ArrayList<MenuDescriptionButton> mButtons = new ArrayList<MenuDescriptionButton>();
	private MenuDescription mCurrentMenu = null;
	private MenuDescription mPlayGamesMenu;

	private boolean mUiBlocked = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);
		
		mPlayGamesMenu = (MenuDescription) findViewById(R.id.menuPlayGames);

		mMenu.put(MenuDescriptionType.CLASSIC,
				(MenuDescription) findViewById(R.id.menuClassic));
		mMenu.put(MenuDescriptionType.HELP,
				(MenuDescription) findViewById(R.id.menuHelp));
		mMenu.put(MenuDescriptionType.SPEED,
				(MenuDescription) findViewById(R.id.menuSpeed));
		mMenu.put(MenuDescriptionType.TRIPLE,
				(MenuDescription) findViewById(R.id.menuTriple));
		mMenu.put(MenuDescriptionType.PLAY_GAMES, mPlayGamesMenu);
		

		for (MenuDescription menu : mMenu.values()) {
			menu.setMenuDescriptionListener(this);
		}

		mCurrentMenu = mMenu
				.get(TrioSettings.hasPlayed() ? MenuDescriptionType.CLASSIC
						: MenuDescriptionType.HELP);
		mCurrentMenu.setVisibility(View.VISIBLE);
		hideMenus(mCurrentMenu.getMenuType());

		for (View view : new View[] { 
				findViewById(R.id.showClassic),
				findViewById(R.id.showHelp), 
				findViewById(R.id.showSpeed),
				findViewById(R.id.showTriple),
				findViewById(R.id.showPlayGames),
				findViewById(R.id.showSettings)
			}) {
			view.setOnClickListener(this);
			mButtons.add((MenuDescriptionButton) view);
		}
		
		updateActiveButtons(mCurrentMenu.getMenuType());

		applyAnimations();
		
		((MenuDescriptionPlaceholder) findViewById(R.id.menuSwitcher))
				.setGestureListener(this);
		
		if (Trio.LOCAL_LOGD) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			Log.d("DENSITY", metrics.density + " " + metrics.densityDpi);
		}
		
	}

	@Override
	protected void onResume() {
		mCurrentMenu.update();
		super.onResume();
	}

	private void applyAnimations() {

		Animation logoAnimation = AnimationUtils.loadAnimation(this,
				R.anim.logo_appear);
		mTrioLogo.startAnimation(logoAnimation);
	}

	@Override
	protected void onDestroy() {
		getSoundManager().release();
		super.onDestroy();
	}

	public void onClick(View view) {
		if (!mUiBlocked) {
			makeClickSound();

			MenuDescriptionType type = ((MenuDescriptionButton) view).getType();
			
			if (type.equals(MenuDescriptionType.SETTINGS)) {
				openSettingsActvity();
				
			} else {
				updateActiveButtons(type);
				switchToDescription(type, 0);
			}
		}
	}

	private void updateActiveButtons(MenuDescriptionType exception) {
		for (MenuDescriptionButton button : mButtons) {
			button.setActive(button.getType() == exception);
		}
	}

	private void hideMenus(MenuDescriptionType exception) {
		for (MenuDescription menu : mMenu.values()) {
			if (menu.getMenuType() != exception) {
				menu.setVisibility(View.GONE);
			}
		}
	}
	
	private void switchToDescription(final MenuDescriptionType type, int diff) {
		final long duration = Math.max(700 - Math.abs(diff), 400);
		Animation slideOut;
		Animation slideIn;

		if (diff > 0) {
			slideOut = Utils.generateSlideToBottomAnimation(diff, duration);
			slideIn = Utils.generateSlideFromTopAnimation(diff, duration);
		} else {
			slideOut = Utils.generateSlideToTopAnimation(diff, duration);
			slideIn = Utils.generateSlideFromBottomAnimation(diff, duration);
		}

		if (!type.equals(mCurrentMenu.getMenuType())) {
			final MenuDescription next = mMenu.get(type);
			final MenuDescription previous = mCurrentMenu;

			previous.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);

			slideOut.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					mCurrentMenu = next;
					hideMenus(type);
					mUiBlocked = false;
					findViewById(android.R.id.content).invalidate();
				}
			});

			previous.clearAnimation();
			previous.startAnimation(slideOut);
			next.clearAnimation();
			next.startAnimation(slideIn);

			mUiBlocked = true;
		}
	}

	public void onLeftButtonPressed(MenuDescriptionType type, View v) {
		makeClickSound();

		if (type.equals(MenuDescriptionType.CLASSIC)) {
			Intent intent = new Intent(HomeActivity.this,ClassicGameActivity.class);
			intent.putExtra(TrioGameActivity.START_GAME_IMMEDIATELY, true);
			startActivity(intent);
		} else if (type.equals(MenuDescriptionType.PLAY_GAMES)) {
			runPlayGamesIntent(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()));
		}
	}

	public void onRightButtonPressed(MenuDescriptionType type, View v) {
		Intent intent = null;
		makeClickSound();

		switch (type) {
		case CLASSIC:
			TrioSettings.setSavedGamePresence(false);
			intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
			intent.putExtra(TrioGameActivity.START_GAME_IMMEDIATELY, true);
			break;
		case HELP:
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
			break;
		case SPEED:
			intent = new Intent(HomeActivity.this, SpeedGameActivity.class);
			intent.putExtra(TrioGameActivity.START_GAME_IMMEDIATELY, true);
			break;
		case TRIPLE:
			intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
			intent.putExtra(TrioGameActivity.START_GAME_IMMEDIATELY, true);
			break;
		case PLAY_GAMES:
			if (isSignedIn()) {
				runPlayGamesIntent(Games.Achievements.getAchievementsIntent(getApiClient()));
			} else {
				beginUserInitiatedSignIn();
			}
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}

	}
	
	private void runPlayGamesIntent(final Intent intent) {
		if (getApiClient().isConnected()) {
			startActivityForResult(intent, 1);
		} else {
			getApiClient().registerConnectionCallbacks(new ConnectionCallbacks() {
				public void onConnectionSuspended(int cause) {}
				public void onConnected(Bundle connectionHint) {
					getApiClient().unregisterConnectionCallbacks(this);
					startActivityForResult(intent, 1);
				}
			});
		}
	}

	public void onUp(int diff) {
		if (!mUiBlocked) {
			MenuDescriptionType newType;
			switch (mCurrentMenu.getMenuType()) {
			case CLASSIC:
				newType = MenuDescriptionType.PLAY_GAMES;
				break;
			case TRIPLE:
				newType = MenuDescriptionType.CLASSIC;
				break;
			case SPEED:
				newType = MenuDescriptionType.TRIPLE;
				break;
			case HELP:
				newType = MenuDescriptionType.SPEED;
				break;
			case PLAY_GAMES:
				newType = MenuDescriptionType.HELP;
				break;
			default:
				newType = MenuDescriptionType.CLASSIC;
			}

			updateActiveButtons(newType);
			switchToDescription(newType, diff);
		}
	}

	public void onDown(int diff) {
		if (!mUiBlocked) {
			MenuDescriptionType newType;
			switch (mCurrentMenu.getMenuType()) {
			case CLASSIC:
				newType = MenuDescriptionType.TRIPLE;
				break;
			case TRIPLE:
				newType = MenuDescriptionType.SPEED;
				break;
			case SPEED:
				newType = MenuDescriptionType.HELP;
				break;
			case HELP:
				newType = MenuDescriptionType.PLAY_GAMES;
				break;
			default:
				newType = MenuDescriptionType.CLASSIC;
			}

			updateActiveButtons(newType);
			switchToDescription(newType, diff);
		}
	}
	
	public void onMoving(int diff, boolean isMoving) {
		if (!mUiBlocked) {
			TranslateAnimation movement = new TranslateAnimation(0, 0, diff, diff);
			movement.setFillAfter(true);
			movement.setFillBefore(true);
			movement.setDuration(0);
			mCurrentMenu.clearAnimation();
			mCurrentMenu.startAnimation(movement);
		}
	}
	
	public void onStoppedMoving(int diff) {
		if (!mUiBlocked) {
			TranslateAnimation movement = new TranslateAnimation(0, 0, diff, 0);
			movement.setFillBefore(true);
			movement.setDuration(200);
			mCurrentMenu.clearAnimation();
			mCurrentMenu.startAnimation(movement);
		}
	}
	
	private void updatePlayGamesStatus() {
		mPlayGamesMenu.update();
	}
	
	public boolean isSignedIn() {
		return super.isSignedIn();
	}
	
	@Override
	public void onSignInSucceeded() {
		super.onSignInSucceeded();
		updatePlayGamesStatus();
	}
	
	@Override
	public void onSignInFailed() {
		super.onSignInFailed();
		updatePlayGamesStatus();
	}
	
	@Override
	protected void signOut() {
		super.signOut();
		updatePlayGamesStatus();
	}
}