package com.xidian.qualitytime.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.xidian.qualitytime.LockApplication;
import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.module.SplashActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotifyUtil {

    public static void stopServiceNotify(Context context) {
        Intent intent=new Intent(context, SplashActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.lock_stop))
                .setTicker(context.getString(R.string.lock_stop))
                .setContentText(context.getString(R.string.lock_stop))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
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
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.lock_start))
                .setTicker(context.getString(R.string.lock_start_advanced_model))
                .setContentText(context.getString(R.string.advanced_model))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setContentIntent(pi)
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
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(Title)
           /*     .setTicker(message)*/
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setContentIntent(pi)
                .build();

        manager.notify(AppConstants.NOTIFICATION_ID.SERVICE,
                notification);
    }

}
