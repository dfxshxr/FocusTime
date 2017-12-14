package com.xidian.focustime;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.module.LockActivity;
import com.xidian.focustime.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

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
        activityList = new ArrayList<>();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);

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
