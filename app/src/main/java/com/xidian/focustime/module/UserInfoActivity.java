package com.xidian.focustime.module;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.optionitemview.OptionItemView;
import com.xidian.focustime.R;
import com.xidian.focustime.base2.BaseActivity;
import com.xidian.focustime.base2.BasePresenter;
import com.xidian.focustime.module.presenter.MyInfoAtPresenter;
import com.xidian.focustime.module.view.IMyInfoAtView;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * @创建者 CSDN_LQR
 * @描述 我的个人信息
 */
public class UserInfoActivity extends BaseActivity{

    public static final int REQUEST_IMAGE_PICKER = 1000;

    @BindView(R.id.appBar)
    protected AppBarLayout mAppBar;
    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView tvName;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
       /* mIvHeader.setOnClickListener(v -> {
            Intent intent = new Intent(MyInfoActivity.this, ShowBigImageActivity.class);
            intent.putExtra("url", mPresenter.mUserInfo.getPortraitUri().toString());
            jumpToActivity(intent);
        });
        mLlHeader.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });
        mOivName.setOnClickListener(v -> jumpToActivity(ChangeMyNameActivity.class));*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_info;
    }

}
