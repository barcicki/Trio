package com.barcicki.trio.animations;

import android.content.Context;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.barcicki.trio.R;

public class CloudAnimation extends Animation {

	private Context mContext;
	
	public CloudAnimation(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context; 
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		// TODO Auto-generated method stub
		super.initialize(width, height, parentWidth, parentHeight);
		
		setDuration(mContext.getResources().getInteger(R.integer.cloud_anim_duration));
		setInterpolator(new LinearInterpolator());
		
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// TODO Auto-generated method stub
		final Matrix matrix = t.getMatrix();
		
		
		
	}
		
	
	
}
