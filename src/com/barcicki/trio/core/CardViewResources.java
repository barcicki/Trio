package com.barcicki.trio.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

import com.barcicki.trio.R;

public class CardViewResources {
	
	private static BitmapDrawable emptySqaure;
	private static BitmapDrawable selectedSquare;
	
	private static BitmapShader blueShader;
	private static BitmapShader greenShader;
	private static BitmapShader redShader;
	
	private static int whiteColor;
	private static int blueColor;
	private static int redColor;
	private static int greenColor;
	
	private static int strokeWidth;
	
	private static Paint bitmapPaint;
	
	private static boolean isInitialized = false;
	
	static public void initialize(Context context) {
		if (!isInitialized) {
			Resources res = context.getResources();
			
			emptySqaure = (BitmapDrawable) res.getDrawable(R.drawable.square);
			emptySqaure.setAntiAlias(true);
			emptySqaure.setFilterBitmap(true);
			
			selectedSquare = (BitmapDrawable) res.getDrawable(R.drawable.square_selected);
			selectedSquare.setAntiAlias(true);
			selectedSquare.setFilterBitmap(true);
			
			blueShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.blue_shader), TileMode.REPEAT, TileMode.REPEAT);
			redShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.red_shader), TileMode.REPEAT, TileMode.REPEAT);
			greenShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.green_shader), TileMode.REPEAT, TileMode.REPEAT);
			
			whiteColor = res.getColor(R.color.white);
			greenColor = res.getColor(R.color.green);
			blueColor = res.getColor(R.color.blue);
			redColor = res.getColor(R.color.red);
			
			strokeWidth = res.getInteger(R.integer.stroke);
			
			bitmapPaint =  new Paint();
			bitmapPaint.setAntiAlias(true);
			bitmapPaint.setFilterBitmap(true);
			bitmapPaint.setDither(true);
			
			isInitialized = true;
		}
	}
	
	static public BitmapDrawable getBackgroundSqaure(boolean isSelected) {
		return isSelected ? selectedSquare : emptySqaure;
	}
	
	static public BitmapShader getShader(int color) {
		switch (color) {
			case Card.COLOR_BLUE:
				return blueShader;
			case Card.COLOR_GREEN:
				return greenShader;
			case Card.COLOR_RED:
			default:
				return redShader;
		}
	}
	
	static public int getWhiteColor() {
		return whiteColor;
	}
	
	static public int getCardColor(int color) {
		switch (color) {
			case Card.COLOR_BLUE:
				return blueColor;
			case Card.COLOR_GREEN:
				return greenColor;
			case Card.COLOR_RED:
			default:
				return redColor;
		}
	}
	
	static public int getStrokeWidth() {
		return strokeWidth;
	}

	static public Paint getBitmapPaint() {
		return bitmapPaint;
	}
}
