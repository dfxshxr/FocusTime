package com.xidian.mktime.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.apkfuns.logutils.LogUtils;
import com.xidian.mktime.R;
import com.xidian.mktime.bean.Monitor;
import com.xidian.mktime.module.SplashActivity;
import com.xidian.mktime.utils.DataUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonitorService extends Service {

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private Monitor monitor=new Monitor();

    public MonitorService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        monitor.setStartMilliseconds(System.currentTimeMillis());
        startForeground(101, getNotification("点我开始学习", ""));
        AsyncTask.SERIAL_EXECUTOR.execute(new ServiceWorker());
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try{
                    monitor.setEndMilliseconds(System.currentTimeMillis());
                    monitor.save();
                   // getNotificationManager().notify(1, getNotification("Monitoring...", ""));
                    LogUtils.i(DataUtil.timeParse(System.currentTimeMillis()));
                    Thread.sleep(30000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        monitor.setEndMilliseconds(System.currentTimeMillis());
        monitor.save();
        super.onDestroy();
    }


    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"status");
        builder.setOngoing(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        builder.setContentText(message);
        //builder.setContentText(new Date(System.currentTimeMillis()).toString());

        return builder.build();
    }





}
