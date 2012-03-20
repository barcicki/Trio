package com.barcicki.trio.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.barcicki.trio.R;

public class TrioDrawingClouds extends TrioDrawing {

	private Bitmap clouds;
	float position = 0;
	
	
	public TrioDrawingClouds(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean onInitialize(Resources res) {
		clouds = BitmapFactory.decodeResource(res, R.drawable.clouds);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		float ratio = (float) canvas.getWidth() / clouds.getWidth();
		float scaledHeight = clouds.getHeight() * ratio;
		
		Matrix matrix = new Matrix();
//		matrix.postScale(ratio, ratio);
		
		position++;
		
		Bitmap clouds_bitmap = Bitmap.createBitmap(clouds, 0, 0, clouds.getWidth(), clouds.getHeight(), matrix, false);
		float left = - (clouds_bitmap.getWidth() - canvas.getWidth()) / 2 + 100f * (float) Math.sin(position/10.0);
		canvas.drawBitmap(clouds_bitmap, left, canvas.getHeight() - 0.7f * clouds_bitmap.getHeight(), null);
	}

}
