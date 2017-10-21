package com.xidian.qualitytime.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;
import com.xidian.qualitytime.LockApplication;
import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.base.BaseActivity;
import com.xidian.qualitytime.db.AppManager;
import com.xidian.qualitytime.service.LockService;
import com.xidian.qualitytime.utils.LockUtil;
import com.xidian.qualitytime.utils.NotifyUtil;
import com.xidian.qualitytime.utils.SpUtil;
import com.xidian.qualitytime.utils.ToastUtil;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{


    private String pkgName; //解锁应用的包名
    private String actionFrom;//按返回键的操作
    private AppManager mAppManager;



    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                allowPlay();
            }
        });

        Button mCancleButton = (Button) findViewById(R.id.cancle_button);
        mCancleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopIntent = new Intent(LockApplication.getContext(), LockService.class);
                stopService(stopIntent);
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                NotifyUtil.stopServiceNotify(LockApplication.getContext());
                startActivity(new Intent(LoginActivity.this, ResultActivity.class));
            }
        });


    }

    @Override
    protected void initData() {
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        //获取按返回键的操作
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        mAppManager = new AppManager(this);
    }

    @Override
    protected void initAction() {
    }

    private void allowPlay() {

        Long remainPlaytime=SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,1000*60*10);
        if(remainPlaytime>=1000*30){
            SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_START_MILLISENCONS,System.currentTimeMillis());
            SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE,false);
            if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                SpUtil.getInstance().putString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, pkgName);//记录解锁包名
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                finish();
            }
        }else {
            LogUtils.i(remainPlaytime);
            ToastUtil.showToast("可玩时间不足");
        }

    }

    @Override
    public void onBackPressed() {
        LockUtil.goHome(this);
    }

}

