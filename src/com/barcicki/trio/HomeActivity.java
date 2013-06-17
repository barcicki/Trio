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
import android.widget.Button;
import android.widget.ImageView;

import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TrioSettings;

public class HomeActivity extends TrioActivity implements OnClickListener {

	
	private CardView mCard;
	private ImageView mTrioLogo;
	private Trio mTrio = new Trio();
	private Button mContinueButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		mCard = (CardView) findViewById(R.id.gameCard);
		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);
		mContinueButton = (Button) findViewById(R.id.buttonGameClassicContinue);
		
		for (View view : new View[] { 
				findViewById(R.id.buttonGameChallengeNew),
				mContinueButton,
				findViewById(R.id.buttonGameClassicNew),
				findViewById(R.id.buttonTutorial),
//				findViewById(R.id.buttonMusicSwitch)
		}) {
			view.setOnClickListener(this);
		}
		
		mContinueButton.setEnabled(TrioSettings.isSavedGamePresent());
		applyAnimations();
	}

	@Override
	protected void onResume() {
		mContinueButton.setEnabled(TrioSettings.isSavedGamePresent());
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
		Intent intent = null;
		makeClickSound();
		
		switch (view.getId()) {
			case R.id.buttonGameClassicNew:
				TrioSettings.setSavedGamePresence(false);
				intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
				break;
			case R.id.buttonGameClassicContinue:
				intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
				break;
			case R.id.buttonGameChallengeNew:
				intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
				break;
			case R.id.buttonTutorial:
				intent = new Intent(HomeActivity.this, TutorialActivity.class);
				break;
		}
		
		if (intent != null) {
			setMusicContinue(true);
			startActivity(intent);
		}
	}
}