package com.xidian.mktime.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.xidian.mktime.LockApplication;
import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.module.SplashActivity;
import com.xidian.mktime.module.TomatoWakeUpActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotifyUtil {

    public static void stopServiceNotify(Context context) {
        Intent intent=new Intent(context, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(context,"status")
                .setContentTitle(context.getString(R.string.lock_stop))
                .setTicker(context.getString(R.string.lock_stop))
                .setContentText(context.getString(R.string.lock_stop))
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setDefaults(Notification.FLAG_NO_CLEAR)
                .setContentIntent(pi)
                .build();

        manager.notify(AppConstants.NOTIFICATION_ID.SERVICE,
                notification);
    }

    public static void startAccessibilityServiceNotify(Context context) {
        Intent intent=new Intent(context, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(context,"status")
                .setContentTitle(context.getString(R.string.lock_start))
                .setTicker(context.getString(R.string.lock_start_advanced_model))
                .setContentText(context.getString(R.string.advanced_model))
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setContentIntent(pi)
                .setDefaults(Notification.FLAG_NO_CLEAR)
                .build();

        manager.notify(AppConstants.NOTIFICATION_ID.SERVICE,
                notification);
    }

    public static void updateNotify(String Title,String message) {
        Context context= LockApplication.getContext();
        Intent intent=new Intent(context, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(context,"status")
                .setContentTitle(Title)
           //     .setTicker(message)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
               // .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setContentIntent(pi)
                .setDefaults(Notification.FLAG_NO_CLEAR)
                .build();

        manager.notify(AppConstants.NOTIFICATION_ID.SERVICE,
                notification);
    }

    public static void tomatoNotify(Context context) {
        Intent intent=new Intent(context, TomatoWakeUpActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"status");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText( "是否需要休息一下" )
                // 点击消失
                .setAutoCancel( true )
               // .setLargeIcon( Bitmap.createScaledBitmap(icon, 128, 128, false) )
                .setTicker("您已完成一个番茄时间周期")
                .setContentIntent(pi)
                // 通知首次出现在通知栏，带上升动画效果的
                .setWhen( System.currentTimeMillis() );
        //在5.0版本之后，可以支持在锁屏界面显示notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }

        Notification notification  = mBuilder.build();
        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(AppConstants.TOMATO_NOTIFICATION_ID.SERVICE,
                notification);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) LockApplication.getContext().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}
