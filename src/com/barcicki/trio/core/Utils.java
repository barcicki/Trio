package com.barcicki.trio.core;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class Utils {
	
	
	public static void setAlpha(View view, float alpha) {
        final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        view.startAnimation(animation);
	}
	
	public static AlphaAnimation generateAlphaAnimation(float from, float to, long duration) {
		AlphaAnimation anim = new AlphaAnimation(from, to);
//		anim.setFillAfter(true);
		anim.setDuration(duration);
		return anim;
	}
	
	public static Animation generateSlideFromTopAnimation(long duration) {
		
		AnimationSet set = new AnimationSet(true);
		
		TranslateAnimation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -2.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
		
		set.addAnimation(slideDown);
		set.addAnimation(generateAlphaAnimation(0f, 1f, duration));
		set.setDuration(duration);
		set.setFillAfter(true);
		
		return set;
	}
	
	public static Animation generateSlideToBottomAnimation(long duration) {
		
		AnimationSet set = new AnimationSet(true);
		
		TranslateAnimation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 2.0f);
		
		set.addAnimation(slideDown);
		set.addAnimation(generateAlphaAnimation(1f, 0f, duration));
		set.setDuration(duration);
//		set.setFillAfter(true);
		
		return set;
	}
	
	public static Animation generateSlideFromBottomAnimation(long duration) {
		
		AnimationSet set = new AnimationSet(true);
		
		TranslateAnimation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 2.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
		
		set.addAnimation(slideDown);
		set.addAnimation(generateAlphaAnimation(0f, 1f, duration));
		set.setDuration(duration);
//		set.setFillAfter(true);
		
		return set;
	}
	
	public static Animation generateSlideToTopAnimation(long duration) {
		
		AnimationSet set = new AnimationSet(true);
		
		TranslateAnimation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -2.0f);
		
		set.addAnimation(slideDown);
		set.addAnimation(generateAlphaAnimation(1f, 0f, duration));
		set.setDuration(duration);
//		set.setFillAfter(true);
		
		return set;
	}
}
