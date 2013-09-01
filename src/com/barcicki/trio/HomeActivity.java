package com.barcicki.trio;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.MenuDescription;
import com.barcicki.trio.core.MenuDescription.MenuDescriptionListener;
import com.barcicki.trio.core.MenuDescription.MenuDescriptionType;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.core.Utils;

public class HomeActivity extends TrioActivity implements OnClickListener, MenuDescriptionListener {
	
	private CardView mCard;
	private ImageView mTrioLogo;
	private Trio mTrio = new Trio();
	private HashMap<MenuDescriptionType, MenuDescription> mMenu = new HashMap<MenuDescription.MenuDescriptionType, MenuDescription>();
	private MenuDescription mCurrentMenu = null;

	private boolean mUIBlocked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
//		mCard = (CardView) findViewById(R.id.gameCard);
		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);
		
		mMenu.put(MenuDescriptionType.CLASSIC, (MenuDescription) findViewById(R.id.menuClassic));
		mMenu.put(MenuDescriptionType.HELP, (MenuDescription) findViewById(R.id.menuHelp));
		mMenu.put(MenuDescriptionType.SPEED, (MenuDescription) findViewById(R.id.menuSpeed));
		mMenu.put(MenuDescriptionType.TRIPLE, (MenuDescription) findViewById(R.id.menuTriple));
		
		for (MenuDescription menu : mMenu.values()) {
			menu.setMenuDescriptionListener(this);
		}
		
		mCurrentMenu = mMenu.get(TrioSettings.hasPlayed() ? MenuDescriptionType.CLASSIC : MenuDescriptionType.HELP);
		mCurrentMenu.setVisibility(View.VISIBLE);
		hideMenus(mCurrentMenu.getMenuType());
		
		for (View view : new View[] { 
				findViewById(R.id.showClassic),
				findViewById(R.id.showHelp),
				findViewById(R.id.showSpeed),
				findViewById(R.id.showTriple),
		}) {
			view.setOnClickListener(this);
		}
		
		applyAnimations();
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
			MenuDescriptionType type = MenuDescriptionType.HELP;
			makeClickSound();
			
			switch (view.getId()) {
				case R.id.showClassic:
					type = MenuDescriptionType.CLASSIC;
					break;
				case R.id.showHelp:
					type = MenuDescriptionType.HELP;
					break;
				case R.id.showSpeed:
					type = MenuDescriptionType.SPEED;
					break;
				case R.id.showTriple:
					type = MenuDescriptionType.TRIPLE;
					break;
			}
	
			switchToDescription(type);
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
		final long duration = 500;
		
		Animation slideOut = Utils.generateSlideDownAnimation(duration);
		Animation slideIn = Utils.generateSlideInAnimation(duration);
		
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
					mUIBlocked = false;
				}
			});
			
			previous.startAnimation(slideOut);
			next.startAnimation(slideIn);
			
			mUIBlocked = true;
		}
		
//		if (mIsDescriptionVisible) {
//			if (!type.equals(mDescription.getMenuDescription())) {
//				final MenuDescription previous = mDescription;
//				final MenuDescription next = new MenuDescription(this);
//				final RelativeLayout parent = (RelativeLayout) previous.getParent();
//				next.setMenuDescription(type);
//				next.setMenuDescriptionListener(this);
//				parent.addView(next, previous.getLayoutParams());
//				
//				AnimationListener listener = new AnimationListener() {
//					public void onAnimationStart(Animation animation) {}			
//					public void onAnimationRepeat(Animation animation) {}
//					public void onAnimationEnd(Animation animation) {
//						mDescription = next;
//						new Handler().post(new Runnable() {
//							public void run() {
//								parent.removeView(previous);
//							}
//						});
//					}
//				};
//				slideOut.setAnimationListener(listener);
//				
//				previous.startAnimation(slideOut);
//				next.startAnimation(slideIn);
//			}
//		} else {
//			mCard.startAnimation(slideOut);
//			mDescription.setMenuDescription(type);
//			mDescription.startAnimation(slideIn);
//		}
//		
//		mIsDescriptionVisible = true;
	}
	
	public void onLeftButtonPressed(MenuDescriptionType type, View v) {
		makeClickSound();
		
		if (type.equals(MenuDescriptionType.CLASSIC)) {
			setMusicContinue(true);
			startActivity(new Intent(HomeActivity.this, ClassicGameActivity.class));
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
	
}