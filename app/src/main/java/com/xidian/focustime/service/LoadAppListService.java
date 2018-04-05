package com.xidian.focustime.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.bean.App;
import com.xidian.focustime.bean.RecommendApp;
import com.xidian.focustime.db.AppManager;
import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.utils.SpUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 后台加载应用列表
 */

public class LoadAppListService extends IntentService {

    public static final String ACTION_START_LOAD_APP = "com.xidian.qualitytime.service.action.LOADAPP";
    private PackageManager mPackageManager;
    private AppManager mAppManager;
    long time = 0;

    public LoadAppListService() {
        super("LoadAppListService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPackageManager = getPackageManager();
        mAppManager = new AppManager(this);

        time = System.currentTimeMillis();

        boolean isInitRecommend = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_INIT_RECOMMEND, false);
        boolean isInitDb = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_INIT_DB, false);
        if (!isInitRecommend) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_INIT_RECOMMEND, true);
            initRecommendApps();
        }

        //每次都获取手机上的所有应用
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        //非第一次，对比数据
        if (isInitDb) {
            List<ResolveInfo> appList = new ArrayList<>();
            List<App> dbList = mAppManager.getAllApps(); //获取数据库列表
            //处理应用列表
            for (ResolveInfo resolveInfo : resolveInfos) {

                if (!resolveInfo.activityInfo.packageName.equals(AppConstants.APP_PACKAGE_NAME)){
                    appList.add(resolveInfo);
                }
            }
            if (appList.size() > dbList.size()) { //如果有安装新应用
                List<ResolveInfo> reslist = new ArrayList<>();
                HashMap<String, App> hashMap = new HashMap<>();
                for (App app : dbList) {
                    hashMap.put(app.getPackageName(), app);
                }
                for (ResolveInfo app : appList) {
                    if (!hashMap.containsKey(app.activityInfo.packageName)) {
                        reslist.add(app);
                    }
                }
                try {
                    if (reslist.size() != 0)
                        mAppManager.instanceAppTable(reslist); //将剩下不同的插入数据库
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (appList.size() < dbList.size()) { //如果有卸载应用
                List<App> commlist = new ArrayList<>();
                HashMap<String, ResolveInfo> hashMap = new HashMap<>();
                for (ResolveInfo app : appList) {
                    hashMap.put(app.activityInfo.packageName, app);
                }
                for (App app : dbList) {
                    if (!hashMap.containsKey(app.getPackageName())) {
                        commlist.add(app);
                    }
                }
                LogUtils.i("有应用卸载，个数是 = " + dbList.size());
                if (commlist.size() != 0)
                    mAppManager.deleteAppTable(commlist);//将多的从数据库删除
            } else {
                LogUtils.i("应用数量不变");
            }
        } else {
            //数据库只插入一次
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_INIT_DB, true);
            try {
                mAppManager.instanceAppTable(resolveInfos);    //插入数据库
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        LogUtils.i("耗时 = " + (System.currentTimeMillis() - time));
    }

    @Override
    protected void onHandleIntent(Intent handleIntent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAppManager = null;
    }

    /**
     * 初始化白名单的应用
     */
    public void initRecommendApps() {
        List<String> packageList = new ArrayList<>();
        List<RecommendApp> recommendApps = new ArrayList<>();

        packageList.add("com.android.contacts");
       /* packageList.add("com.android.settings");       //设置菜单
        packageList.add("com.tencent.android.qqdownloader");       //腾讯应用宝
        packageList.add("com.qihoo.appstore");       //360手机助手
        packageList.add("com.baidu.appsearch");       //百度手机助手
        packageList.add("com.xiaomi.market");       //小米应用商店
        packageList.add("com.huawei.appmarket");       //华为应用商店
        packageList.add("com.wandoujia.phoenix2");       //豌豆荚
        packageList.add("com.dragon.android.pandaspace");       //91手机助手
        packageList.add("com.hiapk.marketpho");       //安智应用商店
        packageList.add("com.yingyonghui.market");       //应用汇
        packageList.add("com.tencent.qqpimsecure");       //QQ手机管家
        packageList.add("com.mappn.gfan");       //机锋应用市场
        packageList.add("com.pp.assistant");       //PP手机助手
        packageList.add("com.oppo.market");       //OPPO应用商店
        packageList.add("cn.goapk.market");       //GO市场
        packageList.add("zte.com.market");       //中兴应用商店
        packageList.add("com.yulong.android.coolmart");       //宇龙Coolpad应用商店
        packageList.add("com.lenovo.leos.appstore");       //联想应用商店
        packageList.add("com.coolapk.market");       //cool市场

        packageList.add("om.miui.securitycenter");       //小米安全中心
        packageList.add("com.samsung.android.sm_cn");       //三星智能管理器
        packageList.add("com.letv.android.permissionautoboot");       //乐视自启动管理
        packageList.add("com.huawei.systemmanager");       //华为手机管家
        packageList.add("com.iqoo.secure");       //VIVO i管家
        packageList.add("com.meizu.safe");       //魅族手机管家
        packageList.add("com.oppo.safe");       //OPPO手机管家
        packageList.add("com.yulong.android.coolsafe");       //360手机管家

        packageList.add("com.android.gallery3d");       //相册
        packageList.add("com.android.mms");             //短信
        packageList.add("com.android.contacts");        //联系人和电话
        packageList.add("com.facebook.katana");         //facebook
        packageList.add("com.facebook.orca");           //facebook Messenger
        packageList.add("com.mediatek.filemanager");    //文件管理器
        packageList.add("com.sec.android.gallery3d");   //也是个相册
        packageList.add("com.android.email");           //邮箱
        packageList.add("com.sec.android.app.myfiles"); //三星的文件
        packageList.add("com.android.vending");         //应用商店
        packageList.add("com.google.android.youtube");  //youtube
        packageList.add("com.tencent.mobileqq");        //qq
        packageList.add("com.tencent.qq");              //qq
        packageList.add("com.tencent.mm");              //微信

        packageList.add("com.android.dialer");          //拨号
        packageList.add("com.twitter.android");         //twitter
        packageList.add("com.sina.weibo");         //微博


        packageList.add("com.eg.android.AlipayGphone"); //支付宝
        packageList.add("com.alipay.android.client.pad"); //支付宝

        packageList.add("com.bankcomm"); //交通银行
        packageList.add("cmb.pb"); //招商银行
        packageList.add("com.chinamworld.bocmbci");//中国银行
        packageList.add("com.chinamworld.electronicpayment");//中国银行
        packageList.add("com.chinamworld.bocapad"); //中国银行
        packageList.add("com.icbc.mobile.abroadbank"); //工商银行
        packageList.add("com.icbc.hwhce");//工商银行
        packageList.add("com.chinamworld.main");//中国建设银行
        packageList.add("com.ccba");//中国建设银行*/

        for (String packageName : packageList) {
            RecommendApp app = new RecommendApp();
            app.setPackageName(packageName);
            recommendApps.add(app);
        }

        DataSupport.deleteAll(RecommendApp.class);
        DataSupport.saveAll(recommendApps);
    }
}
