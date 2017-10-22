package com.xidian.qualitytime.module;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.qualitytime.R;
import com.xidian.qualitytime.base.AppConstants;
import com.xidian.qualitytime.base.BaseActivity;
import com.xidian.qualitytime.utils.LockUtil;
import com.xidian.qualitytime.utils.MobileInfoUtils;
import com.xidian.qualitytime.utils.SpUtil;
import com.xidian.qualitytime.utils.ToastUtil;


public class GuideActivity extends BaseActivity implements View.OnClickListener{


    private TextView mBtnAutoStart,mBtnBackgroundRun,mBtnDeviceAdmin,mBtnNoClear,mBtnHideIcon;


    private CheckBox mAdvancedLockSwitch,mLockInstallSwitch,mLockAutoScreenSwitch;


    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mAdvancedLockSwitch = (CheckBox) findViewById(R.id.advanced_lock_switch_compat);
        mLockInstallSwitch = (CheckBox) findViewById(R.id.lock_install_switch_compat);
        mLockAutoScreenSwitch = (CheckBox) findViewById(R.id.lock_auto_screen_switch_compat);

        mBtnHideIcon = (TextView) findViewById(R.id.hide_icon);
        mBtnAutoStart = (TextView) findViewById(R.id.auto_start);
        mBtnBackgroundRun = (TextView) findViewById(R.id.background_run);
        mBtnDeviceAdmin = (TextView) findViewById(R.id.device_admin);
        mBtnNoClear = (TextView) findViewById(R.id.no_clear);
    }

    @Override
    protected void initData() {

        boolean isAdvancedLockOpen = LockUtil.isAccessibilitySettingsOn(this);
        boolean isLockInstallOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_INSTALL,false);
        boolean isLockAutoScreenOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN,true);

        mAdvancedLockSwitch.setChecked(isAdvancedLockOpen);
        mLockInstallSwitch.setChecked(isLockInstallOpen);
        mLockAutoScreenSwitch.setChecked(isLockAutoScreenOpen);

    }

    @Override
    protected void initAction() {

        mBtnHideIcon.setOnClickListener(this);
        mBtnAutoStart.setOnClickListener(this);
        mBtnBackgroundRun.setOnClickListener(this);
        mBtnDeviceAdmin.setOnClickListener(this);
        mBtnNoClear.setOnClickListener(this);
        mAdvancedLockSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
            boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    ToastUtil.showToast("请开启「应用监管系统」服务");
                } else {
                    ToastUtil.showToast("请关闭「应用监管系统」服务");
                }
                Intent intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                SpUtil.getInstance().putBoolean(AppConstants.ADVANCED_LOCK_STATE,checked);

            }
        });
        mLockInstallSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                boolean checked = ((CheckBox) v).isChecked();
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_INSTALL,checked);
            }
        });
        mLockAutoScreenSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                boolean checked = ((CheckBox) v).isChecked();
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN,checked);
            }
        });

        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, FirstActivity.class);
        int res = packageManager.getComponentEnabledSetting(componentName);
        if (!(res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED)) {
            mBtnHideIcon.setText("显示桌面图标");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.auto_start:
                MobileInfoUtils.jumpStartInterface(this);
                break;
            case R.id.background_run:
                MobileInfoUtils.jumpClearInterface(this);
                break;
            case R.id.device_admin:
                MobileInfoUtils.jumpDeviceAdminInterface(this);
                break;
            case R.id.no_clear:
                MobileInfoUtils.showRecentlyApp();
                Intent addWhiteListIntent = new Intent(this,AddWhiteListActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    addWhiteListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                }
                startActivity(addWhiteListIntent);
                break;
            case R.id.hide_icon:
                PackageManager packageManager = getPackageManager();
                ComponentName componentName = new ComponentName(this, FirstActivity.class);
                int res = packageManager.getComponentEnabledSetting(componentName);
                if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                        || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    // 隐藏应用图标
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    mBtnHideIcon.setText("显示桌面图标");
                    ToastUtil.showToast("已隐藏桌面图标");
                } else {
                    // 显示应用图标
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);
                    mBtnHideIcon.setText("隐藏桌面图标");
                    ToastUtil.showToast("已显示桌面图标");
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
            if (LockUtil.isAccessibilitySettingsOn(this)){
                mAdvancedLockSwitch.setChecked(true);
                SpUtil.getInstance().putBoolean(AppConstants.ADVANCED_LOCK_STATE, true);
            }
            else{
                mAdvancedLockSwitch.setChecked(false);
                SpUtil.getInstance().putBoolean(AppConstants.ADVANCED_LOCK_STATE, false);
            }

    }
}
