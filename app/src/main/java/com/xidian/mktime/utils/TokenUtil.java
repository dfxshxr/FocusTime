package com.xidian.mktime.utils;

import android.provider.Settings;

import com.xidian.mktime.base.AppConstants;
import com.xidian.mktime.bean.Billing;
import com.xidian.mktime.bean.StudyStatistics;

import org.litepal.crud.DataSupport;

public class TokenUtil {


    public static void Billing(int amount, String note) {
        Billing billing = new Billing();
        billing.setMilliseconds(System.currentTimeMillis());
        billing.setToken(amount);
        billing.setNote(note);
        billing.save();
    }

    public static int getTokenSum() {
        return DataSupport.sum(Billing.class,"token",int.class);
    }

    public static int getTodayTokenSum() {
        return DataSupport.where("milliseconds>=? AND milliseconds<?",
                String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis())),
                String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis())+24*60*60*1000)
        ).sum(Billing.class,"token",int.class);
    }

    public static boolean checkSignIn() {
        return 0==DataSupport
                .where("milliseconds>=? AND milliseconds<? AND note LIKE '%打卡%'",
                        String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis())),
                        String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis())+24*60*60*1000)
                )
                .count(Billing.class);
    }
    public static boolean checkLastSignIn() {
        return 0<DataSupport
                .where("milliseconds>=? AND milliseconds<? AND note LIKE '%打卡%'",
                        String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis())-24*60*60*1000),
                        String.valueOf(DataUtil.getStartTimeOfDay(System.currentTimeMillis()))
                )
                .count(Billing.class);
    }

    public static double earlySignInBonus() {

        long second = System.currentTimeMillis()-DataUtil.getStartTimeOfDay(System.currentTimeMillis());
        if(second>5*1000*60*60){
            return 1.6;
        }else if(second>5.5*1000*60*60) {
            return 1.5;
        }else if(second>6.0*1000*60*60) {
            return 1.4;
        }else if(second>6.5*1000*60*60) {
            return 1.3;
        }else if(second>7.0*1000*60*60) {
            return 1.2;
        }else if(second>7.5*1000*60*60) {
            return 1.1;
        }
        return 1;
    }

}
