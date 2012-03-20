package com.barcicki.trio.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

abstract public class TrioDrawing {

	private boolean isInitialized = false;
	
	public TrioDrawing(Context context) {
		initialize(context.getResources());
	}
	
	abstract boolean onInitialize(Resources res);
	abstract public void onDraw(Canvas canvas);
	
	final public void initialize(Resources res) {
		if (onInitialize(res)) {
			isInitialized = true;
		} else {
			isInitialized = false;
		}
	}

	final public boolean isInitialized() {
		return isInitialized;
	}
	
	
	
}
