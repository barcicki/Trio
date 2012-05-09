package com.barcicki.trio.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.barcicki.trio.R;

public class CardView extends ImageView  {

	private Card card = null;
	private Bitmap cardCache = null;
	private boolean overdraw = true;
	
	public CardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
	}

	public CardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CardView(Context context) {
		this(context, null);
	}
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
		this.cardCache = null;
		invalidate();
		refreshDrawableState();		
	}
	
	
	public boolean isOverdrawn() {
		return overdraw;
	}

	public void setOverdraw(boolean overdraw) {
		this.overdraw = overdraw;
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		this.cardCache = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (null != card) {
			
			if (null == this.cardCache && getWidth() > 0 && getHeight() > 0) {
				
				this.cardCache = Bitmap.createBitmap(getWidth(), getHeight(),
						Config.ARGB_8888);
				
				Canvas cacheCanvas = new Canvas(this.cardCache);
				Resources res = getContext().getResources();
				
				
				Bitmap square = BitmapFactory.decodeResource(res, isSelected() ? R.drawable.square_selected : R.drawable.square);
				float width_ratio = (float) getWidth() / square.getWidth();
				float height_ratio = (float) getHeight() / square.getHeight();
				
//				ScaleType scaleType = getScaleType();
				int x_offset = 0, y_offset = 0;
				
				// sqaure assumption, centering
				if (width_ratio > height_ratio) {
//					if (scaleType.equals(ScaleType.CENTER)) {
						x_offset = (getWidth() - getHeight()) / 2;	
//					} else if (scaleType.equals(ScaleType.FIT_END)) {
//						x_offset = getWidth() - getHeight();
//					}
				} else {
					y_offset = (getHeight() - getWidth()) / 2;
				}
				
				float ratio = Math.min( width_ratio, height_ratio);
//		
				Matrix matrix = new Matrix();
				matrix.postScale(ratio, ratio);
				
				cacheCanvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, square.getWidth(), square.getHeight(), matrix, false), x_offset, y_offset, null);
				cacheCanvas = card.drawCanvas(cacheCanvas, res, (int) (square.getWidth() * ratio), (int) (square.getHeight() * ratio), x_offset, y_offset);
												
			}
			
			if (this.overdraw && getWidth() > 0 && getHeight() > 0) {
				canvas.drawBitmap(this.cardCache, 0, 0, null);
			} else {
				super.onDraw(canvas);
			}

		} else {
			super.onDraw(canvas);
		}
		
		
	}
	
	public void  redraw() {
		this.cardCache = null;
//		invalidate();
//		refreshDrawableState();
	}
	
	private AnimationListener mFailAnimationListener = null;
	private AnimationListener mSwitchAnimationListener = null;
	private AnimationListener mRevealAniamiationListener = null;
	private AnimationListener mHideAnimationListener;
	private AnimationListener mShowAnimationListener;
	
	public void setFailAnimationLsitener(AnimationListener failAnimationListener) {
		mFailAnimationListener = failAnimationListener;
	}
		
	public void animateFail() {
		Animation failAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_fail);
		failAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (mFailAnimationListener != null) mFailAnimationListener.onAnimationStart(animation);
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				if (mFailAnimationListener != null) mFailAnimationListener.onAnimationRepeat(animation);
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				invalidate();
				refreshDrawableState();
				setSelected(false);
				if (mFailAnimationListener != null) mFailAnimationListener.onAnimationEnd(animation);
			}
		});
		startAnimation(failAnimation);
	}
	
	public void setSwitchAnimationLsitener(AnimationListener animationListener) {
		mSwitchAnimationListener = animationListener;
	}
	
	public void animateSwitchCard(final Card nextCard) {
		int duration = getResources().getInteger(R.integer.card_flip_anim_duration);
		animateSwitchCard(nextCard, duration);
	}
	
	public void animateSwitchCard(Card card, int duration) {
		animateSwitchCard(card, duration, 0);
	}
	
	public void animateSwitchCard(final Card nextCard, int duration, int delay) {
//		Animation switchAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_flip);
		final Animation switchAnimation = new CardFlipAnimation(0, 90);
//		final Animation switchBackAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_reflip);
		final Animation switchBackAnimation = new CardFlipAnimation(270, 360);
		switchAnimation.setDuration(duration);
		switchAnimation.setStartOffset(delay);
		switchBackAnimation.setDuration(duration);
		switchBackAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				setSelected(false);
				invalidate();
				refreshDrawableState();
				if (mSwitchAnimationListener != null) mSwitchAnimationListener.onAnimationEnd(animation);
			}
		});
		
		switchAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (mSwitchAnimationListener != null) mSwitchAnimationListener.onAnimationStart(animation);
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				setCard(nextCard);
				setSelected(false);
				invalidate();
				refreshDrawableState();
				startAnimation(switchBackAnimation);
			}
		});
		
		startAnimation(switchAnimation);
	}
	
	public void setRevealAnimationListener(AnimationListener animationListener) {
		mRevealAniamiationListener = animationListener;
	}
	
	public void animateReveal() {
		Animation switchAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_flip);
		final Animation switchBackAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_reflip);
		switchBackAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				invalidate();
				refreshDrawableState();
				setSelected(false);
				if (mRevealAniamiationListener != null) mRevealAniamiationListener.onAnimationEnd(animation);
			}
		});
		
		switchAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				setOverdraw(false);
				if (mRevealAniamiationListener != null) mRevealAniamiationListener.onAnimationStart(animation);
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				setSelected(false);
				invalidate();
				refreshDrawableState();
				setOverdraw(true);
				startAnimation(switchBackAnimation);
			}
		});
		
		startAnimation(switchAnimation);
	}

	public void setHideAnimationListener(AnimationListener animationListener) {
		mHideAnimationListener = animationListener;
	}
	
	public void animateHide() {
		Animation hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_remove);
		hideAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (mHideAnimationListener != null) mHideAnimationListener.onAnimationStart(animation);
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				if (mHideAnimationListener != null) mHideAnimationListener.onAnimationRepeat(animation);
			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
				if (mHideAnimationListener != null) mHideAnimationListener.onAnimationEnd(animation);
			}
		});
		
		startAnimation(hideAnimation);
	}
	
	public void setShowAnimationListener(AnimationListener animationListener) {
		mShowAnimationListener = animationListener;
	}
	
	public void animateShow() {
		Animation hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_appear);
		hideAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (mShowAnimationListener != null) mShowAnimationListener.onAnimationStart(animation);
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				if (mShowAnimationListener != null) mShowAnimationListener.onAnimationRepeat(animation);
			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				setVisibility(View.VISIBLE);
				if (mShowAnimationListener != null) mShowAnimationListener.onAnimationEnd(animation);
			}
		});
		
		startAnimation(hideAnimation);
	}
	
	public void forceMeasureSize(int width, int height) {
		setMeasuredDimension(width, height);
	}


	
}
