package com.barcicki.trio.animations;

import android.view.animation.Interpolator;

public class ReverseInterpolator implements Interpolator {
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}