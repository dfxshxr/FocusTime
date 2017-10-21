package com.xidian.qualitytime.service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.xidian.qualitytime.LockApplication;
import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.db.AppManager;
import com.xidian.qualitytime.module.SplashActivity;
import com.xidian.qualitytime.receiver.ServiceReceiver;
import com.xidian.qualitytime.utils.LockUtil;
import com.apkfuns.logutils.LogUtils;
import com.xidian.qualitytime.utils.SpUtil;

public class AdvancedLockService extends AccessibilityService {

    private final static String TAG = "AdvancedLockService";
    private static AdvancedLockService mInstance = null;


    private boolean lockState;
    private boolean runLockState;
    private AppManager mAppManager;
    private ActivityManager activityManager;
    public String savePkgName;

    public static final String UNLOCK_ACTION = "UNLOCK_ACTION";

    private ServiceReceiver mServiceReceiver;

    @Override
    protected  void onServiceConnected() {
        Intent stopIntent = new Intent(this, LockService.class);
        stopService(stopIntent);
        LogUtils.i("onServiceConnected");

        //注册广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(UNLOCK_ACTION);
        registerReceiver(mServiceReceiver, filter);


        lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false);
        mAppManager = new AppManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Intent intent=new Intent(this, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this,0,intent,0);

        Notification notification;
        if(lockState){
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.lock_start))
                    .setTicker(getString(R.string.lock_start_advanced_model))
                    .setContentText(getString(R.string.advanced_model))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setOngoing(true)
                    .setContentIntent(pi)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.lock_stop))
                    .setTicker(getString(R.string.lock_stop))
                    .setContentText(getString(R.string.lock_stop))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setOngoing(true)
                    .setContentIntent(pi)
                    .build();
        }


        startForeground(AppConstants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);


        super.onServiceConnected();
    }


    /**
     * 监听窗口焦点,并且获取焦点窗口的包名
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


        LogUtils.i("onAccessibilityEvent");
        lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false);
        runLockState=SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,true);

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED&&lockState){
            LogUtils.i("你打开了："+event.getPackageName());
            PowerManager manager = (PowerManager) this.getSystemService(POWER_SERVICE);
            String packageName = event.getPackageName().toString();

            if (manager.isScreenOn()){
                Log.d(TAG,"开启屏幕");
                try {

                    //判断包名打开解锁页面
                    if (!LockUtil.inWhiteList(packageName) && !TextUtils.isEmpty(packageName)) {

                        savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, ""); //上次解锁的应用包名
                        LogUtils.i("packageName = " + packageName + "  savePkgName = " + savePkgName);

                        //返回桌面加锁

                            if (!savePkgName.equals(packageName)) {
                                if (LockUtil.getHomes(this).contains(packageName) || packageName.contains("launcher")) {
                                    SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);
                                    runLockState=true;
                                    LockApplication.getInstance().clearAllActivity();
                                }
                            }


                        /*
                        if(runLockState)
                        {
                            LogUtils.i("锁定状态");
                        }else{
                            LogUtils.i("解锁状态");
                        }*/
                        if ((mAppManager.isLockedPackageName(packageName)|| LockUtil.inBlackList(packageName))&&runLockState) {
                            LockUtil.gotoUnlock(this,packageName);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG,"关闭屏幕");
            }

        }
    }






    @Override
    public void onInterrupt() {

        LogUtils.i("onInterrupt");
    }


    /**
     * 服务断开
     */
    @Override
    public boolean onUnbind(Intent intent)
    {
        LogUtils.i("onUnbind");
        Intent startIntent = new Intent(this, LockService.class);
        startService(startIntent);
        return super.onUnbind(intent);
    }




/*
    public static void disableAdvancedLockService(){

        Intent intent = new Intent(this, LockService.class);
        startService(intent);

        if (Build.VERSION.SDK_INT >= 24)
        {
            disableSelf();
        }
        else{
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }

    }
*/

}
