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
    public static final String TOMATO_CYCLE_ACTION = "TOMATO_CYCLE_ACTION";

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
        filter.addAction(Intent.ACTION_SCREEN_ON);//开屏
        filter.addAction(Intent.ACTION_SCREEN_OFF);//关屏
        filter.addAction(Intent.ACTION_USER_PRESENT);//解锁
        filter.addAction(UNLOCK_ACTION);
        filter.addAction(TOMATO_CYCLE_ACTION);
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

        Notification notification = new NotificationCompat.Builder(this,"status")
                .setContentTitle(getString(R.string.lock_start))
                .setTicker(getString(R.string.lock_start_cycle_model))
                .setContentText(getString(R.string.cycle_model))
                .setSmallIcon(R.mipmap.ic_launcher)
            //    .setLargeIcon( Bitmap.createScaledBitmap(icon, 128, 128, false))
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
            Long continueTime =SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);

            Long startPlayTime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,currentTime);
            Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);

            Long totalPlayTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0);

            lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false);
            runLockState=SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,true);
            try {

                //番茄学习法
                int tomatoCycle=SpUtil.getInstance().getInt(AppConstants.TOMATO_STUDY_CYCLE,1);
                Long tomatoTime=SpUtil.getInstance().getLong(AppConstants.TOMATO_TIME,1000*60*25);
                Long tomatoStartTime=SpUtil.getInstance().getLong(AppConstants.TOMATO_START_TIME,currentTime);
                Boolean tomatoBreakStatus=SpUtil.getInstance().getBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE,false);

                if (lockState && runLockState && !tomatoBreakStatus && currentTime-tomatoStartTime >tomatoTime)
                {//锁定 运行锁 非番茄状态
                    SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE,true);
                    LogUtils.i("唤醒屏幕"+(currentTime-tomatoStartTime)/1000);
                    Intent tomatoIntent =new Intent(TOMATO_CYCLE_ACTION);
                    sendBroadcast(tomatoIntent);
                    SpUtil.getInstance().putInt(AppConstants.TOMATO_STUDY_CYCLE,tomatoCycle+1);
                }

                //应用状态检测
                Long lastnormalTime=SpUtil.getInstance().getLong(AppConstants.LAST_NORMAL_STATE_MILLISENCONS,currentTime);
                Long timeIncrement=currentTime-lastnormalTime;
                if(timeIncrement>1000*10){
                    SpUtil.getInstance().putLong(
                            AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,
                            SpUtil.getInstance().getLong(AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,0)+timeIncrement
                    );
                }
                SpUtil.getInstance().putLong(AppConstants.LAST_NORMAL_STATE_MILLISENCONS,currentTime);
                Long totalErrorTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,0);

                //判断包名打开解锁页面
                if (lockState ) {//&& !TextUtils.isEmpty(packageName)

                    //savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, ""); //上次解锁的应用包名
                    //LogUtils.i("packageName = " + packageName + "  savePkgName = " + savePkgName+"  activity = "+LockUtil.getLauncherTopActivity(this,activityManager));

                    if(!runLockState){
                        //休息中
                        if(tomatoBreakStatus){
                            //番茄法
                            Long tomatoBreakTime=SpUtil.getInstance().getLong(AppConstants.TOMATO_BREAK_TIME,1000*60*5);
                            if (currentTime - startPlayTime > tomatoBreakTime) {
                                LockUtil.startLock();
                            }else {
                                NotifyUtil.updateNotify("休息中","休息时间还有"+(tomatoBreakTime-currentTime+startPlayTime)/1000+"秒");
                            }
                        }else {
                            //一般
                            if (currentTime - startPlayTime > remainPlaytime) {
                                LockUtil.startLock();
                                //当前玩耍时间大于2 不处于番茄休息状态 休息时间强制归4
                                if (currentTime - startPlayTime > 1000 * 60 * 2) {
                                    SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS, 1000 * 60 * 4);
                                }
                            }else {
                                NotifyUtil.updateNotify("愉快玩耍中","可玩时间还有"+(remainPlaytime-currentTime+startPlayTime)/1000+"秒");
                            }
                        }
                    }else {
                        //学习中
                        NotifyUtil.updateNotify("专心学习中","已学习"+(currentTime-startTime-totalPlayTime-totalErrorTime)/1000/60+"分钟,连续"+(currentTime-tomatoStartTime)/1000/60+"分钟");
                    }

                    /*//返回桌面加锁
                    if (!savePkgName.equals(packageName)) {
                        if (LockUtil.getHomes(this).contains(packageName) || packageName.contains("launcher")) {
                            SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,true);
                            runLockState=true;
                            LockApplication.getInstance().clearAllActivity();
                        }
                    }*/
                    LogUtils.i("当前包名:"+packageName);
                    // 如果是锁定状态，转向解锁页面
                    if ((!TextUtils.isEmpty(packageName)||mAppManager.isLockedPackageName(packageName)||LockUtil.inBlackList(packageName))&&SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,true)&&!LockUtil.inWhiteList(packageName)) {

                        LogUtils.i("后台跳转:"+packageName);
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

