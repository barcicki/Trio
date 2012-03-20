package com.barcicki.trio;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.barcicki.trio.graphics.TrioDrawing;

public class TrioSurface extends SurfaceView implements Runnable {

	private Thread thread = null;
	private SurfaceHolder holder;
	private volatile boolean running = false;
	
	private Resources res;
	private ArrayList<TrioDrawing> drawings = new ArrayList<TrioDrawing>();
		
	public TrioSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = getHolder();
		res = context.getResources();
	}
	
	public TrioSurface(Context context) {
		super(context);
		holder = getHolder();
		res = context.getResources();
	}

	public TrioSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		res = context.getResources();
	}
	
	public void addDrawing(TrioDrawing drawing) {
		drawings.add(drawing);
	}
	
	public void onResumeTrioSurface() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void onPauseTrioSurface() {
		boolean retry = true;
		running = false;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while(running) {
			
//			for (TrioDrawing drawing : drawings) {
//				if (!drawing.isInitialized()) drawing.initialize(res); 
//			}
			
			if (holder.getSurface().isValid()) {
				Canvas canvas = holder.lockCanvas();
				int width = canvas.getWidth();
				int height = canvas.getHeight();

				for (TrioDrawing drawing : drawings) {
					drawing.onDraw(canvas); 
				}
				
				holder.unlockCanvasAndPost(canvas);
			}
			
		}
		
	}
	
	private void drawBackground(Canvas canvas) {
		
	}
	
	private void drawClouds(Canvas canvas) {
		
	}

}
