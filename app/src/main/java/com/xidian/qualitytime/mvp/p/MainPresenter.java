package com.xidian.qualitytime.mvp.p;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.bean.App;
import com.xidian.qualitytime.db.AppManager;
import com.xidian.qualitytime.mvp.contract.MainContract;
import com.xidian.qualitytime.utils.SpUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private PackageManager mPackageManager;
    private AppManager mAppManager;
    private Context mContext;
    private LoadAppInfoAsyncTask mLoadAppInfo;
    private LoadLockAsyncTask mLoadLockAsyncTask;

    public MainPresenter(MainContract.View view, Context mContext) {
        mView = view;
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
        mAppManager = new AppManager(mContext);
    }

    @Override
    public void loadAppInfo(Context context, boolean isSort) {
        mLoadAppInfo = new LoadAppInfoAsyncTask(isSort);
        mLoadAppInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void loadLockAppInfo(Context context) {
        mLoadLockAsyncTask = new LoadLockAsyncTask();
        mLoadLockAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadAppInfoAsyncTask extends AsyncTask<Void, String, List<App>> {

        private boolean isSort = false;

        public LoadAppInfoAsyncTask(boolean isSort) {
            this.isSort = isSort;
        }

        @Override
        protected List<App> doInBackground(Void... params) {
            List<App> Apps = mAppManager.getAllApps();
            Iterator<App> infoIterator = Apps.iterator();
            int recommendNum = 0;
            int sysAppNum = 0;
            int userAppNum = 0;

            while (infoIterator.hasNext()) {
                App info = infoIterator.next();
                try {
                    ApplicationInfo appInfo = mPackageManager.getApplicationInfo(info.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                    if (appInfo == null || mPackageManager.getApplicationIcon(appInfo) == null) {
                        infoIterator.remove(); //将有错的app移除
                        continue;
                    } else {
                        info.setAppInfo(appInfo); //给列表ApplicationInfo赋值
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //判断是否是系统应用 ApplicationInfo#isSystemApp()
                            info.setSysApp(true);
                            info.setTopTitle("系统应用");
                        } else {
                            info.setSysApp(false);
                            info.setTopTitle("用户应用");
                        }
                    }
                    //获取推荐应用总数
                    if (info.isLocked()) {
                        recommendNum++;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    infoIterator.remove();
                }
            }
            SpUtil.getInstance().putInt(AppConstants.LOCK_RECOMMEND_NUM, recommendNum);

            if (isSort) {
                List<App> sysList = new ArrayList<>();
                List<App> userList = new ArrayList<>();
                for (App info : Apps) {
                    if (info.isSysApp()) {
                        sysList.add(info);
                        sysAppNum++;
                    } else {
                        userList.add(info);
                        userAppNum++;
                    }
                }
                SpUtil.getInstance().putInt(AppConstants.LOCK_SYS_APP_NUM, sysAppNum);
                SpUtil.getInstance().putInt(AppConstants.LOCK_USER_APP_NUM, userAppNum);
                Apps.clear();
                Apps.addAll(sysList);
                Apps.addAll(userList);
            }
            return Apps;
        }

        @Override
        protected void onPostExecute(List<App> Apps) {
            super.onPostExecute(Apps);
            mView.loadAppInfoSuccess(Apps);
        }
    }

    private class LoadLockAsyncTask extends AsyncTask<String, Void, List<App>> {

        @Override
        protected List<App> doInBackground(String... params) {
            List<App> Apps = mAppManager.getAllApps();
            Iterator<App> infoIterator = Apps.iterator();

            while (infoIterator.hasNext()) {
                App info = infoIterator.next();
                try {
                    ApplicationInfo appInfo = mPackageManager.getApplicationInfo(info.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                    if (appInfo == null || mPackageManager.getApplicationIcon(appInfo) == null) {
                        infoIterator.remove(); //将有错的app移除
                        continue;
                    } else {
                        info.setAppInfo(appInfo); //给列表ApplicationInfo赋值
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //判断是否是系统应用 ApplicationInfo#isSystemApp()
                            info.setSysApp(true);
                            info.setTopTitle("系统应用");
                        } else {
                            info.setSysApp(false);
                            info.setTopTitle("用户应用");
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    infoIterator.remove();
                }
            }

            List<App> list = new ArrayList<>();
            for (App info : Apps) {
                if (info.isLocked()) {
                    list.add(info);
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<App> Apps) {
            super.onPostExecute(Apps);
            mView.loadAppInfoSuccess(Apps);
        }

    }

    @Override
    public void onDestroy() {
        if (mLoadAppInfo != null && mLoadAppInfo.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadAppInfo.cancel(true);
        }
        if (mLoadLockAsyncTask != null && mLoadLockAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadLockAsyncTask.cancel(true);
        }
    }
}
