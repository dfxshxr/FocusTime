package com.xidian.mktime.module;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.bean.StudyStatistics;
import com.xidian.mktime.utils.DataUtil;
import com.xidian.mktime.utils.SpUtil;
import com.xidian.mktime.utils.TokenUtil;

public class ResultActivity extends BaseActivity {

    private Chronometer chronometer;
    TextView mResultText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

        mResultText = (TextView) findViewById(R.id.textView);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void initData() {
        setToolbarTitle("本次专注");
        long currentTime =System.currentTimeMillis();
        long startTime = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS,0);
        long settingTime =SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);
        long totalErrorTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_ERROR_STATE_MILLISENCONS,0);
        long totalPlayTime=SpUtil.getInstance().getLong(AppConstants.TOTAL_PLAY_MILLISENCONS,0);
        if(!SpUtil.getInstance().getBoolean(AppConstants.RUN_LOCK_STATE,true)) {
            //只有解过锁后开始可玩时间才有值
            long startPlayTime = SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_START_MILLISENCONS, currentTime);
            totalPlayTime =totalPlayTime + (currentTime - startPlayTime);
        }


        long thisTime =currentTime-startTime-totalPlayTime-totalErrorTime;

        chronometer.setBase(SystemClock.elapsedRealtime()-thisTime);


        chronometer.stop();

        int amount =0;
        if(thisTime>settingTime){
            mResultText.setText("专注成功");
            amount = DataUtil.safeLongToInt(thisTime/(1000*60*5)) ;

        }else{
            mResultText.setText("专注失败");
            amount = DataUtil.safeLongToInt(thisTime/(1000*60*10)) ;
        }

        if (amount<=0){
            amount=0;
        }
        if(amount>=100){
            amount=100;
        }
        if (amount>0){
            TokenUtil.Billing(amount,"专注奖励");
        }


        StudyStatistics studyStatistics = new StudyStatistics();
        studyStatistics.setThisMilliseconds(thisTime);
        studyStatistics.setEndMilliseconds(currentTime);
        studyStatistics.setStartMilliseconds(startTime);
        studyStatistics.setSettingMilliseconds(settingTime);
        studyStatistics.setErrorMilliseconds(totalErrorTime);
        studyStatistics.setPlayMilliseconds(totalPlayTime);
        studyStatistics.save();

    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
