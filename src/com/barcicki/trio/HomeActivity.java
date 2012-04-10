package com.barcicki.trio;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.barcicki.trio.core.Trio;
import com.barcicki.trio.core.TrioSettings;
import com.barcicki.trio.tutorial.TutorialActivity;
import com.openfeint.api.OpenFeint;
import com.openfeint.api.OpenFeintDelegate;
import com.openfeint.api.OpenFeintSettings;
import com.openfeint.api.ui.Dashboard;

public class HomeActivity extends Activity {

	private RelativeLayout mNavigationLayout;
	private ImageView mTrioLogo;
	private ImageView mClouds;
	private Animation mSlideLeft;
	private Animation mSlideRight;
	private Animation mReslideLeft;
	private Animation mReslideRight;
	private ImageSwitcher mGameHeader;
	private TextSwitcher mGameDescription;
	private static int NUMBER_OF_GAME_TYPES = 3;
	private static int[] GAME_TYPES_HEADERS = { 
			R.drawable.tutorial,
			R.drawable.classic, 
			R.drawable.challenge };
	private static int[] GAME_TYPES_DESCRIPTION = {
			R.string.game_tutorial_desc, 
			R.string.game_classic_desc,
			R.string.game_challenge_desc };
	private int gameType;
	private GestureDetector mSwipeDetector;
	private OnTouchListener mSwipeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_v2);

		mNavigationLayout = (RelativeLayout) findViewById(R.id.layoutNavigation);
		mTrioLogo = (ImageView) findViewById(R.id.layoutTrio);
		mClouds = (ImageView) findViewById(R.id.layoutClouds);
		mGameHeader = (ImageSwitcher) findViewById(R.id.gameTypeLogo);
		mGameDescription = (TextSwitcher) findViewById(R.id.gameTypeDescription);

		mSwipeDetector = new GestureDetector(new HomeSwipeDetector());
		mSwipeListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return mSwipeDetector.onTouchEvent(event);
			}
		};
		
		mSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
		mReslideLeft = AnimationUtils.loadAnimation(this, R.anim.reslide_left);
		mSlideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
		mReslideRight = AnimationUtils
				.loadAnimation(this, R.anim.reslide_right);

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
		applyMotionListeners();
		
		gameType = 0;
		showGameType();
		
	}

	private void applyMotionListeners() {
		// TODO Auto-generated method stub
		((RelativeLayout) findViewById(R.id.layout)).setOnTouchListener(mSwipeListener);
	}
	
	public void onStartGame(View v) {
		Intent intent;
		switch (GAME_TYPES_HEADERS[gameType]) {
			case R.drawable.tutorial:
				intent = new Intent(HomeActivity.this, TutorialActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				break;
			case R.drawable.classic:
				intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				break;
			case R.drawable.challenge:
				intent = new Intent(HomeActivity.this, PracticeGameActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				break;
		}
	}

	public void onNextGameType(View v) {
		mGameHeader.setInAnimation(mReslideRight);
		mGameDescription.setInAnimation(mReslideRight);
		mGameHeader.setOutAnimation(mSlideLeft);
		mGameDescription.setOutAnimation(mSlideLeft);

		gameType = (gameType + (NUMBER_OF_GAME_TYPES + 1)) % NUMBER_OF_GAME_TYPES;
		
		showGameType();
	}
	
	public void onPrevGameType(View v) {
		mGameHeader.setInAnimation(mReslideLeft);
		mGameDescription.setInAnimation(mReslideLeft);
		mGameHeader.setOutAnimation(mSlideRight);
		mGameDescription.setOutAnimation(mSlideRight);

		gameType = (gameType + (NUMBER_OF_GAME_TYPES - 1)) % NUMBER_OF_GAME_TYPES;
		
		showGameType();
	}	
	

	private void showGameType() {
		mGameHeader.setImageResource( GAME_TYPES_HEADERS[gameType] );
		mGameDescription.setText( getString(GAME_TYPES_DESCRIPTION[gameType]) );
	}

	public void startGame(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.gameClassic:
			intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

		// Animation navigationAnimation = AnimationUtils.loadAnimation(this,
		// R.anim.navigation_fade);
		// mNavigationLayout.startAnimation(navigationAnimation);

		Animation logoAnimation = AnimationUtils.loadAnimation(this,
				R.anim.logo);
		mTrioLogo.startAnimation(logoAnimation);

		
		Animation cloudsAnimation = AnimationUtils.loadAnimation(this,
				R.anim.clouds);
		mClouds.startAnimation(cloudsAnimation);
	}
	
	private class HomeSwipeDetector extends SimpleOnGestureListener {
		int SWIPE_MIN_DISTANCE = 100;
		int SWIPE_THRESHOLD_VELOCITY = 200;
		int SWIPE_MAX_OFF_PATH = 250;
		
		@Override
		public boolean onDown(MotionEvent e) {
			if (Trio.LOCAL_LOGD)
				Log.d("Trio Home", "Coords: x=" + e.getX() + ",y=" + e.getY());
		    return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Trio.LOCAL_LOGD) 
				Log.d("Trio Home", "Fling Coords: x1=" + e1.getX() + ",y1=" + e1.getY()+ " x2=" + e2.getX() + ",y2=" + e2.getY());
			try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                	Log.e("Trio Home", "Too far");
                    return false;
                }
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(HomeActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                	onNextGameType(null);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(HomeActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                	onPrevGameType(null);
                }
            } catch (Exception e) {
            	Log.e("Trio Home", "Exception: " + e.getLocalizedMessage());
            }
            return false;
		}
	}

	
}

// @Override
// protected void onResume() {
// // TODO Auto-generated method stub
// mSurface.onResumeTrioSurface();
// super.onResume();
// }
//
// @Override
// protected void onPause() {
// // TODO Auto-generated method stub
// mSurface.onPauseTrioSurface();
// super.onPause();
// }
//
// private class NavigationAdapter extends PagedAdapter {
//
// private ArrayList<GameType> options = new ArrayList<GameType>();
// private OnPageItemClick listener = null;
//
// public NavigationAdapter() {
// options.add(new GameType(
// GAME_TYPE_TUTORIAL,
// getResources().getDrawable(R.drawable.tutorial),
// getResources().getDrawable(R.drawable.ic_launcher),
// getString(R.string.game_tutorial_desc)));
// options.add(new GameType(
// GAME_TYPE_CLASSIC,
// getResources().getDrawable(R.drawable.classic),
// getResources().getDrawable(R.drawable.ic_launcher),
// getString(R.string.game_classic_desc)));
// options.add(new GameType(
// GAME_TYPE_CHALLENGE,
// getResources().getDrawable(R.drawable.challenge),
// getResources().getDrawable(R.drawable.ic_launcher),
// getString(R.string.game_challenge_desc)));
// }
//
// public void setOnItemClickListener(OnPageItemClick onPageItemClick) {
// listener = onPageItemClick;
// }
//
// @Override
// public int getCount() {
// // TODO Auto-generated method stub
// return options.size();
// }
//
// @Override
// public Object getItem(int arg0) {
// // TODO Auto-generated method stub
// return null;
// }
//
// @Override
// public long getItemId(int arg0) {
// // TODO Auto-generated method stub
// return 0;
// }
//
// @Override
// public View getView(int position, View convertView, ViewGroup parent) {
// // TODO Auto-generated method stub
//
// if (convertView == null) {
// convertView = getLayoutInflater().inflate(R.layout.home_navigation_item,
// parent, false);
// }
//
// final GameType option = options.get(position);
// if (option != null) {
//
// ImageView header = (ImageView) convertView.findViewById(R.id.navHeader);
// ImageView icon = (ImageView) convertView.findViewById(R.id.navIcon);
// TextView tagline = (TextView) convertView.findViewById(R.id.navDesc);
//
// header.setImageDrawable(option.getName());
// icon.setImageDrawable(option.getIcon());
// tagline.setText(option.getDescription());
//
// if (null != listener) {
// convertView.setOnClickListener(new OnClickListener() {
//
// public void onClick(View v) {
// listener.click(option.getId(), v);
// }
// });
// }
//
// }
//
// return convertView;
// }
//
// }
//
// public interface OnPageItemClick {
// public void click(int id, View view);
// }
// }
