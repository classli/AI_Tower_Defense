package com.ai_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Callback,Runnable{

	private Paint paint;
	private Canvas canvas;
	private SurfaceHolder sfh;
	private Thread thd;
	private boolean flags;
	private Map map;
	public GameLogic game;
	Bitmap bmpStart;
	Bitmap bmpEnd;
	
	public MySurfaceView(Context context) {
		super(context);
		sfh=this.getHolder();
		sfh.addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		setKeepScreenOn(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		//===================//
		map=new Map();
		game=new GameLogic();
		bmpStart=BitmapFactory.decodeResource(getResources(), R.drawable.source);
		bmpEnd=BitmapFactory.decodeResource(getResources(), R.drawable.target);
		
	}

	
	@Override
	public void run() {
		// TODO run-generated method stub
		while(flags){
			long start=System.currentTimeMillis();
			myDraw();
			logic();
			long end=System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace(System.out);
			}
		}
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO surfaceCreated-generated method stub
		flags=true;
		thd=new Thread(this);
		thd.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO surfaceChanged-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO surfaceDestroyed-generated method stub
		flags=false;
	}

	public void myDraw(){
		try {
			canvas=sfh.lockCanvas();
			if(canvas!=null){
				canvas.drawARGB(255, 128, 128, 128);
				map.MapDraw(canvas, paint);
				canvas.drawBitmap(bmpStart, game.source[0]*(Map.span+1)+2,game.source[1]*(Map.span+1)+2 , paint);
				canvas.drawBitmap(bmpEnd, game.target[0]*(Map.span+1)+2,game.target[1]*(Map.span+1)+2 , paint);
				game.draw(canvas,paint);
				
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}finally{
			if(canvas!=null)
				sfh.unlockCanvasAndPost(canvas);
		}
		
	}
	
	public void logic(){
		
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO onTouchEvent-generated method stub
		int x=(int)event.getX();
		int y=(int)event.getY();
		
		if(event.getAction()==event.ACTION_DOWN){
			int col=(int) ((int)(x-2)/(Map.span+1));
			int row=(int)((y-2)/(Map.span+1));
			if(MapList.map[0][row][col]==0){
				MapList.map[0][row][col]=1;
			}
			else{
				MapList.map[0][row][col]=0;
			}
		}
		
		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			MainActivity.instance.finish();
			System.exit(0);
		}
		return true;
	}
	
	
}