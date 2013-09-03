package com.barcicki.trio;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.barcicki.trio.core.MenuDescription;
import com.barcicki.trio.core.MenuDescription.MenuDescriptionListener;
import com.barcicki.trio.core.MenuDescription.MenuDescriptionType;
import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.core.Utils;
import com.barcicki.trio.views.MenuDescriptionButton;
import com.barcicki.trio.views.MenuDescriptionPlaceholder;
import com.barcicki.trio.views.MenuDescriptionPlaceholder.MenuDescriptionGestureListener;

public class HomeActivity extends TrioActivity implements OnClickListener,
		MenuDescriptionListener, MenuDescriptionGestureListener {

	private ImageView mTrioLogo;
	private HashMap<MenuDescriptionType, MenuDescription> mMenu = new HashMap<MenuDescription.MenuDescriptionType, MenuDescription>();
	private ArrayList<MenuDescriptionButton> mButtons = new ArrayList<MenuDescriptionButton>();
	private MenuDescription mCurrentMenu = null;

	private GestureDetector mGestureDetector;

	private boolean mUIBlocked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);

		mMenu.put(MenuDescriptionType.CLASSIC,
				(MenuDescription) findViewById(R.id.menuClassic));
		mMenu.put(MenuDescriptionType.HELP,
				(MenuDescription) findViewById(R.id.menuHelp));
		mMenu.put(MenuDescriptionType.SPEED,
				(MenuDescription) findViewById(R.id.menuSpeed));
		mMenu.put(MenuDescriptionType.TRIPLE,
				(MenuDescription) findViewById(R.id.menuTriple));

		for (MenuDescription menu : mMenu.values()) {
			menu.setMenuDescriptionListener(this);
		}

		mCurrentMenu = mMenu
				.get(TrioSettings.hasPlayed() ? MenuDescriptionType.CLASSIC
						: MenuDescriptionType.HELP);
		mCurrentMenu.setVisibility(View.VISIBLE);
		hideMenus(mCurrentMenu.getMenuType());

		for (View view : new View[] { findViewById(R.id.showClassic),
				findViewById(R.id.showHelp), findViewById(R.id.showSpeed),
				findViewById(R.id.showTriple), }) {
			view.setOnClickListener(this);
			mButtons.add((MenuDescriptionButton) view);
		}

		updateActiveButtons(mCurrentMenu.getMenuType());

		applyAnimations();

		((MenuDescriptionPlaceholder) findViewById(R.id.menuSwitcher))
				.setGestureListener(this);

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
		if (!mUIBlocked) {
			makeClickSound();

			MenuDescriptionType type = ((MenuDescriptionButton) view).getType();

			updateActiveButtons(type);
			switchToDescription(type);
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
	
	private void switchToDescription(final MenuDescriptionType type) {
		switchToDescription(type, false);
	}

	private void switchToDescription(final MenuDescriptionType type, boolean isSlidingDown) {
		final long duration = 500;
		Animation slideOut;
		Animation slideIn;

		if (isSlidingDown) {
			slideOut = Utils.generateSlideToBottomAnimation(duration);
			slideIn = Utils.generateSlideFromTopAnimation(duration);
		} else {
			slideOut = Utils.generateSlideToTopAnimation(duration);
			slideIn = Utils.generateSlideFromBottomAnimation(duration);
		}

		if (!type.equals(mCurrentMenu.getMenuType())) {
			final MenuDescription next = mMenu.get(type);
			final MenuDescription previous = mCurrentMenu;

			previous.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);

			slideOut.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mCurrentMenu = next;
					hideMenus(type);
					mUIBlocked = false;
				}
			});

			previous.startAnimation(slideOut);
			next.startAnimation(slideIn);

			mUIBlocked = true;
		}
	}

	public void onLeftButtonPressed(MenuDescriptionType type, View v) {
		makeClickSound();

		if (type.equals(MenuDescriptionType.CLASSIC)) {
			setMusicContinue(true);
			startActivity(new Intent(HomeActivity.this,
					ClassicGameActivity.class));
		}
	}

	public void onRightButtonPressed(MenuDescriptionType type, View v) {
		Intent intent = null;
		makeClickSound();

		switch (type) {
		case CLASSIC:
			TrioSettings.setSavedGamePresence(false);
			intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
			break;
		case HELP:
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
			break;
		case SPEED:
			intent = new Intent(HomeActivity.this, SpeedGameActivity.class);
			break;
		case TRIPLE:
			intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
			break;
		}

		if (intent != null) {
			setMusicContinue(true);
			startActivity(intent);
		}

	}

	public void onUp() {
		if (!mUIBlocked) {
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
			default:
				newType = MenuDescriptionType.CLASSIC;
			}

			updateActiveButtons(newType);
			switchToDescription(newType, true);
		}
	}

	public void onDown() {
		if (!mUIBlocked) {
			MenuDescriptionType newType;
			switch (mCurrentMenu.getMenuType()) {
			case CLASSIC:
				newType = MenuDescriptionType.HELP;
				break;
			case TRIPLE:
				newType = MenuDescriptionType.CLASSIC;
				break;
			case SPEED:
				newType = MenuDescriptionType.TRIPLE;
				break;
			default:
				newType = MenuDescriptionType.SPEED;
			}

			updateActiveButtons(newType);
			switchToDescription(newType);
		}
	}

}