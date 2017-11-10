package com.xidian.focustime.utils;

import com.xidian.focustime.bean.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



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

}
