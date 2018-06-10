package com.xidian.mktime.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.xidian.mktime.LockApplication;
import com.xidian.mktime.R;
import com.xidian.mktime.receiver.DeviceAdminBroadcastReceiver;

import java.lang.reflect.Method;

import static android.content.Context.DEVICE_POLICY_SERVICE;
import static com.xidian.mktime.utils.ToastUtil.showToast;

/**
 * 用途
 *
 * @version V1.0 <跳转到手机设置界面>
 * @FileName: com.xidian.qualitytime.utils.MobileInfoUtils.java
 * @date: 2017-04-16 16:43
 */
public class MobileInfoUtils {

        /** * Get Mobile Type * * @return */
        private static String getMobileType() {
            return Build.MANUFACTURER;
        }
        /** * GoTo Open Self Setting Layout * Compatible Mainstream Models 兼容市面主流机型 * * @param context */
        public static void jumpStartInterface(Context context) {
            Intent intent = new Intent();
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e("HLQ_Struggle", "当前手机型号为：" + getMobileType());
                ComponentName componentName = null;
                if (getMobileType().equals("Xiaomi")) {
                    componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
                } else if (getMobileType().equals("Letv")) {
                    intent.setAction("com.letv.android.permissionautoboot");
                } else if (getMobileType().equals("samsung")) {
                    componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
                } else if (getMobileType().equals("HUAWEI")) {
                    componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                } else if (getMobileType().equals("vivo")) {
                    intent=(context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure"));
                } else if (getMobileType().equals("Meizu")) {
                    componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");
                } else if (getMobileType().equals("OPPO")) {
                    componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
                } else if (getMobileType().equals("ulong")) {
                    componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
                } else {
                    showToast(context.getString(R.string.not_found_setting_screen));
                }
                showToast(context.getString(R.string.setting_auto_start));
                if(componentName!=null)
                {intent.setComponent(componentName);}
                context.startActivity(intent);
            } catch (Exception e) {
                showToast(context.getString(R.string.not_found_setting_screen));
                e.printStackTrace();
            }
        }


    public static void jumpClearInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("HUAWEI")) {
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            }else if (getMobileType().equals("vivo")) {
                componentName = new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
            } else {
                showToast(context.getString(R.string.not_found_setting_screen));
            }
            showToast(context.getString(R.string.setting_no_clear));
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            showToast(context.getString(R.string.not_found_setting_screen));
            e.printStackTrace();
        }
    }


    public static void jumpDeviceAdminInterface(Context context) {

        try {

            DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
            ComponentName mDeviceAdmin = new ComponentName(context,DeviceAdminBroadcastReceiver.class);
            if (!manager.isAdminActive(mDeviceAdmin)) {
                //如果不是，则构建一个intent，action参数的意思为添加一个设备管理者
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,R.string.device_admin_description);
                context.startActivity(intent);
            }
            else{
                manager.removeActiveAdmin(mDeviceAdmin);
                showToast(context.getString(R.string.remove_device_admin_success));
            }

        } catch (Exception e) {//抛出异常就直接打开设置页面
            showToast(context.getString(R.string.not_found_setting_screen_2));
            Intent intent=new Intent();
            intent.setComponent(new ComponentName("com.android.settings","com.android.settings.DeviceAdminSettings"));
            context.startActivity(intent);
            e.printStackTrace();
        }
    }


    /*
    * 通过反射的方法打开最近运行的任务
    * Android7.0以下有效
    * */
    @SuppressWarnings("unchecked")
    public static void showRecentlyApp() {
        Class serviceManagerClass;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                showToast(LockApplication.getContext().getString(R.string.add_white_list_toast_tip1));
            }else {
                serviceManagerClass = Class.forName("android.os.ServiceManager");
                Method getService = serviceManagerClass.getMethod("getService",
                        String.class);
                IBinder retbinder = (IBinder) getService.invoke(
                        serviceManagerClass, "statusbar");
                Class statusBarClass = Class.forName(retbinder
                        .getInterfaceDescriptor());
                Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                        "asInterface", IBinder.class).invoke(null,
                        new Object[]{retbinder});
                Method clearAll = statusBarClass.getMethod("toggleRecentApps");
                clearAll.setAccessible(true);
                clearAll.invoke(statusBarObject);
                showToast(LockApplication.getContext().getString(R.string.add_white_list_toast_tip2));
            }

        } catch (Exception e) {
            showToast(LockApplication.getContext().getString(R.string.not_found_setting_screen_2));
            e.printStackTrace();
        }
    }



}
