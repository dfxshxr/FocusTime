package com.xidian.focustime.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.LockApplication;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.module.LockActivity;
import com.xidian.focustime.module.ResultActivity;

import java.util.ArrayList;
import java.util.List;


public class LockUtil {
    /**
     * 判断是否已经获取 有权查看使用情况的应用程序 权限
     *
     * @param context
     * @return boolean
     */
    public static boolean isStatAccessPermissionSet(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 查看是存在查看使用情况的应用程序界面
     *
     * @return boolean
     */
    public static boolean isNoOption(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 是否有开启通知栏服务
     */
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    public static boolean isNotificationSettingOn(Context mContext) {
        String pkgName = mContext.getPackageName();
        final String flat = Settings.Secure.getString(mContext.getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Home键操作
     */
    public static void goHome(BaseActivity activity) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(homeIntent);
        activity.finish();
    }


    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     * @param context context
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context context){
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1){
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null){
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    /**
     * 获取栈顶应用包名
     */
    public static String getLauncherTopApp(Context context, ActivityManager activityManager) {

        //LogUtil.d("当前版本号"+Integer.toString(Build.VERSION.SDK_INT));
        String result = "";
        if (Build.VERSION.SDK_INT < 21) {
            // LogUtil.d("小于5.0");
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                result= appTasks.get(0).topActivity.getPackageName();
            }
        } else if(isStatAccessPermissionSet(LockApplication.getContext())) {
            /*
            使用它之前需要在清单文件中配置 “android.permission.PACKAGE_USAGE_STATS”的权限
            用户必须在 设置–安全–有权查看使用情况的应用 中勾选相应的应用
            对应设备 Android 5.0 及其以上。
             */
            //LogUtil.d("大于5.0");
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();

                    // LogUtil.d(result);
                }
            }
        }else{
            LogUtils.i("这是当前应用："+getApplicationValue((LockApplication) ((Service) context).getApplication()));
            if(getApplicationValue((LockApplication) ((Service) context).getApplication())){
                result=AppConstants.APP_PACKAGE_NAME;

            }else {
                result="black";
            }
        }

        if (!android.text.TextUtils.isEmpty(result)) {

            return result;
        }

        return "";
    }


    public static String getLauncherTopActivity(Context context, ActivityManager activityManager) {

        //LogUtil.d("当前版本号"+Integer.toString(Build.VERSION.SDK_INT));
        String result = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // LogUtil.d("小于5.0");
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                result = appTasks.get(0).topActivity.getClassName();
            }
        } else {
            /*
            使用它之前需要在清单文件中配置 “android.permission.PACKAGE_USAGE_STATS”的权限
            用户必须在 设置–安全–有权查看使用情况的应用 中勾选相应的应用
            对应设备 Android 5.0 及其以上。
             */
            //LogUtil.d("大于5.0");
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getClassName();

                    // LogUtil.d(result);
                }
            }
        }
        if (!android.text.TextUtils.isEmpty(result)) {

            return result;
        }
        return "";
    }

    /**
     * 白名单
     */
    public static boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.APP_PACKAGE_NAME+".debug")||packageName.equals(AppConstants.APP_PACKAGE_NAME)||packageName.contains("launcher");
    }
    /**
    黑名单
     */
    public static boolean inBlackList(String packageName) {
        if(packageName.equals("black")){
            return true;
        }
        if(packageName.equals("com.android.packageinstaller")&&SpUtil.getInstance().getBoolean(AppConstants.LOCK_INSTALL, false))
        {return true;}
        return false;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     */
    public static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            if(!ri.activityInfo.packageName.equals("com.android.setting")) //为什么系统设置菜单也会被算在里面
            {names.add(ri.activityInfo.packageName);}
        }
        return names;
    }


    /**
     * 转到解锁界面
     */
    public static void gotoUnlock(Context context, String packageName) {
        LockApplication.getInstance().clearAllActivity();
        Intent intent = new Intent(context, LockActivity.class);

        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 转到结束界面
     */
    public static void gotoResult(Context context, Boolean result) {
        Intent intent = new Intent(LockApplication.getContext(), ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 开始学习/锁定
     */
    public static void startLock(){

        Long currentTime =System.currentTimeMillis();
        Long startPlayTime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,0);

        LogUtils.i("当前时间：" + currentTime + "开始时间：" + startPlayTime + "时间差：" + (currentTime - startPlayTime));

        SpUtil.getInstance().putLong(AppConstants.TOMATO_START_TIME,currentTime);
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE, true);
        SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE, false);

        //记录已玩时间
        SpUtil.getInstance().putLong(AppConstants.TOTAL_PLAY_MILLISENCONS, SpUtil.getInstance().getLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0)+(currentTime - startPlayTime));
        LogUtils.i(SpUtil.getInstance().getLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0));
        LockApplication.getInstance().clearAllActivity();

        //当前玩耍时间大于2 不处于番茄休息状态 currentTime - startPlayTime > 1000 * 60 * 2||
        if ( SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS, 0)<1000*60*4) {
            SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS, 1000 * 60 * 4);
        }

    }

    public static boolean getApplicationValue(LockApplication myApplication) {
        return myApplication.getAppCount() > 0;
    }
}
