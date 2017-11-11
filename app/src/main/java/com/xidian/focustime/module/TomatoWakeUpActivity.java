package com.xidian.focustime.module;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.xidian.focustime.R;
import com.xidian.focustime.base.BaseActivity;

public class TomatoWakeUpActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_tomato_wake_up;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initAction() {
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
