package com.xidian.focustime.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;

/**
 * 用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.xidian.qualitytime.base.BaseActivity.java
 * @author: KIKI
 * @date: 2017-03-06 15:29
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mCustomTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LockApplication.getInstance().doForCreate(this);

        //设置布局内容
        setContentView(getLayoutId());
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();
        initData();
        initAction();
    }

    public abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
        }
    }


    protected abstract void initData();

    protected abstract void initAction();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockApplication.getInstance().doForFinish(this);
    }

    public final void clear() {
        super.finish();
    }
}
