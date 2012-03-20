package com.barcicki.trio;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;

import com.barcicki.trio.core.DynamicCardAdapter;
import com.barcicki.trio.core.Trio;

public class ClassicGameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classic);
		
		applyAnimations();
		
		GridView grid = (GridView) findViewById(R.id.gridView1);
		DynamicCardAdapter adapter = new DynamicCardAdapter(this, R.layout.card, new Trio().getDeck());
		
		Log.d("trio", "" + adapter.getCount());
		grid.setAdapter(adapter);
	}
	
	private void applyAnimations() {
		ImageView clouds = (ImageView) findViewById(R.id.layoutClouds);
		Animation cloudsAnimation = AnimationUtils.loadAnimation(this, R.anim.clouds);
		clouds.startAnimation(cloudsAnimation);
	}
}
