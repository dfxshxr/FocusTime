package com.xidian.mktime;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.module.LockActivity;
import com.xidian.mktime.utils.NotifyUtil;
import com.xidian.mktime.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * 引入LitePal数据库
 *
 * @version V1.0

 */
public class LockApplication extends LitePalApplication {

    private static LockApplication application;
    private static List<BaseActivity> activityList; //acticity管理
    private int appCount = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SpUtil.getInstance().init(application);

        // 基础控件换肤初始化
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
          //      .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
          //      .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();

        activityList = new ArrayList<>();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);


        //注册通知渠道

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "status";
            String channelName = "学习状态";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotifyUtil.createNotificationChannel(channelId, channelName, importance);
            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotifyUtil.createNotificationChannel(channelId, channelName, importance);
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static LockApplication getInstance() {
        return application;
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean clearAllWhiteList(BaseActivity activity) {

        return activity instanceof LockActivity;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }
}
