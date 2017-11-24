package com.xidian.focustime.module.presenter;

import com.bumptech.glide.Glide;
import com.lqr.imagepicker.bean.ImageItem;
import com.xidian.focustime.R;

import com.xidian.focustime.base2.BaseActivity;
import com.xidian.focustime.base2.BasePresenter;
import com.xidian.focustime.module.view.IMyInfoAtView;

import com.xidian.focustime.base2.UIUtils;


import java.io.File;

public class MyInfoAtPresenter extends BasePresenter<IMyInfoAtView> {

    //public UserInfo mUserInfo;
   // private UploadManager mUploadManager;

    public MyInfoAtPresenter(BaseActivity context) {
        super(context);
    }

    public void loadUserInfo() {
        /*mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (mUserInfo != null) {
            Glide.with(mContext).load(mUserInfo.getPortraitUri()).centerCrop().into(getView().getIvHeader());
            getView().getOivName().setRightText(mUserInfo.getName());
            getView().getOivAccount().setRightText(mUserInfo.getUserId());
        }*/
    }

    public void setPortrait(ImageItem imageItem) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        //上传头像
    }

    private void uploadError(Throwable throwable) {
        /*if (throwable != null)
            LogUtils.sf(throwable.getLocalizedMessage());*/
        mContext.hideWaitingDialog();
        UIUtils.showToast(UIUtils.getString(R.string.set_fail));
    }
}
