package com.lib.play_rec.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	/**
	  * 获取现在时间
	  * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	  */
	public static String getStringDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}

	/** 获取当前时间毫秒数 */
	public static String getTimeMillisStr() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	public static long getTimeMillisLong() {
		return System.currentTimeMillis();
	}
	static String format;
	/** 把long行的时间转成mm:ss格式的时间字符串 */
	public static String longToDate(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		Date curDate = new Date(time);// 获取当前时间
		format = formatter.format(curDate);
		return format;
	}

}
