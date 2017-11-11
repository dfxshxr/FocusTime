package com.xidian.focustime.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.utils.SpUtil;
import com.xidian.focustime.utils.ToastUtil;

public class TomatoWakeUpActivity extends Activity {
    private Chronometer chronometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_tomato_wake_up);
        Button continueButton = (Button) findViewById(R.id.Btn_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("请关闭屏幕");
                finish();
            }
        });

        Button playButton = (Button) findViewById(R.id.Btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)&&SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,false)) {
                    SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,System.currentTimeMillis());
                    SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,false);
                    SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE,true);
                    ToastUtil.showToast("可以休息五分钟，关闭屏幕开始下轮计时");
                    finish();
                }else {
                    ToastUtil.showToast("不在学习中");
                }
            }
        });

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
            long lastSuccess = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS);
            long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            chronometer.setBase(lastSuccess - elapsedRealtimeOffset);
            chronometer.start();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }
}
