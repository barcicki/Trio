package com.barcicki.trio.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.barcicki.trio.R;

public class CardView extends ImageView  {

	private Card card = null;
	
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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Resources res = getContext().getResources();
		Bitmap square = BitmapFactory.decodeResource(res, R.drawable.square);
		float ratio = (float) getWidth() / square.getWidth();
//		
		Matrix matrix = new Matrix();
		matrix.postScale(ratio, ratio);
//		
		canvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, square.getWidth(), square.getHeight(), matrix, false), 0, 0, null);
		
		if (null != card) {
			canvas = card.drawCanvas(canvas, res, getWidth(), getHeight());
		} else {
			canvas = new Card(Card.SHAPE_CIRCLE, Card.COLOR_GREEN, Card.FILL_FULL, Card.NUMBER_TWO).drawCanvas(canvas, res, getWidth(), getHeight());
		}
		
		postInvalidate();
		
	}
}
