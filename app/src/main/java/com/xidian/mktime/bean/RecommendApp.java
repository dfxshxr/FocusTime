package com.xidian.mktime.bean;

import org.litepal.crud.DataSupport;

/**
 * 用途
 *
 * @version V1.0 <推荐加锁的应用信息>
 * @FileName: com.xidian.qualitytime.bean.RecommendApp.java
 * @author: KIKI
 * @date: 2017-04-19 13:28
 */
public class RecommendApp extends DataSupport {
    public String packageName;

    public RecommendApp() {
    }

    public RecommendApp(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
