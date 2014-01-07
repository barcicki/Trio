package com.barcicki.trio.views;

import com.barcicki.trio.core.Trio;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MenuDescriptionPlaceholder extends RelativeLayout {

	private MenuDescriptionGestureListener mListener = null;

	private final int SWIPE_MIN_DISTANCE = 150;

	private boolean mIsScrolling = false;
	private float mStartScroll = 0f;

	public MenuDescriptionPlaceholder(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGestureListener(MenuDescriptionGestureListener listener) {
		mListener = listener;
	}

	public MenuDescriptionGestureListener getGestureListener() {
		return mListener;
	}

	public interface MenuDescriptionGestureListener {
		public void onUp(int diff);
		public void onMoving(int diff, boolean isMoving);
		public void onDown(int diff);
		public void onStoppedMoving(int diff);
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
				diff = Math.round(event.getY() - mStartScroll);
				mIsScrolling = Math.abs(diff) > SWIPE_MIN_DISTANCE;
				
				if (Trio.LOCAL_LOGD)
					Log.d("EVENT", "moving! " + diff);
				
				if (mListener != null) {
					mListener.onMoving(diff, mIsScrolling);
				}
				return true;
	
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				diff = Math.round(event.getY() - mStartScroll);
				if (mIsScrolling) {
					if (mListener != null) {
						if (diff > 0) {
							if (Trio.LOCAL_LOGD) Log.d("EVENT", "up!");
							mListener.onUp(diff);
						} else {
							if (Trio.LOCAL_LOGD) Log.d("EVENT", "down!");
							mListener.onDown(diff);
						}
					}
				} else {
					if (mListener != null) {
						mListener.onStoppedMoving(diff);
					}
				}
				mIsScrolling = false;
				return true;
				
			default:
				return super.onTouchEvent(event);
		}
	}
}