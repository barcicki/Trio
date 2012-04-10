package com.barcicki.trio.core;

import com.barcicki.trio.R;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

public class Card {

	public final static int SHAPE_SQUARE = 0, SHAPE_TRIANGLE = 2,
			SHAPE_CIRCLE = 1, COLOR_GREEN = 2, COLOR_BLUE = 0, COLOR_RED = 1,
			FILL_EMPTY = 1, FILL_HALF = 2, FILL_FULL = 0, NUMBER_ONE = 0,
			NUMBER_TWO = 1, NUMBER_THREE = 2;

	private int shape, color, fill, number;

	public Card(int shape, int color, int fill, int number) {
		this.color = color;
		this.shape = shape;
		this.fill = fill;
		this.number = number;

		// Log.d("Trio", this.toString() + " added");

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getColor());
		sb.append(getFill());
		sb.append(getShape());
		sb.append(getNumber());
		return sb.toString();
	}

	public Canvas drawCanvas(Canvas canvas, Resources res, int width, int height) {

		// float[] positions = {};
		PointF[] points = {};

		float item_size_width = width / 3f;
		float item_size_height = height / 3f;
		
		BitmapShader[] shaders = {
				new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.blue_shader), TileMode.REPEAT, TileMode.REPEAT),
				new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.red_shader), TileMode.REPEAT, TileMode.REPEAT),
				new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.green_shader), TileMode.REPEAT, TileMode.REPEAT)
		};

		PointF point1, point2, point3;
		switch (number) {
		case NUMBER_ONE:
			point1 = new PointF((width - item_size_width) / 2f,
					(height - item_size_height) / 2);
			points = new PointF[] { point1 };
			break;
		case NUMBER_TWO:
			point1 = new PointF(width / 4f - item_size_width / 3f, height / 4f
					- item_size_height / 3f);
			point2 = new PointF(3 * width / 4f - 2 * item_size_width / 3f, 3 * height / 4f
					- 2* item_size_height / 3f);
			points = new PointF[] { point1, point2 };
			break;
		case NUMBER_THREE:
			point1 = new PointF(width / 4f - item_size_width / 3f, height / 4f
					- item_size_height / 3f);
			point2 = new PointF(3 * width / 4f - 2 * item_size_width / 3f, height / 4f
					- item_size_height / 3f);
			point3 = new PointF(width / 2f - item_size_width / 2f, 3 * height / 4f
					- 2 * item_size_height / 3f);
			points = new PointF[] { point1, point2, point3 };
			break;
		}

		Paint paint = new Paint();
		Paint shaderPaint = new Paint();
		
		shaderPaint.setStyle(Style.FILL);
		shaderPaint.setAntiAlias(true);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
				
		switch (color) {
			case COLOR_BLUE:
				paint.setColor(res.getColor(R.color.blue));
				break;
			case COLOR_GREEN:
				paint.setColor(res.getColor(R.color.green));
				break;
			case COLOR_RED:
				paint.setColor(res.getColor(R.color.red));
				break;
		}
		
		switch (fill) {
			case FILL_FULL:
				paint.setStyle(Style.FILL_AND_STROKE);
				break;
			case FILL_HALF:
//				paint.setShader(new BitmapShader(BitmapFactory.decodeResource(res, R.drawable.shader), TileMode.REPEAT, TileMode.REPEAT));
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(3);
				break;
			case FILL_EMPTY:
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(3);
				break;
		}
		
		shaderPaint.setShader( shaders[ color ]);

		for (int i = 0; i < (number + 1); i++) {
			switch (shape) {
			case SHAPE_CIRCLE:
				canvas.drawCircle(points[i].x + item_size_width/2f, points[i].y + item_size_height/2f,
						item_size_width / 2f, paint);
				
				if (FILL_HALF == fill) {
					canvas.drawCircle(points[i].x + item_size_width/2f, points[i].y + item_size_height/2f,
							item_size_width / 2f, shaderPaint);
				}
				break;
			case SHAPE_SQUARE:
				RectF rect = new RectF(points[i].x, points[i].y, points[i].x
						+ item_size_width, points[i].y + item_size_height);
				canvas.drawRect(rect, paint);
				
				if (FILL_HALF == fill) {
					canvas.drawRect(rect, shaderPaint);
				}
				break;
			case SHAPE_TRIANGLE:
				Path path = new Path();
				PointF top = new PointF(points[i].x + item_size_width / 2f, points[i].y + 0.1f * item_size_height);
				PointF bottom_left = new PointF(points[i].x, points[i].y + item_size_height);
				PointF bottom_right = new PointF(points[i].x + item_size_width, points[i].y + item_size_height);

				path.moveTo(top.x, top.y);
				path.lineTo(bottom_right.x, bottom_right.y);
				
//				path.moveTo(bottom_right.x, bottom_right.y);
				path.lineTo(bottom_left.x, bottom_left.y);
				
//				path.moveTo(bottom_left.x, bottom_left.y);
				path.lineTo(top.x, top.y);
				
				path.close();

				canvas.drawPath(path, paint);
				if (FILL_HALF == fill) {
					canvas.drawPath(path, shaderPaint);
				}
			}

		}

		// // Bitmap square = BitmapFactory.decodeResource(res,
		// R.drawable.square);
		// Bitmap circle = BitmapFactory.decodeResource(res,
		// R.drawable.circle_blue_empty);
		//
		// // if (number == 1) {
		// int left = (canvas.getWidth() - circle.getWidth()) / 2;
		// int top = (canvas.getHeight() - circle.getHeight()) / 2;
		// canvas.drawBitmap(Bitmap.createBitmap(circle, 0, 0,
		// circle.getWidth(), circle.getHeight(), matrix, false), 0, 0, null);
		// // }

		return canvas;
	}
	
	public boolean isEqual(Card c) {
		return c.toString().equals(toString());
	}

}
