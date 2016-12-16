package com.lib.play_rec.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class ScreenManager {
	private static List<Activity> list; 
	private static ScreenManager instance;

	private ScreenManager() { }

	public static ScreenManager getInstance() {
		if (instance == null) {
			list=new LinkedList<Activity>();
			instance = new ScreenManager();
		}
		return instance;
	}

	/**  移除指定的Activity  */
	public void popActivity(Activity activity){ 
		if(activity!=null){ 
			activity.finish(); 
			list.remove(activity); 
			activity=null; 
		} 
	} 
	
	/**   添加一个Activity到堆栈中  */
	public void pushActivity(Activity activity) {
		list.add(activity);
	}

	/**  移除指定名称的Activity  */
	public void popActivity(Class<?> cls){
		Activity activity = null;
		for(int i=0;i<list.size();i++){
			activity = list.get(i);
			if(activity==null){ 
				continue; 
			} 
			if(activity.getClass().equals(cls)){ 
				popActivity(activity); 
			} 
		}
	}
	
	/**  移除List集合中指定名称的Activity  */
	public void popActivityList(List<Class<?>> cls_list){
		Activity activity = null;
		for(int i=0;i<list.size();i++){
			activity = list.get(i);
			if(activity==null){ 
				continue; 
			} 
			if(cls_list.indexOf(activity.getClass())!=-1){ 
				popActivity(activity); 
			} 
		}
	}
	
	/**   移除所有Activity  */   
	public void popAllActivity(){   
		Activity activity = null;
		for(int i=0;i<list.size();i++){
			activity = list.get(i);
			popActivity(activity);
		}
		list.clear();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);   
	}  
	
	/**  移除除指定Activity以外的Activity  */
	public void popAllExceptOne(Class<?> cls){   
		Activity activity = null;
		for(int i=0;i<list.size();i++){
			activity = list.get(i);
			if(!activity.getClass().equals(cls)){ 
				popActivity(activity); 
			} 
		}
	}  
	
}