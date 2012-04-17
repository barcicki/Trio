package com.barcicki.trio;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.barcicki.trio.core.CardView;
import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.tutorial.TutorialActivity;
import com.openfeint.api.OpenFeint;
import com.openfeint.api.OpenFeintDelegate;
import com.openfeint.api.OpenFeintSettings;
import com.openfeint.api.ui.Dashboard;

public class HomeActivity extends Activity {
	
	private CardView mCard;
	private Trio mTrio = new Trio();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_tablets);

		mCard = (CardView) findViewById(R.id.gameCard);

		Map<String, Object> options = new HashMap<String, Object>();
		options.put(OpenFeintSettings.SettingCloudStorageCompressionStrategy,
				OpenFeintSettings.CloudStorageCompressionStrategyDefault);
		options.put(OpenFeintSettings.RequestedOrientation,
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		OpenFeintSettings settings = new OpenFeintSettings(
				TrioSettings.APP_NAME, TrioSettings.APP_KEY,
				TrioSettings.APP_SECRET, TrioSettings.APP_ID, options);

		OpenFeint.initialize(this, settings, new OpenFeintDelegate() {
		});

		applyAnimations();
		
	}


	public void onStartGame(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.gameClassic:
			intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
			startActivity(intent);
			break;

		case R.id.gameChallenge:
			intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
			startActivity(intent);
			break;

		case R.id.gameTutorial:
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
			startActivity(intent);
			break;

		}
	}
	
	public void onOpenFeintClicked(View v) {
		Dashboard.open();
	}

	private void applyAnimations() {
		
		final Animation flip = AnimationUtils.loadAnimation(this, R.anim.card_flip);
		final Animation reflip = AnimationUtils.loadAnimation(this, R.anim.card_reflip);
		final int duration = getResources().getInteger(R.integer.card_flip_slow_animation);
						
		flip.setDuration(duration);
		flip.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mCard.setCard(mTrio.getDeck().getRandomRange(1).get(0));
				mCard.startAnimation(reflip);
			}
		});
		
		reflip.setDuration(duration);
		reflip.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mCard.startAnimation(flip);
			}
		});
		
		mCard.setCard( mTrio.getDeck().getRandomRange(1).get(0) );
		mCard.startAnimation(flip);

	}
	
}