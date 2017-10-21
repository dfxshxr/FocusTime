package com.xidian.qualitytime.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xidian.qualitytime.LockApplication;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.db.AppManager;
import com.xidian.qualitytime.module.SplashActivity;
import com.xidian.qualitytime.utils.LockUtil;
import com.xidian.qualitytime.utils.NotifyUtil;
import com.xidian.qualitytime.utils.SpUtil;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 用途
 *
 * @version V1.0 <服务广播>
 * @FileName: com.xidian.qualitytime.receiver.ServiceReceiver.java
 * @author: KIKI
 * @date: 2017-04-19 12:46
 */
public class ServiceReceiver extends BroadcastReceiver {

    private AppManager mAppManager;
    private ActivityManager activityManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        mAppManager = new AppManager(context);
        activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        String action = intent.getAction();

        boolean isLockOffScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, true); //是否在手机屏幕关闭后再次锁定

        switch (action) {
            case Intent.ACTION_SCREEN_OFF: //屏幕关闭的广播
                SpUtil.getInstance().putLong(AppConstants.LOCK_CURR_MILLISENCONS, System.currentTimeMillis()); //记录屏幕关闭时间
                //锁屏的时候进行加锁
                if (isLockOffScreen) {
                    Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);
                    SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,remainPlaytime/2);
                    SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);
                }
                break;
            case Intent.ACTION_SCREEN_ON:
                if (isLockOffScreen) {
                    NotifyUtil.updateNotify("专心学习中","专心学习中");
                    if(LockUtil.getLauncherTopApp(context,activityManager).equals(AppConstants.APP_PACKAGE_NAME)&&
                            !LockUtil.getLauncherTopActivity(context,activityManager).equals(AppConstants.APP_PACKAGE_NAME+".module.LoginActivity"))
                    {
                        LockApplication.getInstance().clearAllActivity();
                        intent=new Intent(context, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
                break;
        }
    }
}
