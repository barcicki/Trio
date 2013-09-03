package com.barcicki.trio.views;

import com.barcicki.trio.core.MenuDescription.MenuDescriptionType;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MenuDescriptionPlaceholder extends RelativeLayout {

	private MenuDescriptionGestureListener mListener = null;

	private final int SWIPE_MIN_DISTANCE = 100;

	private boolean mIsScrolling = false;
	private float mStartScroll = 0f;

	public MenuDescriptionPlaceholder(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setGestureListener(MenuDescriptionGestureListener listener) {
		mListener = listener;
	}

	public MenuDescriptionGestureListener getGestureListener() {
		return mListener;
	}

	public interface MenuDescriptionGestureListener {
		public void onUp();

		public void onDown();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = MotionEventCompat.getActionMasked(event);
		int diff;

		switch (action) {

			case MotionEvent.ACTION_DOWN:
				mStartScroll = event.getY();
				return true;
	
			case MotionEvent.ACTION_MOVE:
				Log.d("EVENT", "moving!");
				if (mIsScrolling) {
					return true;
				}
	
				diff = Math.round(event.getY() - mStartScroll);
	
				if (Math.abs(diff) > SWIPE_MIN_DISTANCE) {
					mIsScrolling = true;
				}
				return true;
	
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (mIsScrolling) {
					diff = Math.round(event.getY() - mStartScroll);
					if (mListener != null) {
						if (diff > 0) {
							Log.d("EVENT", "up!");
							mListener.onUp();
						} else {
							Log.d("EVENT", "down!");
							mListener.onDown();
						}
					}
				}
				mIsScrolling = false;
				return true;
				
			default:
				return super.onTouchEvent(event);
		}
	}
}