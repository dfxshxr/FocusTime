package com.xidian.focustime.receiver;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.LockApplication;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.db.AppManager;
import com.xidian.focustime.module.SplashActivity;
import com.xidian.focustime.module.TomatoWakeUpActivity;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.xidian.focustime.service.LockService.TOMATO_CYCLE_ACTION;

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
            case TOMATO_CYCLE_ACTION:
                KeyguardManager km = (KeyguardManager)LockApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
                LogUtils.i("收到唤醒广播"+km.inKeyguardRestrictedInputMode());
                if (km.inKeyguardRestrictedInputMode()) {
                    Intent alarmIntent = new Intent(LockApplication.getContext(), TomatoWakeUpActivity.class);
                    alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(alarmIntent);
                }
                break;
            case Intent.ACTION_SCREEN_OFF:
                //屏幕关闭的广播
                //记录屏幕关闭时间
                SpUtil.getInstance().putLong(AppConstants.LOCK_CURR_MILLISENCONS, System.currentTimeMillis());
                //锁屏的时候进行加锁 主锁true 运行锁false
                if (isLockOffScreen&&SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)&&!SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,false)) {
                    LockUtil.startLock();
                }
                break;
            case Intent.ACTION_SCREEN_ON:
                    SpUtil.getInstance().putBoolean(AppConstants.FIRST_PLAY_CYCLE, true);
                if (isLockOffScreen) {
                    NotifyUtil.updateNotify("专心学习中","专心学习中");
                    //解锁之后的第一个学习周期
                    /*if(LockUtil.getLauncherTopApp(context,activityManager).equals(AppConstants.APP_PACKAGE_NAME)&&
                            !LockUtil.getLauncherTopActivity(context,activityManager).equals(AppConstants.APP_PACKAGE_NAME+".module.LockActivity"))
                    {
                        LockApplication.getInstance().clearAllActivity();
                        intent=new Intent(context, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }*/
                }
                break;
            default:
        }
    }
}
