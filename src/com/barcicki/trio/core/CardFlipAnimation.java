package com.barcicki.trio.core;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

public class CardFlipAnimation extends Animation {
	private final float mFromDegrees;
	private final float mToDegrees;
	private float mCenterX;
	private float mCenterY;
	private final float mDepthZ;
	private Camera mCamera;

	public CardFlipAnimation(float fromDegree, float toDegree) {
		mFromDegrees = fromDegree;
		mToDegrees = toDegree;
		mDepthZ = 0f;
		setZAdjustment(ZORDER_BOTTOM);
		setDetachWallpaper(true);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, width * 2, height * 2);
		mCamera = new Camera();
		mCenterX = (float) width / 2;
		mCenterY = (float) height / 2;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		camera.save();
		camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
