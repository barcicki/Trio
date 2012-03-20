package com.barcicki.trio;

import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcicki.trio.core.GameType;

public class HomeActivity extends Activity {
	
	final private int GAME_TYPE_CLASSIC = 1;
	final private int GAME_TYPE_TUTORIAL = 2;
	final private int GAME_TYPE_CHALLENGE = 3;
	
//	private TrioSurface mSurface;
	private PagedView mPagedView;
	private ImageView mTrioLogo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_static);
		
//	with moving clouds		
//		mSurface = (TrioSurface) findViewById(R.id.layoutSurface);
//		mSurface.addDrawing(new TrioDrawingBackground(this));
//		mSurface.addDrawing(new TrioDrawingClouds(this));
//		mSurface.addDrawing(new TrioDrawingLogo(this));
		
		applyAnimations();
		
		NavigationAdapter navigation = new NavigationAdapter();
		navigation.setOnItemClickListener(new OnPageItemClick() {
			
			@Override
			public void click(int id, View view) {
				Intent intent;
				switch (id) {
					case GAME_TYPE_TUTORIAL:
						intent = new Intent(HomeActivity.this, ClassicGameActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intent);
						break;
					case GAME_TYPE_CLASSIC:
						intent = new Intent(HomeActivity.this, TrioActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intent);
						break;
				}
			}
		});
		mPagedView.setAdapter(navigation);
		

	}

	private void applyAnimations() {
		mPagedView = (PagedView) findViewById(R.id.navigation);
		Animation navigationAnimation = AnimationUtils.loadAnimation(this, R.anim.navigation_fade);
		mPagedView.startAnimation(navigationAnimation);
		
		mTrioLogo = (ImageView) findViewById(R.id.layoutTrio);
		Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo);
		mTrioLogo.startAnimation(logoAnimation);
		
		ImageView clouds = (ImageView) findViewById(R.id.layoutClouds);
		Animation cloudsAnimation = AnimationUtils.loadAnimation(this, R.anim.clouds);
		clouds.startAnimation(cloudsAnimation);
	}
	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		mSurface.onResumeTrioSurface();
//		super.onResume();
//	}
//	
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		mSurface.onPauseTrioSurface();
//		super.onPause();
//	}
//	
	private class NavigationAdapter extends PagedAdapter {
		
		private ArrayList<GameType> options = new ArrayList<GameType>();
		private OnPageItemClick listener = null;
		
		public NavigationAdapter() {
			options.add(new GameType(
					GAME_TYPE_TUTORIAL, 
					getResources().getDrawable(R.drawable.tutorial), 
					getResources().getDrawable(R.drawable.ic_launcher), 
					getString(R.string.game_tutorial_desc)));
			options.add(new GameType(
					GAME_TYPE_CLASSIC, 
					getResources().getDrawable(R.drawable.classic), 
					getResources().getDrawable(R.drawable.ic_launcher), 
					getString(R.string.game_classic_desc)));
			options.add(new GameType(
					GAME_TYPE_CHALLENGE, 
					getResources().getDrawable(R.drawable.challenge), 
					getResources().getDrawable(R.drawable.ic_launcher), 
					getString(R.string.game_challenge_desc)));
		}
		
		public void setOnItemClickListener(OnPageItemClick onPageItemClick) {
			listener = onPageItemClick;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return options.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.home_navigation_item,
						parent, false);
			}
			
			final GameType option = options.get(position);
			if (option != null) {
				
				ImageView header = (ImageView) convertView.findViewById(R.id.navHeader);
				ImageView icon = (ImageView) convertView.findViewById(R.id.navIcon);
				TextView tagline = (TextView) convertView.findViewById(R.id.navDesc);
				
				header.setImageDrawable(option.getName());
				icon.setImageDrawable(option.getIcon());
				tagline.setText(option.getDescription());
				
				if (null != listener) {
					convertView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							listener.click(option.getId(), v);
						}
					});
				}
				
			}
			
			return convertView;
		}
		
	}
	
	public interface OnPageItemClick {
		public void click(int id, View view);
	}
}
