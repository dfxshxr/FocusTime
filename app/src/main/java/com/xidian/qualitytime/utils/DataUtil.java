package com.xidian.qualitytime.utils;

import com.xidian.qualitytime.bean.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 去除重复数据.
 */

public class DataUtil {

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

}
