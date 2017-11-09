package com.xidian.focustime.module;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.R;
import com.xidian.focustime.base.AppConstants;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.bean.App;
import com.xidian.focustime.mvp.contract.MainContract;
import com.xidian.focustime.mvp.p.MainPresenter;
import com.xidian.focustime.service.LockService;
import com.xidian.focustime.utils.LockUtil;
import com.xidian.focustime.utils.NotifyUtil;
import com.xidian.focustime.utils.SpUtil;
import com.xidian.focustime.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements MainContract.View, View.OnClickListener,DialogInterface.OnDismissListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CommentPagerAdapter mPagerAdapter;
    private MainPresenter mMainPresenter;

    private List<String> titles ;
    private List<Fragment> fragmentList ;


    private TextView mBtnSetting;
    private CheckBox mLockSwitch;


    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;



    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mLockSwitch = (CheckBox) findViewById(R.id.switch_compat);

        mBtnSetting = (TextView) findViewById(R.id.guide_activity);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mMainPresenter = new MainPresenter(this,this);
        mMainPresenter.loadAppInfo(this,true);
    }

    @Override
    protected void initData() {

        boolean isLockOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);

        mLockSwitch.setChecked(isLockOpen);

        startLockService();




    }

    @Override
    protected void initAction() {

        mBtnSetting.setOnClickListener(this);

        mLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                LogUtils.d(String.valueOf(b));

                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, b);

                if (b) {
                    SpUtil.getInstance().putLong(AppConstants.LOCK_START_MILLISENCONS, System.currentTimeMillis());
                    LogUtils.i(System.currentTimeMillis());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_PLAY_REMAIN_MILLISENCONS,SpUtil.getInstance().getLong(AppConstants.LOCK_PLAY_SETTING_MILLISENCONS,1000*60*10));
                    SpUtil.getInstance().putBoolean(AppConstants.RUN_LOCK_STATE, true);
                    startLockService();
                    LockUtil.gotoUnlock(MainActivity.this,AppConstants.APP_PACKAGE_NAME);
                } else {
                    LockUtil.gotoResult(MainActivity.this,false);
                    stopLockService();

                }
            }
        });


    }

    @Override
    public void loadAppInfoSuccess(List<App> list) {

        titles = new ArrayList<>();

        titles.add("第三方应用");
        titles.add("系统应用");

        UserAppFragment unlockAppFragment = UserAppFragment.newInstance(list);
        SysAppFragment sysAppFragment = SysAppFragment.newInstance(list);
        fragmentList = new ArrayList<>();
        fragmentList.add(unlockAppFragment);
        fragmentList.add(sysAppFragment);
        mPagerAdapter = new CommentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.guide_activity:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }
    }


    public class CommentPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titles = new ArrayList<>();


        public CommentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (!LockUtil.isStatAccessPermissionSet(MainActivity.this)){
                ToastUtil.showToast("开启失败，没有权限");
                mLockSwitch.setChecked(false);
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                Intent intent = new Intent(MainActivity.this, LockService.class);

                stopService(intent);
            }
        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }


    /**
     * 启动后台监测正在运行的程序服务
     */

    public void startLockService() {

        Intent intent = new Intent(MainActivity.this, LockService.class);
        startService(intent);

        if (Build.VERSION.SDK_INT >21&&!LockUtil.isStatAccessPermissionSet(MainActivity.this)) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(R.string.dialog_tip);
                dialog.setCancelable(false);
                dialog.setPositiveButton("去修改",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                });

                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ToastUtil.showToast("开启失败，没有权限");
                        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                        mLockSwitch.setChecked(false);
                        Intent intent = new Intent(MainActivity.this, LockService.class);

                        stopService(intent);
                        NotifyUtil.stopServiceNotify(MainActivity.this);
                    }
                });

                dialog.show();
            }

    }

    public void stopLockService() {
        Intent stopIntent = new Intent(this, LockService.class);
        stopService(stopIntent);
        NotifyUtil.stopServiceNotify(this);
}


}
