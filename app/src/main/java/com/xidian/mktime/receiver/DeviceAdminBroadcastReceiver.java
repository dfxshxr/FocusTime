package com.xidian.mktime.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.utils.LockUtil;
import com.xidian.mktime.utils.SpUtil;

/**
 * 用途
 *
 * @version V1.0 <接收设备管理器广播>
 * @FileName: com.xidian.qualitytime.receiver.DeviceAdminBroadcastReceiver.java
 * @author: KIKI
 * @date: 2017-04-16 14:40
 */
public class DeviceAdminBroadcastReceiver extends DeviceAdminReceiver {

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        ComponentName mDeviceAdmin = new ComponentName(context,DeviceAdminBroadcastReceiver.class);
        if (manager.isAdminActive(mDeviceAdmin)&&SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE,false)) {


            //调用设备管理器本身的功能，每 100ms 锁屏一次，用户即便解锁也会立即被锁，直至 7s 后
            final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            dpm.lockNow();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (i < 70) {
                        dpm.lockNow();
                        try {
                            Thread.sleep(100);
                            i++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            LockUtil.gotoUnlock(context,"com.android.setting");

        }
        return "取消激活后可正常卸载本应用";
    }


}
