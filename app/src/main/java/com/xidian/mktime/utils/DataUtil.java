package com.xidian.mktime.utils;

import android.content.Context;
import android.text.TextUtils;

import com.xidian.mktime.bean.App;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class DataUtil {

    /**
     * 去除重复数据.
     */
    public static List<App> clearRepeatApp(List<App> lockInfos) {
        HashMap<String, App> hashMap = new HashMap<>();
        for (App lockInfo : lockInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<App> Apps = new ArrayList<>();
        for (HashMap.Entry<String, App> entry : hashMap.entrySet()) {
            Apps.add(entry.getValue());
        }
        return Apps;
    }

    /**
     * 将时间毫秒数转为分钟数
     */
    public static String timeParse(long duration) {
        String time = "" ;

        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;

        long second = Math.round((float)seconds/1000) ;

        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;

        if( second < 10 ){
            time += "0" ;
        }
        time += second ;

        return time ;
    }

    /**
     *
     * @return 将时间毫秒数转为小时分钟数
     */
    public static String timeParseInSetting(long mss) {

        long hours = (mss / (1000 * 60 * 60));
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        StringBuilder stringBuilder = new StringBuilder("");

        if(hours>0){
            stringBuilder.append(hours);
            stringBuilder.append("小时");
        }
        if(minutes>0||hours==0){
            stringBuilder.append(minutes);
            stringBuilder.append("分钟");
        }
    return new String(stringBuilder);
    }

    /**
     *
     * @return 将时间毫秒数转为小时分钟数
     */
    public static String timeParse2Minutes(long mss) {

        long minutes = (mss) / (1000 * 60);
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(minutes+1);
        stringBuilder.append("分钟");
        return new String(stringBuilder);
    }

    /**
     *
     * @return 将时间毫秒数转为小时分钟秒数
     */
    public static String timeParseInStatistics(long ms) {

        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuilder stringBuilder = new StringBuilder("");

        if(day>0){
            stringBuilder.append(day);
            stringBuilder.append("d");
        }

        if(hour>0){
            stringBuilder.append(hour);
            stringBuilder.append("h");
        }
        if(minute>0){
            stringBuilder.append(minute);
            stringBuilder.append("m");
        }
        if(second>=0){
            stringBuilder.append(second);
            stringBuilder.append("s");
        }
        return new String(stringBuilder);
    }

    public static long getStartTimeOfDay(long now) {
        TimeZone curTimeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 将毫秒时间转换成时分秒
     * @param totalTime
     * @return
     */
    public static String formatTime(long totalTime){
        long hour = 0;
        long minute = 0;
        long second = 0;
        second = totalTime / 1000;
        if (totalTime <= 1000 && totalTime > 0) {
            second = 1;
        }
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        // 转换时分秒 00:00:00
        String duration = (hour >= 10 ? hour : "0" + hour)+ ":" +(minute >= 10 ? minute : "0" + minute)+ ":" +(second >= 10 ? second : "0" + second);
        return duration;
    }
}
