package com.barcicki.trio.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader.TileMode;

import com.barcicki.trio.R;

public class CardViewResources {
	
	private static Bitmap emptySqaure;
	private static Bitmap selectedSquare;
	private static BitmapShader blueShader;
	private static BitmapShader greenShader;
	private static BitmapShader redShader;
	
	private static int whiteColor;
	private static int blueColor;
	private static int redColor;
	private static int greenColor;
	
	private static int strokeWidth;
	
	private static boolean isInitialized = false;
	
	static void initialize(Context context) {
		if (!isInitialized) {
			Resources res = context.getResources();
			
			emptySqaure = BitmapFactory.decodeResource(res, R.drawable.square);
			selectedSquare= BitmapFactory.decodeResource(res, R.drawable.square_selected);
			
			blueShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.blue_shader), TileMode.REPEAT, TileMode.REPEAT);
			redShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.red_shader), TileMode.REPEAT, TileMode.REPEAT);
			greenShader = new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.green_shader), TileMode.REPEAT, TileMode.REPEAT);
			
			whiteColor = res.getColor(R.color.white);
			greenColor = res.getColor(R.color.green);
			blueColor = res.getColor(R.color.blue);
			redColor = res.getColor(R.color.red);
			
			strokeWidth = res.getInteger(R.integer.stroke);
			
			isInitialized = true;
		}
	}
	
	static Bitmap getBackgroundSqaure(boolean isSelected) {
		return isSelected ? selectedSquare : emptySqaure;
	}
	
	static BitmapShader getShader(int color) {
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
	
	static int getWhiteColor() {
		return whiteColor;
	}
	
	static int getCardColor(int color) {
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
	
	static int getStrokeWidth() {
		return strokeWidth;
	}
}
