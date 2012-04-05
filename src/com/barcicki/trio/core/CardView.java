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
				float ratio = (float) getWidth() / square.getWidth();
//		
				Matrix matrix = new Matrix();
				matrix.postScale(ratio, ratio);
				
				cacheCanvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, square.getWidth(), square.getHeight(), matrix, false), 0, 0, null);
				cacheCanvas = card.drawCanvas(cacheCanvas, res, cacheCanvas.getWidth(), cacheCanvas.getHeight());
												
			}
			
			canvas.drawBitmap(this.cardCache, 0, 0, null);

		} else {
			super.onDraw(canvas);
		}
		
		
	}
	
	public void  redraw() {
		this.cardCache = null;
	}
}
