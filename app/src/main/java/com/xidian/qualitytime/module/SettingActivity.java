package com.xidian.qualitytime.module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;
import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.base.BaseActivity;
import com.xidian.qualitytime.utils.SpUtil;

import java.util.Calendar;

import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

import static com.xidian.qualitytime.utils.ToastUtil.showToast;

public class SettingActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Button mStudyButton = (Button) findViewById(R.id.study);
        mStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStudyTimePicker(view);
            }
        });
        Button mplayButton = (Button) findViewById(R.id.play);
        mplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayTimePicker(view);
            }
        });
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onStudyTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 1);//00:00
        picker.setRangeEnd(23, 59);//23:59
        int currentHour =(int)SpUtil.getInstance().getLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,1000*60*10)/(1000*60*60);
        int currentMinute = (int)SpUtil.getInstance().getLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,1000*60*10)/(1000*60);
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
                SpUtil.getInstance().putLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,continueTime);
            }
        });
        picker.show();
    }

    public void onPlayTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 1);//00:00

        Long continueTime =SpUtil.getInstance().getLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,1000*60*60*2);
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
}