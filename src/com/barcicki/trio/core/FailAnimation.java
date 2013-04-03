package com.barcicki.trio.core;

import android.view.animation.CycleInterpolator;

public class FailAnimation extends CardFlipAnimation {

	public FailAnimation() {
		super(0, 50);
		setInterpolator(new CycleInterpolator(2f));
		setDuration(500);
	}
}
