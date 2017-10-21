package com.xidian.qualitytime.utils;

import android.widget.Toast;

import com.xidian.qualitytime.LockApplication;


public class ToastUtil {
    private static Toast mToast = null;

    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(LockApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
