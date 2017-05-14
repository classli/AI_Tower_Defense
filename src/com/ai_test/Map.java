package com.ai_test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Map{
	
	static final float span=36.7f;//矩形大小
	private int [][] map;
	private int row;
	private int col;
	private int width;
	private int hight;
	
	public Map(){
		map=MapList.map[0];
		row=map.length;//行
		col=map[0].length;//列
		width=(int)span*row;
		hight=(int)span*col;
	}
	
	public void MapDraw(Canvas canvas,Paint paint){
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				if(map[i][j]==1){
					paint.setColor(Color.BLACK);
				}else if(map[i][j]==0){
					paint.setColor(Color.WHITE);
				}
				canvas.drawRect(2+j*(span+1), 2+i*(span+1), 2+j*(span+1)+span, 2+i*(span+1)+span, paint);
			}
		}
		
	}
	
}