package com.barcicki.trio.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
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
			
			if (null == this.cardCache) {
				
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
			
			if (this.overdraw) {
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
	}
}
