package com.xidian.focustime.module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.db.AppManager;
import com.xidian.focustime.service.LockService;
import com.xidian.focustime.utils.DataUtil;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;
import com.xidian.focustime.utils.ToastUtil;

import java.lang.reflect.Field;


/**
 * A login screen that offers login via email/password.
 */
public class StatisticsActivity extends BaseActivity{


    @Override
    public int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    @Override
    protected void initData() {


    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void initAction() {
    }



}

