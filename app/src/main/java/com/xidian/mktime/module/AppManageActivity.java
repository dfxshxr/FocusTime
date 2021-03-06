package com.xidian.mktime.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.xidian.mktime.R;
import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.bean.App;
import com.xidian.mktime.mvp.contract.MainContract;
import com.xidian.mktime.mvp.p.MainPresenter;
import com.xidian.mktime.service.LockService;
import com.xidian.mktime.utils.LockUtil;
import com.xidian.mktime.utils.NotifyUtil;
import com.xidian.mktime.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;


public class AppManageActivity extends BaseActivity implements MainContract.View{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CommentPagerAdapter mPagerAdapter;
    private MainPresenter mMainPresenter;

    private List<String> titles ;
    private List<Fragment> fragmentList ;


    private TextView mBtnSetting;
    private CheckBox mLockSwitch;






    @Override
    public int getLayoutId() {
        return R.layout.activity_app_manage;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mMainPresenter = new MainPresenter(this,this);
        mMainPresenter.loadAppInfo(this,true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void initData() {
        setToolbarTitle("应用白名单");
    }

    @Override
    protected void initAction() {}

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


}
