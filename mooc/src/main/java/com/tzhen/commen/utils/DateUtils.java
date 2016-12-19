package com.tzhen.commen.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuyong on 16/12/19.
 */
public class DateUtils {

    public static String parseLongToyyyy_MM_dd(long timeInMills){
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        return format.format(new Date(timeInMills));
    }

    public static String parseLongToyyyy_MM_dd_HH_mm(long timeInMills){
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd HH:mm");
        return format.format(new Date(timeInMills));
    }

    public static String parseLongToHH_mm_ss(long timeInMills){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(timeInMills);
    }
}
