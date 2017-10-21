package com.xidian.qualitytime.mvp.contract;

import android.content.Context;

import com.xidian.qualitytime.base.BasePresenter;
import com.xidian.qualitytime.base.BaseView;
import com.xidian.qualitytime.bean.App;

import java.util.List;


public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<App> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
