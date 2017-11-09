package com.xidian.focustime.mvp.contract;

import android.content.Context;

import com.xidian.focustime.base.BasePresenter;
import com.xidian.focustime.base.BaseView;
import com.xidian.focustime.bean.App;

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
