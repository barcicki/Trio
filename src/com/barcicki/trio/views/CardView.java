package com.barcicki.trio.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.barcicki.trio.R;
import com.barcicki.trio.core.Card;
import com.barcicki.trio.core.CardFlipAnimation;
import com.barcicki.trio.core.CardViewResources;
import com.barcicki.trio.core.FailAnimation;

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
		
		if (cardCache != null) {
			cardCache.recycle();
		}
		cardCache = null;
		
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
					
					Bitmap square = CardViewResources.getBackgroundSqaure(isSelected()).getBitmap();
					float width_ratio = (float) getWidth() / square.getWidth();
					float height_ratio = (float) getHeight() / square.getHeight();
					
//						ScaleType scaleType = getScaleType();
					int x_offset = 0, y_offset = 0;
					
					// sqaure assumption, centering
					if (width_ratio > height_ratio) {
//							if (scaleType.equals(ScaleType.CENTER)) {
							x_offset = (getWidth() - getHeight()) / 2;	
//							} else if (scaleType.equals(ScaleType.FIT_END)) {
//								x_offset = getWidth() - getHeight();
//							}
					} else {
						y_offset = (getHeight() - getWidth()) / 2;
					}
					
					float ratio = Math.min( width_ratio, height_ratio);
//				
					Matrix matrix = new Matrix();
					matrix.postScale(ratio, ratio);
					
					cacheCanvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, square.getWidth(), square.getHeight(), matrix, false), x_offset, y_offset, CardViewResources.getBitmapPaint());
					
					drawCard(cacheCanvas, card, (int) (square.getWidth() * ratio), (int) (square.getHeight() * ratio), x_offset, y_offset);
			}
			
			if (this.cardCache != null && this.overdraw && getWidth() > 0 && getHeight() > 0) {
				canvas.drawBitmap(this.cardCache, 0, 0, null);
			} else {
				super.onDraw(canvas);
			}

		} else {
			super.onDraw(canvas);
		}
		
		
	}
	
	public Canvas drawCard(Canvas canvas, Card card, int width, int height, int x_offset, int y_offset) {

		PointF[] points = {};

		float item_size_width = width / 3f;
		float item_size_height = height / 3f;
		

		PointF point1, point2, point3;
		switch (card.getNumber()) {
		case Card.NUMBER_ONE:
			point1 = new PointF((width - item_size_width) / 2f + x_offset,
					(height - item_size_height) / 2f + y_offset);
			points = new PointF[] { point1 };
			break;
		case Card.NUMBER_TWO:
			point1 = new PointF(width / 4f - item_size_width / 3f + x_offset, height / 4f
					- item_size_height / 3f + y_offset);
			point2 = new PointF(3 * width / 4f - 2 * item_size_width / 3f + x_offset, 3 * height / 4f
					- 2* item_size_height / 3f + y_offset);
			points = new PointF[] { point1, point2 };
			break;
		case Card.NUMBER_THREE:
			point1 = new PointF(width / 4f - item_size_width / 3f + x_offset, height / 4f
					- item_size_height / 3f + y_offset);
			point2 = new PointF(3 * width / 4f - 2 * item_size_width / 3f + x_offset, height / 4f
					- item_size_height / 3f + y_offset);
			point3 = new PointF(width / 2f - item_size_width / 2f + x_offset, 3 * height / 4f
					- 2 * item_size_height / 3f + y_offset);
			points = new PointF[] { point1, point2, point3 };
			break;
		}

		Paint paint = new Paint();
		Paint shaderPaint = new Paint();
		Paint whitePaint = new Paint();
		
		whitePaint.setStyle(Style.FILL);
		whitePaint.setAntiAlias(true);
		whitePaint.setColor(CardViewResources.getWhiteColor());
		shaderPaint.setStyle(Style.FILL);
		shaderPaint.setAntiAlias(true);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
				
		paint.setColor(CardViewResources.getCardColor(card.getColor()));
		shaderPaint.setShader(CardViewResources.getShader(card.getColor()));
		
		switch (card.getFill()) {
			case Card.FILL_FULL:
				paint.setStyle(Style.FILL_AND_STROKE);
				break;
			case Card.FILL_HALF:
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(CardViewResources.getStrokeWidth());
				break;
			case Card.FILL_EMPTY:
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(CardViewResources.getStrokeWidth());
				break;
		}
		
		for (int i = 0; i < (card.getNumber()+ 1); i++) {
			switch (card.getShape()) {
			case Card.SHAPE_CIRCLE:
				
				if (Card.FILL_HALF == card.getFill()) {
					canvas.drawCircle(points[i].x + item_size_width/2f, points[i].y + item_size_height/2f,
							item_size_width / 2f, shaderPaint);
				}
				
				if (Card.FILL_EMPTY == card.getFill()) {
					canvas.drawCircle(points[i].x + item_size_width/2f, points[i].y + item_size_height/2f,
							item_size_width / 2f, whitePaint);
				}
				
				canvas.drawCircle(points[i].x + item_size_width/2f, points[i].y + item_size_height/2f,
						item_size_width / 2f, paint);
				
				break;
			case Card.SHAPE_SQUARE:
				RectF rect = new RectF(points[i].x, points[i].y, points[i].x
						+ item_size_width, points[i].y + item_size_height);
				
				
				if (Card.FILL_HALF == card.getFill()) {
					canvas.drawRect(rect, shaderPaint);
				}
				
				if (Card.FILL_EMPTY == card.getFill()) {
					canvas.drawRect(rect, whitePaint);
				}
				
				canvas.drawRect(rect, paint);
				
				break;
			case Card.SHAPE_TRIANGLE:
				
				
				Path path = new Path();
				PointF top = new PointF(points[i].x + item_size_width / 2f, points[i].y + 0.1f * item_size_height);
				PointF bottom_left = new PointF(points[i].x, points[i].y + item_size_height);
				PointF bottom_right = new PointF(points[i].x + item_size_width, points[i].y + item_size_height);

				path.moveTo(top.x, top.y);
				path.lineTo(bottom_right.x, bottom_right.y);
				
//				path.moveTo(bottom_right.x, bottom_right.y);
				path.lineTo(bottom_left.x, bottom_left.y);
				
//				path.moveTo(bottom_left.x, bottom_left.y);
				path.lineTo(top.x, top.y);
				
				path.close();
				
				if (Card.FILL_EMPTY == card.getFill()) {
					canvas.drawPath(path, whitePaint);
				}

				if (Card.FILL_HALF == card.getFill()) {
					canvas.drawPath(path, shaderPaint);
				}
				
				canvas.drawPath(path, paint);
				
			}

		}

		return canvas;
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
		Animation failAnimation = new FailAnimation();
//		Animation failAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.card_fail);
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
	
	public void stopSwitchingCard() {
		Animation currentAnimation = getAnimation();
		if (currentAnimation != null) {
			currentAnimation.setAnimationListener(null);
			currentAnimation.cancel();
		}
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
