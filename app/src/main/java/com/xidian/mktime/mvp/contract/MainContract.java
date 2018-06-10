package com.xidian.mktime.mvp.contract;

import android.content.Context;

import com.xidian.mktime.base.BasePresenter;
import com.xidian.mktime.base.BaseView;
import com.xidian.mktime.bean.App;

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
