package com.xidian.focustime.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.db.AppManager;
import com.xidian.focustime.module.SplashActivity;
import com.xidian.focustime.receiver.ServiceReceiver;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;


/**
 * 后台监控服务
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.xidian.qualitytime.service.LockService.java
 * @author: KIKI
 * @date: 2017-03-06 17:20
 */
public class LockService extends IntentService {
    public LockService() {
        super("LockService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public boolean threadIsTerminate = false; //是否开启循环

    public static final String UNLOCK_ACTION = "UNLOCK_ACTION";

    private boolean lockState;
    private boolean runLockState;


    private ServiceReceiver mServiceReceiver;
    private AppManager mAppManager;
    private ActivityManager activityManager;

    public String savePkgName;

    @Override
    public void onCreate() {
        super.onCreate();


        lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false);

        //SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);


        mAppManager = new AppManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //注册广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(UNLOCK_ACTION);
        registerReceiver(mServiceReceiver, filter);



        //如果开启了辅助功能，或处于未加锁状态 关闭服务
        if(LockUtil.isAccessibilitySettingsOn(this)){
            NotifyUtil.startAccessibilityServiceNotify(this);
            stopSelf();
        }
        //如果处于未加锁状态 关闭服务
        if(!lockState){
            NotifyUtil.stopServiceNotify(this);
            stopSelf();
        }

        //开启一个检查锁屏的线程
        threadIsTerminate = true;

        Intent intent=new Intent(this, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this,0,intent,0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.lock_start))
                .setTicker(getString(R.string.lock_start_cycle_model))
                .setContentText(getString(R.string.cycle_model))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setContentIntent(pi)
                .build();

        startForeground(AppConstants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        checkData();
    }

    private void checkData() {
        while (threadIsTerminate) {
            //获取栈顶app的包名
            String packageName = LockUtil.getLauncherTopApp(this, activityManager);

            Long currentTime =System.currentTimeMillis();
            Long startTime = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS,0);
            Long continueTime =SpUtil.getInstance().getLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,1000*60*60*2);

            Long startPlayTime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,0);
            Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);
            //任务结束返回结果页面
            if(lockState &&(currentTime-startTime>continueTime)){//||remainPlaytime<1000*60*2
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE,false);
                lockState =false;
                NotifyUtil.stopServiceNotify(this);
                if(currentTime-startTime>continueTime){
                    LockUtil.gotoResult(LockApplication.getContext(),true);//任务成功
                }else{
                    LockUtil.gotoResult(LockApplication.getContext(),false);//任务失败
                }
            }
            runLockState=SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,true);
            try {
                //判断包名打开解锁页面
                if (lockState && !LockUtil.inWhiteList(packageName) && !TextUtils.isEmpty(packageName)) {

                    savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, ""); //上次解锁的应用包名
                    LogUtils.i("packageName = " + packageName + "  savePkgName = " + savePkgName+"  activity = "+LockUtil.getLauncherTopActivity(this,activityManager));

                    //超过时长加锁
                    if(currentTime-startPlayTime>remainPlaytime){
                        LogUtils.i("当前时间："+currentTime+"开始时间："+startPlayTime+"时间差："+(currentTime-startPlayTime)+"剩余时间："+remainPlaytime);
                        if(!runLockState){
                            SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);
                            runLockState=true;
                            LockApplication.getInstance().clearAllActivity();
                            SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,remainPlaytime/2);
                        }
                        NotifyUtil.updateNotify("专心学习中","学习时间还有"+(continueTime-currentTime+startTime)/1000+"秒");
                    }else {
                        if(!runLockState){
                            NotifyUtil.updateNotify("愉快玩耍中","可玩时间还有"+(remainPlaytime-currentTime+startPlayTime)/1000+"秒");
                        }else {
                            NotifyUtil.updateNotify("专心学习中","学习时间还有"+(continueTime-currentTime+startTime)/1000+"秒");
                        }
                    }

                    /*//返回桌面加锁
                    if (!savePkgName.equals(packageName)) {
                        if (LockUtil.getHomes(this).contains(packageName) || packageName.contains("launcher")) {
                            SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);
                            runLockState=true;
                            LockApplication.getInstance().clearAllActivity();
                        }
                    }*/
                    // 如果是锁定状态，转向解锁页面
                    if ((mAppManager.isLockedPackageName(packageName)||LockUtil.inBlackList(packageName))&&runLockState) {

                        LogUtils.i("后台跳转");
                        LockUtil.gotoUnlock(this,packageName);
                        continue;
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        threadIsTerminate = false;
        unregisterReceiver(mServiceReceiver);

        stopForeground(true);
        stopSelf();
    }

}

