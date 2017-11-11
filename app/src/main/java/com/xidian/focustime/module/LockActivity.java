package com.xidian.focustime.module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;

import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.db.AppManager;
import com.xidian.focustime.service.LockService;
import com.xidian.focustime.utils.DataUtil;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;
import com.xidian.focustime.utils.ToastUtil;


/**
 * A login screen that offers login via email/password.
 */
public class LockActivity extends BaseActivity implements DialogInterface.OnDismissListener{

    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private String pkgName; //解锁应用的包名
    private String actionFrom;//按返回键的操作
    private AppManager mAppManager;

    private Chronometer chronometer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        Button mOpenAccessButton = (Button) findViewById(R.id.open_access_button);
        mOpenAccessButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAccessPermission();
            }
        });

        Button mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    ToastUtil.showToast("已在学习中");
                }else {
                    startLockService();
                }
            }
        });

        Button mPlayButton = (Button) findViewById(R.id.paly_button);
        mPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)&&SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,false)) {
                    allowPlay();
                }else {
                    ToastUtil.showToast("不在学习中");
                }
            }
        });

        Button mCancleButton = (Button) findViewById(R.id.cancle_button);
        mCancleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    cancleLockService();
                }else {
                    ToastUtil.showToast("不在学习中");
                }

            }
        });

        Button mMenuButton = (Button) findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    ToastUtil.showToast("学习中不能修改设置");
                }else {
                    Intent intent = new Intent(LockActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button mAdvancedButton = (Button) findViewById(R.id.advanced_menu_button);
        mAdvancedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    ToastUtil.showToast("学习中不能修改设置");
                }else {
                    Intent intent = new Intent(LockActivity.this, AdvancedSettingActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void initData() {
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        //获取按返回键的操作
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        mAppManager = new AppManager(this);

        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
            long lastSuccess = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS);
            long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            chronometer.setBase(lastSuccess - elapsedRealtimeOffset);
            chronometer.start();
        }

    }

    @Override
    protected void initAction() {
    }


    /**
     * 修改返回键功能
     */
    @Override
    public void onBackPressed() {
        LockUtil.goHome(this);
    }


    /**
     * 启动后台监测正在运行的程序服务
     */
    public void startLockService() {
        //初始化变量
        SpUtil.getInstance().putLong(AppConstants.LOCK_START_MILLISENCONS, System.currentTimeMillis());
        SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10));
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE, true);
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
        SpUtil.getInstance().putBoolean(AppConstants.FIRST_PLAY_CYCLE,true);
        SpUtil.getInstance().putInt(AppConstants.TOMATO_STUDY_CYCLE,1);
        SpUtil.getInstance().putLong(AppConstants.TOMATO_BREAK_TIME,1000*60*5);
        SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE,false);
        SpUtil.getInstance().putLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0);

        //时钟显示计数
        long lastSuccess = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS);
        long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        chronometer.setBase(lastSuccess - elapsedRealtimeOffset);
        chronometer.start();

        Intent intent = new Intent(LockActivity.this, LockService.class);
        startService(intent);

    }

    /**
     * 结束后台监测正在运行的程序服务
     */
    public void cancleLockService() {
        //伪结束 后台服务不停止
        //Intent stopIntent = new Intent(LockApplication.getContext(), LockService.class);
        //stopService(stopIntent);
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
        NotifyUtil.stopServiceNotify(LockApplication.getContext());

        startActivity(new Intent(LockActivity.this, ResultActivity.class));

    }

    /**
     * 小玩一下
     */
    private void allowPlay() {

        Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);

        Long currentTime =System.currentTimeMillis();
        Long startTime = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS,0);
        Long settingTime =SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);
        Long thisTime =currentTime-startTime;

        if(thisTime>settingTime){
            //这里逻辑存在漏洞 如果一直没有亮屏操作 还有初始化结果未知 似乎初始化为true可以
            if(SpUtil.getInstance().getBoolean(AppConstants.FIRST_PLAY_CYCLE, true) )
            {
                ToastUtil.showToast("已达到规定学习时长，本次最多可玩"+ DataUtil.timeParse(remainPlaytime));
                SpUtil.getInstance().putBoolean(AppConstants.FIRST_PLAY_CYCLE, false);
            }else {
                remainPlaytime=remainPlaytime/2;
                ToastUtil.showToast("本次最多可玩"+ DataUtil.timeParse(remainPlaytime));
            }

        }else{
            remainPlaytime=remainPlaytime/2;
            ToastUtil.showToast("未达到规定学习时长，本次最多可玩"+ DataUtil.timeParse(remainPlaytime));
        }

        SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,remainPlaytime);
        SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,System.currentTimeMillis());
        SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE, false);
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,false);

        //记录解锁包名
        SpUtil.getInstance().putString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, pkgName);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        finish();


    }


    /**
     * 权限检查并显示弹框
     */
    private void checkAccessPermission(){
        if (Build.VERSION.SDK_INT >21&&!LockUtil.isStatAccessPermissionSet(LockActivity.this)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(LockActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage(R.string.dialog_tip);
            dialog.setCancelable(false);
            dialog.setPositiveButton("去修改",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                }
            });

            dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ToastUtil.showToast("开启失败，没有权限");
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                    Intent intent = new Intent(LockActivity.this, LockService.class);
                    stopService(intent);
                    NotifyUtil.stopServiceNotify(LockActivity.this);
                }
            });

            dialog.show();
        }else {
            ToastUtil.showToast("已获得权限");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (!LockUtil.isStatAccessPermissionSet(LockActivity.this)){
                ToastUtil.showToast("开启失败，没有权限");
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                Intent intent = new Intent(LockActivity.this, LockService.class);
                stopService(intent);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }
}

