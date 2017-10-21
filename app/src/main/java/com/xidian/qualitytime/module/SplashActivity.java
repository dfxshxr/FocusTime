package com.xidian.qualitytime.module;

import android.content.Intent;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.base.BaseActivity;
import com.xidian.qualitytime.service.LoadAppListService;
import com.xidian.qualitytime.service.LockService;
import com.xidian.qualitytime.utils.SpUtil;


public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        if(SpUtil.getInstance().getLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,0)==0){
            SpUtil.getInstance().putLong(AppConstants.LOCK_CONTINUE_MILLISENCONS,1000*60*60*2);
        }
        if(SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,0)==0){
            SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10);
            SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);
        }

        startService(new Intent(this, LoadAppListService.class));
        Intent intent = new Intent(this, LockService.class);
        startService(intent);

    }


    @Override
    protected void initAction() {

        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {
            LogUtils.i("启动跳转");
             gotoLockMainActivity();
        }else{
            gotoMainActivity();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void gotoLockMainActivity() {

        Intent loginIntent = new Intent(this, LoginActivity.class);

        loginIntent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
        loginIntent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(loginIntent);

        finish();
    }


    private void gotoMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
}
