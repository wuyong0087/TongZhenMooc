package com.lib.play_rec.rec;

import java.io.Serializable;

public class PenDigitalBean implements Serializable{

	private static final long serialVersionUID = -7445909216349496484L;
	
	public float paperWidth = 13000;
	public float paperHeight = 11000;
	public float paperStartX = -5500;
	public float paperStartY = 0;
	public float paperEndX = 7500;
	public float paperEndY = 11000;
	//public float scale=1;
	public int paperType = 1;
	private float scaleX = 1;
	private float scaleY = 1;

	public float screenWidth;
	public float screenHeight;
	
	public PenDigitalBean(){
		calculateWH();
	}
	
	/**  初始化值   */
	public void initVal(float startX,float startY,float endX,float endY){
		paperStartX = startX;
		paperStartY = startY;
		paperEndX = endX;
		paperEndY = endY;
		calculateWH();
	}
	
	/**  计算纸张可绘区域的宽高    */
	public void calculateWH(){
		 if (((paperStartX-paperEndX) * (paperStartY-paperEndY)) > 0) {
			 paperType = 1;
			 paperWidth = Math.abs(paperEndX-paperStartX);
			 paperHeight = Math.abs(paperEndY-paperStartY);
         } else {
        	 paperType = 2;
        	 paperHeight = Math.abs(paperEndX-paperStartX);
             paperWidth = Math.abs(paperEndY-paperStartY);
         }
	}
	
	// 计算缩放比例
	public void calculateScale(float sw,float sh){
		if(paperType==1){
			scaleX = sw/(paperEndX - paperStartX);
			scaleY = sh/(paperEndY - paperStartY);
		}else{
			scaleX = sh/(paperEndX - paperStartX);
			scaleY = sw/(paperEndY - paperStartY);
		}
	}
	
	// 坐标值的缩放
	public float calculatePointX(long point){
		return (point-paperStartX)*scaleX;
	}
	public float calculatePointY(long point){
		return (point-paperStartY)*scaleY;
	}
	
}
