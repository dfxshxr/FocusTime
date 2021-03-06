package com.xidian.mktime.module;

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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.mktime.LockApplication;
import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.db.AppManager;
import com.xidian.mktime.service.LockService;
import com.xidian.mktime.service.MonitorService;
import com.xidian.mktime.utils.DataUtil;
import com.xidian.mktime.utils.LockUtil;
import com.xidian.mktime.utils.NotifyUtil;
import com.xidian.mktime.utils.SpUtil;
import com.xidian.mktime.utils.ToastUtil;


/**
 * A login screen that offers login via email/password.
 */
public class LockActivity extends BaseActivity implements DialogInterface.OnDismissListener{

    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private String pkgName; //解锁应用的包名
    private String actionFrom;//按返回键的操作
    private AppManager mAppManager;

    private final int START=1;
    private final int STOP =2;
    private final int PAUSE=3;
    private final int STOP_ENSURE=4;
    private final int CANCEL=5;
    private Chronometer chronometer;
    ImageView mUserButton, mStartButton,mPlayButton, mStopButton, mSettingButton,mReStartButton,mAppButton,mLogoView,mCycleView,mCancelButton, mOkButton;
    TextView mTipTextView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        mLogoView = (ImageView) findViewById(R.id.logo);
        mCycleView = (ImageView) findViewById(R.id.cycle);
        mUserButton = (ImageView) findViewById(R.id.user_button);
        mTipTextView=(TextView) findViewById(R.id.tip_text_view);
        mAppButton =(ImageView) findViewById(R.id.app_button);
        mAppButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAccessPermission();
            }
        });
        mUserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    ToastUtil.showToast("学习中不能修改设置");
                }else {
                    Intent intent = new Intent(LockActivity.this, StatisticsActivity.class);
                    startActivity(intent);
                }
            }
        });
        mStartButton = (ImageView) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    ToastUtil.showToast("当前任务尚未结束");
                }else {
                   startLockService();
                }
            }
        });

        mPlayButton = (ImageView) findViewById(R.id.play_button);
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

        mStopButton = (ImageView) findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
                    UpdateUI(STOP_ENSURE);
                }else {
                    ToastUtil.showToast("不在学习中");
                }

            }
        });

        mSettingButton = (ImageView) findViewById(R.id.setting_button);
        mSettingButton.setOnClickListener(new OnClickListener() {
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

        mReStartButton = (ImageView) findViewById(R.id.restart_button);
        mReStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)&&!SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,false)) {
                    LockUtil.startLock();
                    UpdateUI(START);
                    chronometer.start();
                }else {
                    ToastUtil.showToast("不在休息中");
                }
            }
        });


        mOkButton = (ImageView) findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancleLockService();
            }
        });

        mCancelButton = (ImageView) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUI(START);
            }
        });

        /*mAppButton = (ImageView) findViewById(R.id.phone_button);
        mAppButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAccessPermission()){
                    Intent intent =new Intent();
                    intent.setAction("android.intent.action.CALL_BUTTON");
                    startActivity(intent);

                }

            }
        });*/

    }

    @Override
    protected void initData() {
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        //获取按返回键的操作
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        mAppManager = new AppManager(this);

        long currentTime=System.currentTimeMillis();
        long lastSuccess = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS);
        long elapsedRealtimeOffset = currentTime - SystemClock.elapsedRealtime();
        long totalPlayTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0);
        long totalErrorTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,0);
        long startPlayTime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,currentTime);

        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
            if(SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,false))
            {
                chronometer.setBase(lastSuccess - elapsedRealtimeOffset+totalPlayTime+totalErrorTime);
                UpdateUI(START);
                chronometer.start();
            }else{
                chronometer.setBase(lastSuccess - elapsedRealtimeOffset+totalPlayTime+totalErrorTime+(currentTime-startPlayTime));
                UpdateUI(PAUSE);
                chronometer.stop();
            }
        }else{
            UpdateUI(STOP);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void initAction() {

        //初始化变量
        Intent intent = new Intent(this, MonitorService.class);
        // 启动监控服务
        startService(intent);

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
        Intent intent = new Intent(this, MonitorService.class);
        // 启动监控服务
        startService(intent);

        SpUtil.getInstance().putLong(AppConstants.LOCK_START_MILLISENCONS, System.currentTimeMillis());
        SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10));
        SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE, true);
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
        SpUtil.getInstance().putBoolean(AppConstants.FIRST_PLAY_CYCLE,true);
        SpUtil.getInstance().putInt(AppConstants.TOMATO_STUDY_CYCLE,1);
        SpUtil.getInstance().putLong(AppConstants.TOMATO_BREAK_TIME,1000*60*5);
        SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE,false);
        SpUtil.getInstance().putLong(AppConstants.TOMATO_START_TIME, System.currentTimeMillis());
        SpUtil.getInstance().putLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0);
        SpUtil.getInstance().putLong(AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,0);
        SpUtil.getInstance().putLong(AppConstants.LAST_NORMAL_STATE_MILLISENCONS,System.currentTimeMillis());
        SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,System.currentTimeMillis());

        //时钟显示计数
        long lastSuccess = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS);
        long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        chronometer.setBase(lastSuccess - elapsedRealtimeOffset);
        chronometer.start();

        intent = new Intent(this, LockService.class);
        startService(intent);
        UpdateUI(START);
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
        UpdateUI(STOP);
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
        Boolean tomatoBreakTimeStatus =SpUtil.getInstance().getBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE, false);
        if(thisTime>settingTime||tomatoBreakTimeStatus){
            //这里逻辑存在漏洞 如果一直没有亮屏操作 还有初始化结果未知 似乎初始化为true可以
            if(tomatoBreakTimeStatus){
                ToastUtil.showToast("番茄时间到，本次可休息"+ DataUtil.timeParse(remainPlaytime));
            }else if(SpUtil.getInstance().getBoolean(AppConstants.FIRST_PLAY_CYCLE, true)){
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
        //SpUtil.getInstance().putBoolean(AppConstants.TOMATO_LEARNING_BREAK_TIME_STATE, false);
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
    private boolean checkAccessPermission(){
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
                    allowPlay();
                }
            });

            dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ToastUtil.showToast("开启失败，没有权限");
                }
            });

            dialog.show();
            return false;
        }else {
            return true;
           // ToastUtil.showToast("已获得权限");
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

    private void UpdateUI(int i){
        switch (i){
            case START:
                chronometer.setVisibility(View.VISIBLE);
                mLogoView.setVisibility(View.GONE);
                mCycleView.setVisibility(View.VISIBLE);
                mUserButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.VISIBLE);
                mAppButton.setVisibility(View.VISIBLE);
                mOkButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                mTipTextView.setVisibility(View.GONE);
                mPlayButton.setVisibility(View.VISIBLE);
                mReStartButton.setVisibility(View.GONE);
                break;
            case PAUSE:
                chronometer.setVisibility(View.VISIBLE);
                mLogoView.setVisibility(View.GONE);
                mCycleView.setVisibility(View.VISIBLE);
                mUserButton.setVisibility(View.GONE);
                mSettingButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.VISIBLE);
                mAppButton.setVisibility(View.VISIBLE);
                mOkButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                mTipTextView.setVisibility(View.GONE);
                mPlayButton.setVisibility(View.GONE);
                mReStartButton.setVisibility(View.VISIBLE);
                break;
            case STOP:
                chronometer.setVisibility(View.GONE);
                mLogoView.setVisibility(View.VISIBLE);
                mCycleView.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                mUserButton.setVisibility(View.VISIBLE);
                mSettingButton.setVisibility(View.VISIBLE);
                mPlayButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.GONE);
                mReStartButton.setVisibility(View.GONE);
                mAppButton.setVisibility(View.GONE);
                mOkButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                mTipTextView.setVisibility(View.GONE);
                break;
            case STOP_ENSURE:
                mReStartButton.setVisibility(View.GONE);
                mOkButton.setVisibility(View.VISIBLE);
                mCancelButton.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.GONE);
                mPlayButton.setVisibility(View.GONE);
                mTipTextView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}

