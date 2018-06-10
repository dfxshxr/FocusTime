package com.xidian.mktime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.service.LoadAppListService;
import com.xidian.mktime.service.LockService;
import com.apkfuns.logutils.LogUtils;
import com.xidian.mktime.utils.SpUtil;

/**
 * @version V1.0 <接收开机启动广播>
 *
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("开机启动服务....");
        context.startService(new Intent(context, LoadAppListService.class));
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            context.startService(new Intent(context, LockService.class));
        }
    }
}
