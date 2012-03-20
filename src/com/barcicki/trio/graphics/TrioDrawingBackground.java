package com.barcicki.trio.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.barcicki.trio.R;

public class TrioDrawingBackground extends TrioDrawing {

	private Bitmap background;
	
	public TrioDrawingBackground(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean onInitialize(Resources res) {
		background = BitmapFactory.decodeResource(res, R.drawable.background);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		float scaledWidth = (float) canvas.getWidth() / background.getWidth();
		float scaledHeight = (float) canvas.getHeight() / background.getHeight();
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaledWidth, scaledHeight);
		
		Bitmap bg_bitmap = Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight(), matrix, false); 
		canvas.drawBitmap(bg_bitmap, 0, 0, null);
		
	}

}
