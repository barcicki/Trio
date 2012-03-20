package com.barcicki.trio.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.barcicki.trio.R;

public class TrioDrawingLogo extends TrioDrawing {

	private static float SCALE_START = 0.01f;
	private static float SCALE_END = 1f;
	private static float SCALE_STEP = 0.02f;
	
	float scale = SCALE_START;
	float step = SCALE_STEP;
	
	public TrioDrawingLogo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private Bitmap logo;
	
	@Override
	boolean onInitialize(Resources res) {
		// TODO Auto-generated method stub
		
		logo = BitmapFactory.decodeResource(res, R.drawable.trio);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		
		
		
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		
		if (scale < SCALE_END) {
			scale += step;
			step *= 1.1f;
		}
		
		Bitmap bitmap = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, false);
		
		float center = canvas.getWidth() / 2;
		float top = canvas.getHeight() * 0.3f - bitmap.getHeight() / 2;
		float left = center - bitmap.getWidth() / 2;
		canvas.drawBitmap(bitmap, left, top, null);
		
	}

}
