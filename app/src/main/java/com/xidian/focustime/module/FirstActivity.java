package com.xidian.focustime.module;

import android.content.Intent;
import android.os.Bundle;

import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;


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

        Intent loginIntent = new Intent(this, SplashActivity.class);

        loginIntent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
        loginIntent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(loginIntent);

        finish();
    }


}
