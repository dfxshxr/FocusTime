package com.xidian.mktime.module;

import android.content.Intent;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.service.LoadAppListService;
import com.xidian.mktime.service.LockService;
import com.xidian.mktime.utils.ServiceUtil;
import com.xidian.mktime.utils.SpUtil;


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
        if(SpUtil.getInstance().getLong(AppConstants.LOCK_SETTING_MILLISENCONS,0)==0){
            SpUtil.getInstance().putLong(AppConstants.LOCK_SETTING_MILLISENCONS,1000*60*60*2);
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
            LogUtils.i("启动跳转");
            gotoLockMainActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void gotoLockMainActivity() {

        Intent intent = new Intent(this, LockActivity.class);

        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }


    private void gotoMainActivity() {

        Intent intent = new Intent(this, AppManageActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

}
