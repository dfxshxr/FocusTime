package com.xidian.qualitytime.module;


import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.BaseActivity;
import com.xidian.qualitytime.utils.MobileInfoUtils;


public class AddWhiteListActivity extends BaseActivity {

    private TextView mTextView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_text;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mTextView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= 24) {
            mTextView.setText(R.string.add_white_list_tip1);
        }else {
            mTextView.setText(R.string.add_white_list_tip2);
        }

    }

    @Override
    protected void initAction() {

        MobileInfoUtils.showRecentlyApp();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



}
