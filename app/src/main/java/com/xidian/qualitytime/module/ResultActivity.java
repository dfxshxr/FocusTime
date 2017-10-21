package com.xidian.qualitytime.module;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.BaseActivity;

public class ResultActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
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

    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
