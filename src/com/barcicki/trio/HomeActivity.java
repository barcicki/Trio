package com.barcicki.trio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
	private MenuDescription mDescription;
	private boolean mIsDescriptionVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		mCard = (CardView) findViewById(R.id.gameCard);
		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);
		mDescription = (MenuDescription) findViewById(R.id.menuDescriptionView);
		mDescription.setMenuDescriptionListener(this);
		
		Utils.setAlpha(mDescription, 0f);
		
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
//		mContinueButton.setEnabled(TrioSettings.isSavedGamePresent());
		super.onResume();
	}

	private void applyAnimations() {

		Animation logoAnimation = AnimationUtils.loadAnimation(this,
				R.anim.logo_appear);
		mTrioLogo.startAnimation(logoAnimation);

		final int duration = getResources().getInteger(
				R.integer.card_flip_slow_animation);
		final int delay = getResources().getInteger(R.integer.logo_anim_duration);
//		mCard.setCard(null);
//		mCard.setImageResource(R.drawable.square_question);
		mCard.setSwitchAnimationLsitener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				mCard.animateSwitchCard(mTrio.getDeck().getRandomRange(1)
					.get(0), duration);
			}

		});
		mCard.animateSwitchCard(mTrio.getDeck().getRandomRange(1).get(0),
				duration, delay);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		MenuItem item = menu.findItem(R.id.mute);
		if (getSoundManager().isBackgroundPlaying()) {
			item.setTitle(R.string.settings_mute);
			item.setIcon(android.R.drawable.ic_media_pause);
		} else {
			item.setTitle(R.string.settings_unmute);
			item.setIcon(android.R.drawable.ic_media_play);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.mute:
			if (!getSoundManager().isBackgroundPlaying()) {
				TrioSettings.setMusicEnabled(true);
				playBackgroundMusic();
				item.setTitle(R.string.settings_mute);
				item.setIcon(android.R.drawable.ic_media_pause);
			} else {
				TrioSettings.setMusicEnabled(false);
				getSoundManager().pauseBackground();
				item.setTitle(R.string.settings_unmute);
				item.setIcon(android.R.drawable.ic_media_play);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		getSoundManager().release();
		super.onDestroy();
	}

	public void onClick(View view) {
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

	private void switchToDescription(final MenuDescriptionType type) {
		final long duration = 500;
		
		if (mIsDescriptionVisible) {
		
			if (!type.equals(mDescription.getMenuDescription())) {
			
				AnimationListener waitForDisappearance = new AnimationListener() {
					public void onAnimationStart(Animation animation) {}
					public void onAnimationRepeat(Animation animation) {}
					public void onAnimationEnd(Animation animation) {
						mDescription.setMenuDescription(type);
						mDescription.startAnimation(Utils.generateAlphaAnimation(0.3f, 1f, duration / 2));
					}
				};
				
				Animation anim = Utils.generateAlphaAnimation(1f, 0.3f, duration / 2); 
				
				anim.setAnimationListener(waitForDisappearance);
				mDescription.startAnimation(anim); 
				
			}
		
		} else {
			mCard.startAnimation(Utils.generateAlphaAnimation(1f, 0f, duration));
			mDescription.setMenuDescription(type);
			mDescription.startAnimation(Utils.generateAlphaAnimation(0f, 1f, duration));
		}
		
		mIsDescriptionVisible = true;
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