package com.xidian.focustime.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.bean.App;
import com.xidian.focustime.bean.RecommendApp;
import com.xidian.focustime.utils.DataUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.litepal.crud.DataSupport.where;


/**
 * 数据库管理
 */

public class AppManager {

    private PackageManager mPackageManager;
    private Context mContext;

    public AppManager(Context mContext) {
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
    }

    /**
     * 查找所有
     */
    public synchronized List<App> getAllApps() {
        List<App> Apps = DataSupport.findAll(App.class);
        Collections.sort(Apps, RecommendComparator);
        Collections.sort(Apps, LockComparator);
        return Apps;
    }

    /**
     * 删除数据
     */
    public synchronized void deleteAppTable(List<App> Apps) {
        for (App info : Apps) {
            DataSupport.deleteAll(App.class, "packageName = ?", info.getPackageName());
        }
    }

    /**
     * 将应用信息插入数据库
     */
    public synchronized void instanceAppTable(List<ResolveInfo> resolveInfos) throws PackageManager.NameNotFoundException {
        List<App> list = new ArrayList<>();

        for (ResolveInfo resolveInfo : resolveInfos) {
            boolean isRecommendApp = isHasRecommendAppInfo(resolveInfo.activityInfo.packageName); //是否为推荐加锁的app

            App App = new App(resolveInfo.activityInfo.packageName,true, isRecommendApp); // 后续需添加默认的开启保护
            ApplicationInfo appInfo = mPackageManager.getApplicationInfo(App.getPackageName(), 0);//PackageManager.MATCH_UNINSTALLED_PACKAGES
            String appName = mPackageManager.getApplicationLabel(appInfo).toString();
            //过滤掉一些应用  !App.getPackageName().equals("com.android.settings")&&
            if (!App.getPackageName().equals(AppConstants.APP_PACKAGE_NAME)) {

                if (isRecommendApp) { //如果是推荐的
                    App.setLocked(false);
                } else {
                    App.setLocked(true);
                }
                App.setAppName(appName);

                list.add(App);
            }
        }
        list = DataUtil.clearRepeatApp(list);  //去除重复数据

        DataSupport.saveAll(list);
    }

    /**
     * 判断是否是推荐加锁的应用
     */
    public boolean isHasRecommendAppInfo(String packageName) {
        List<RecommendApp> infos = DataSupport.where("packageName = ?", packageName).find(RecommendApp.class);
        return infos.size() > 0;
    }

    /**
     * 对app设置加锁标识
     */
    public void lockApp(String packageName) {
        updateLockStatus(packageName, true);
    }

    /**
     * 对app设置解锁标识
     */
    public void unlockApp(String packageName) {
        updateLockStatus(packageName, false);
    }

    public void updateLockStatus(String packageName, boolean isLock) {
        ContentValues values = new ContentValues();
        values.put("isLocked", isLock);
        DataSupport.updateAll(App.class, values, "packageName = ?", packageName);
    }


    /**
     * 检查是否开启了加锁
     *
     * @param packageName
     * @return boolean
     */
    public boolean isLockedPackageName(String packageName) {
        List<App> lockInfos = where("packageName = ?", packageName).find(App.class);
        for (App App : lockInfos) {
            if (App.isLocked()) {
                return true;
            }
        }
        return false;
    }



    private Comparator RecommendComparator = new Comparator() {

        @Override
        public int compare(Object lhs, Object rhs) {
            App leftApp = (App) lhs;
            App rightApp = (App) rhs;

            if (leftApp.isRecommendApp() && !rightApp.isRecommendApp())
            {
                return 1;
            }
            else if (!leftApp.isRecommendApp()
                    && rightApp.isRecommendApp()) {
                return -1;
            }
            return 0;
        }
    };

    private Comparator LockComparator = new Comparator() {

        @Override
        public int compare(Object lhs, Object rhs) {
            App leftApp = (App) lhs;
            App rightApp = (App) rhs;

            if (leftApp.isLocked() && !rightApp.isLocked())
            {
                return 1;
            }
            else if (!leftApp.isLocked()
                    && rightApp.isLocked()) {
                return -1;
            }
            return 0;
        }
    };

}
