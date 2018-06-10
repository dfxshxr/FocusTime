package com.xidian.mktime.module;

import android.content.Intent;
import android.os.Bundle;

import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;


public class FirstActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {


    }


    @Override
    protected void initAction() {
        gotoSplashActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void gotoSplashActivity() {

        Intent intent = new Intent(this, SplashActivity.class);

        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }


}
