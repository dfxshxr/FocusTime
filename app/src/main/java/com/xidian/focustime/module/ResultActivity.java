package com.xidian.focustime.module;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.utils.DataUtil;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;

public class ResultActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    TextView mResultText;

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mResultText = (TextView) findViewById(R.id.textView);
        Button mCancleButton = (Button) findViewById(R.id.button);
        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

        Long currentTime =System.currentTimeMillis();
        Long startTime = SpUtil.getInstance().getLong(AppConstants.LOCK_START_MILLISENCONS,0);
        Long settingTime =SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);
        Long thisTime =currentTime-startTime;

        if(thisTime>settingTime){
            mResultText.setText("任务成功："+"本次学习时长"+ DataUtil.timeParse(thisTime));
        }else{
            mResultText.setText("任务失败："+"本次学习时长"+ DataUtil.timeParse(thisTime)+"设定学习时长"+ DataUtil.timeParse(settingTime));
        }

    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}