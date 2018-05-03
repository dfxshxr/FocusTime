package com.xidian.focustime.module;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lqr.optionitemview.OptionItemView;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.utils.DataUtil;
import com.xidian.focustime.utils.SpUtil;
import com.xidian.focustime.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * @描述 我的个人信息
 */
public class UserInfoActivity extends BaseActivity{

    public static final int REQUEST_IMAGE_PICKER = 1000;

    @BindView(R.id.appBar)
    protected AppBarLayout mAppBar;
    @BindView(R.id.ivToolbarHistory)
    ImageView mIvHistory;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
      //  mIvHistory.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        setToolbarTitle("今日统计");
    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
