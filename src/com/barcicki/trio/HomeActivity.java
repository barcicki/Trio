package com.barcicki.trio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioActivity;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.tutorial.TutorialActivity;
import com.openfeint.api.ui.Dashboard;

public class HomeActivity extends TrioActivity {

	private CardView mCard;
	private ImageView mOpenFeint;
	private ImageView mTrioLogo;
	private Trio mTrio = new Trio();
	private Button mContinueButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home2);

		mCard = (CardView) findViewById(R.id.gameCard);
		mOpenFeint = (ImageView) findViewById(R.id.openFeintButton);
		mTrioLogo = (ImageView) findViewById(R.id.trioLogo);
		mContinueButton = (Button) findViewById(R.id.classicContinue);

		if (TrioSettings.readBooleanPreference(this, "saved_game", false)) {
			mContinueButton.setEnabled(true);
		} else {
			mContinueButton.setEnabled(false);
		}
		
		mOpenFeint.setVisibility(View.INVISIBLE);
		if (!TrioSettings.hasBeenAskedAboutOpenFeint(this)) {

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					TrioSettings.setBeenAskedAboutOpenFeint(
							getApplicationContext(), true);
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						TrioSettings.setUsesOpenFeint(getApplicationContext(),
								true);
						TrioSettings
								.initializeOpenFeint(getApplicationContext());
						mOpenFeint.setVisibility(View.VISIBLE);
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						TrioSettings.setUsesOpenFeint(getApplicationContext(),
								false);
						mOpenFeint.setVisibility(View.INVISIBLE);
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.openfeint_question)
					.setPositiveButton(android.R.string.yes,
							dialogClickListener)
					.setNegativeButton(android.R.string.no, dialogClickListener)
					.show();

		} else {
			if (TrioSettings.usesOpenFeint(this)) {
				TrioSettings.initializeOpenFeint(getApplicationContext());
				mOpenFeint.setVisibility(View.VISIBLE);
			} else {
				mOpenFeint.setVisibility(View.INVISIBLE);
			}
		}

		applyAnimations();

	}

	@Override
	protected void onResume() {
		
		if (TrioSettings.readBooleanPreference(this, "saved_game", false)) {
			mContinueButton.setEnabled(true);
		} else {
			mContinueButton.setEnabled(false);
		}
		
		if (TrioSettings.usesOpenFeint(this)) {
			// TrioSettings.initializeOpenFeint(getApplicationContext());
			mOpenFeint.setVisibility(View.VISIBLE);
		} else {
			mOpenFeint.setVisibility(View.INVISIBLE);
		}

		super.onResume();
	}

	public void onStartGame(View v) {
		makeClickSound();
		Intent intent;
		switch (v.getId()) {

		case R.id.gameClassic:
			setMusicContinue(true);
			intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
			startActivity(intent);
			break;

		case R.id.gameChallenge:
			setMusicContinue(true);
			intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
			startActivity(intent);
			break;

		case R.id.gameTutorial:
			setMusicContinue(true);
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
			startActivity(intent);
			break;

		}
	}

	public void onOpenFeintPressed(View v) {
		makeClickSound();
		TrioSettings.initializeOpenFeint(getApplicationContext());
		Dashboard.open();
	}

	public void onClassicContinuePressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
		startActivity(intent);
	}

	public void onClassicNewGamePressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		TrioSettings.writeBooleanPreference(this, "saved_game", false);
		Intent intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
		startActivity(intent);
	}

	public void onNewChallengePressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(HomeActivity.this,
				PracticeGameActivity.class);
		startActivity(intent);
	}

	public void onTutorialPressed(View v) {
		makeClickSound();
		setMusicContinue(true);
		Intent intent = new Intent(HomeActivity.this, TutorialActivity.class);
		startActivity(intent);
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
				TrioSettings.writeBooleanPreference(this, "play_music", true);
				playBackgroundMusic();
				item.setTitle(R.string.settings_mute);
				item.setIcon(android.R.drawable.ic_media_pause);
			} else {
				TrioSettings.writeBooleanPreference(this, "play_music", false);
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
}