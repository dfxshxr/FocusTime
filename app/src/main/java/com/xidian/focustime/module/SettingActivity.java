package com.xidian.focustime.module;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lqr.optionitemview.OptionItemView;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base2.BaseActivity;
import com.xidian.focustime.base2.BasePresenter;
import com.xidian.focustime.utils.SpUtil;

import butterknife.BindView;
import cn.qqtheme.framework.picker.TimePicker;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.appBar)
    protected AppBarLayout mAppBar;
    @BindView(R.id.tvToolbarTitle)
    public TextView mToolbarTitle;
    @BindView(R.id.tvToolbarSubTitle)
    public TextView mToolbarSubTitle;
    @BindView(R.id.study)
    OptionItemView mStudyButton;
    @BindView(R.id.play)
    OptionItemView mplayButton;
    @BindView(R.id.version)
    TextView mVersionTextView;


    @Override
    public void initData() {
        mVersionTextView.setText("版本号："+getVersionName()+"("+getVersionCode()+")");
    }


    @Override
    public void initListener() {
        mStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStudyTimePicker(view);
            }
        });
        mplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayTimePicker(view);
            }
        });
    }


    public void onStudyTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 10);//00:00
        picker.setRangeEnd(23, 59);//23:59
        picker.setTextColor(ContextCompat.getColor(this,R.color.black0));
        picker.setDividerColor(ContextCompat.getColor(this,R.color.black0));
        picker.setCancelTextColor(ContextCompat.getColor(this,R.color.black0));
        picker.setSubmitTextColor(ContextCompat.getColor(this,R.color.black0));
        int currentHour =(int)SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*10)/(1000*60*60);
        int currentMinute = (int)SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*10)/(1000*60);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {

                int continueTime =(1000*60*Integer.parseInt(minute)+1000*60*60*Integer.parseInt(hour));
                Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10);
                if(continueTime<remainPlaytime){
                    SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,continueTime);
                }
                SpUtil.getInstance().putLong(AppConstants.LOCK_SETTING_MILLISENCONS,continueTime);
            }
        });
        picker.show();
    }

    public void onPlayTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 1);//00:00
        picker.setTextColor(ContextCompat.getColor(this,R.color.black0));
        picker.setDividerColor(ContextCompat.getColor(this,R.color.black0));
        picker.setCancelTextColor(ContextCompat.getColor(this,R.color.black0));
        picker.setSubmitTextColor(ContextCompat.getColor(this,R.color.black0));
        Long continueTime =SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);
        Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10);

        if(continueTime<remainPlaytime){
            remainPlaytime=continueTime;
        }
        int currentStudyHour =(int)(continueTime/(1000*60*60));
        int currentStudyMinute = (int)(continueTime/(1000*60)-currentStudyHour*60);
        int currentHour =(int)(remainPlaytime/(1000*60*60));
        int currentMinute = (int)(remainPlaytime/(1000*60)-currentHour*60);
        LogUtils.i(currentStudyHour+currentStudyMinute+"   "+currentHour+currentMinute);
        picker.setRangeEnd(currentStudyHour, currentStudyMinute);//23:59
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*Integer.parseInt(minute)+1000*60*60*Integer.parseInt(hour));
            }
        });
        picker.show();
    }

    public void onTomatoTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 10);
        Long tomatoTime=SpUtil.getInstance().getLong(AppConstants.TOMATO_TIME,1000*60*25);

        int currentHour =(int)(tomatoTime/(1000*60*60));
        int currentMinute = (int)(tomatoTime/(1000*60)-currentHour*60);

        picker.setRangeEnd(23,59);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                SpUtil.getInstance().putLong(AppConstants.TOMATO_TIME,1000*60*Integer.parseInt(minute)+1000*60*60*Integer.parseInt(hour));
            }
        });
        picker.show();
    }

    public void onTomatoBreakTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 1);
        Long tomatoBreakTime=SpUtil.getInstance().getLong(AppConstants.TOMATO_BREAK_TIME,1000*60*5);

        int currentHour =(int)(tomatoBreakTime/(1000*60*60));
        int currentMinute = (int)(tomatoBreakTime/(1000*60)-currentHour*60);

        picker.setRangeEnd(0,10);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                SpUtil.getInstance().putLong(AppConstants.TOMATO_BREAK_TIME,1000*60*Integer.parseInt(minute)+1000*60*60*Integer.parseInt(hour));
            }
        });
        picker.show();
    }

    /**
     * 获取版本名称
     * @return
     */
    private String getVersionName(){

        //得到包管理器
        PackageManager packageManager = getPackageManager();
        try {
            /**
             *  @param flags Additional option flags.有这个解释就用0
             *  得到包信息
             */
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;//获得版本号
            String versionName = packageInfo.versionName;//获得版本名称
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名的时候会走此异常
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取版本号
     * @return
     */
    private int getVersionCode(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取包信息
            int versionCode = packageInfo.versionCode;//获得版本号
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }
}