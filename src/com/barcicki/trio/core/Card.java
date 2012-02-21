package com.barcicki.trio.core;

import android.util.Log;

public class Card {
	
	public final static int SHAPE_SQUARE = 0,
							SHAPE_TRIANGE = 1,
							SHAPE_CIRCLE = 2,
							COLOR_GREEN = 0,
							COLOR_BLUE = 1,
							COLOR_RED = 2,
							FILL_EMPTY = 0,
							FILL_HALF = 1,
							FILL_FULL = 2,
							NUMBER_ONE = 0,
							NUMBER_TWO = 1,
							NUMBER_THREE = 2;
	
	private int shape,
				color,
				fill,
				number;
	
	public Card(int shape, int color, int fill, int number) {
		this.color = color;
		this.shape = shape;
		this.fill = fill;
		this.number = number;
		
//		Log.d("Trio", this.toString() + " added");
		 
	}

	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getFill() {
		return fill;
	}

	public void setFill(int fill) {
		this.fill = fill;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getColor());
		sb.append(getShape());
		sb.append(getNumber());
		sb.append(getFill());
		return sb.toString();
	}

}
