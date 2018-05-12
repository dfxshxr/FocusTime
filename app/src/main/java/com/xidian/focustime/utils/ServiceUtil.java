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


public class ServiceUtil {
    /**
     * 启动前台服务
     *
     * @param intent
     * @return boolean
     */
    public static void startForegroundService(Context context,Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
