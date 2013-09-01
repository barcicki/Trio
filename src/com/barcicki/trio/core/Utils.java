package com.barcicki.trio.core;

import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class Utils {
	
	
	public static void setAlpha(View view, float alpha) {
        final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        view.startAnimation(animation);
	}
	
	public static AlphaAnimation generateAlphaAnimation(float from, float to, long duration) {
		AlphaAnimation anim = new AlphaAnimation(from, to);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		return anim;
	}
}
