package com.lib.play_rec.entity;

import java.io.File;

import android.app.Activity;
import android.os.Environment;
import android.util.DisplayMetrics;

public class GlobalInit {
	
	private static GlobalInit instance = null;
	private int screenWidth,screenHeight;  // 屏幕宽度和高度
	private int densityDpi;//屏幕密度DPI
	private float screenDensity;  // 屏幕密度
	private float scaledDensity;
	private String projectPath;  // 工程对应SD卡的路径
	private String personalPath;  //个人视频根目录
	private String tempPath;
	/** 视频信息   */
	private WorksBean videoBean;

	public static GlobalInit getInstance(Activity context){
		if(instance == null){
			instance = new GlobalInit(context);
		}
		return instance;
	}
	
	public static GlobalInit getInstance(){
		return instance;
	}
	
	private GlobalInit(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		screenDensity = dm.density;
		scaledDensity = dm.scaledDensity;
//		this.context = context;
		setDensityDpi(dm.densityDpi);
		
		// 确保宽度>=高度
		if(screenWidth<=screenHeight){
			int temp = screenWidth;
			screenWidth = screenHeight;
			screenHeight = temp;
		}
		
		projectPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator+ context.getPackageName() +File.separator;
		File f = new File(projectPath);
		if(!f.exists()){
			f.mkdir();
		}
		personalPath = projectPath+"personal"+File.separator;
		f = new File(personalPath);
		if(!f.exists()){
			f.mkdir();
		}
		
		tempPath = projectPath+"temp"+File.separator;
		f = new File(tempPath);
		if(!f.exists()){
			f.mkdir();
		}
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public int dip2px(float dpValue) {  
        return (int) (dpValue * screenDensity + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public int px2dip(float pxValue) {  
        return (int) (pxValue / screenDensity + 0.5f);  
    }  
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public float getScreenDensity() {
		return screenDensity;
	}

	public void setScreenDensity(float screenDensity) {
		this.screenDensity = screenDensity;
	}
	
	public float getScaledDensity() {
		return scaledDensity;
	}

	public void setScaledDensity(float scaledDensity) {
		this.scaledDensity = scaledDensity;
	}

	public String getPersonalPath() {
		return personalPath;
	}

	public void setPersonalPath(String personalPath) {
		this.personalPath = personalPath;
	}
	public String getTempPath() {
		return tempPath;
	}
	public WorksBean getVideoBean() {
		return videoBean;
	}

	public void setVideoBean(WorksBean videoBean) {
		this.videoBean = videoBean;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}
}
