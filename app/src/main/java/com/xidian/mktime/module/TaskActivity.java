package com.xidian.mktime.module;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lqr.optionitemview.OptionItemView;
import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.utils.DataUtil;
import com.xidian.mktime.utils.SpUtil;
import com.xidian.mktime.utils.ToastUtil;
import com.xidian.mktime.utils.TokenUtil;

import butterknife.BindView;
import cn.qqtheme.framework.picker.TimePicker;

public class TaskActivity extends BaseActivity {

    @BindView(R.id.appBar)
    protected AppBarLayout mAppBar;
    @BindView(R.id.tvToolbarTitle)
    public TextView mToolbarTitle;
    @BindView(R.id.tvToolbarSubTitle)
    public TextView mToolbarSubTitle;
    @BindView(R.id.oivToken)
    OptionItemView oivToken;
    @BindView(R.id.oivBonus)
    OptionItemView oivBonus;
    @BindView(R.id.oivSignIn)
    OptionItemView oivSignIn;


    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void initData() {
        setToolbarTitle("任务列表");
        oivToken.setRightText(Integer.toString(TokenUtil.getTokenSum()));
        if(TokenUtil.checkSignIn()){
            oivSignIn.setRightText("打卡");
        }else {
            if(TokenUtil.checkLastSignIn()){
                int totalSignIn = SpUtil.getInstance().getInt("TOTAL_SIGN_IN",0);
                oivSignIn.setRightText("已连续打卡"+totalSignIn+"天");
            }else {
                oivSignIn.setRightText("已打卡");
            }
        }

        oivBonus.setRightText("敬请期待");
    }


    @Override
    protected void initAction() {
        oivToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        oivSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TokenUtil.checkSignIn()){

                    if(TokenUtil.checkLastSignIn()){
                        int totalSignIn = SpUtil.getInstance().getInt("TOTAL_SIGN_IN",0);
                        TokenUtil.Billing(10+totalSignIn,"连续打卡");
                        ToastUtil.showToast("打卡成功，获得"+totalSignIn+"积分");
                        totalSignIn++;
                        SpUtil.getInstance().putInt("TOTAL_SIGN_IN",totalSignIn);
                        oivSignIn.setRightText("已连续打卡"+totalSignIn+"天");
                    }else {
                        SpUtil.getInstance().putInt("TOTAL_SIGN_IN",1);
                        TokenUtil.Billing(10,"打卡");
                        ToastUtil.showToast("打卡成功，获得10积分\n连续打卡可获得更多奖励哦～");
                        oivSignIn.setRightText("已打卡");
                    }

                }
                oivToken.setRightText(Integer.toString(TokenUtil.getTokenSum()));
            }
        });
        oivBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}


