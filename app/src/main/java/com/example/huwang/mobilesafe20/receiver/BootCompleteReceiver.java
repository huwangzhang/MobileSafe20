package com.example.huwang.mobilesafe20.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.huwang.mobilesafe20.utils.SmsUtil;

/**
 * Created by huwang on 2017/3/27.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    //处理逻辑，接受信号
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("zhang", "---------------onReceive------------------");
        //sim卡
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        // sim号 1.存 2.重新获取
        String simString1 = sp.getString("safenumber", "");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simString2 = tm.getSimSerialNumber();// 序列号
        if (simString1.equals(simString2)) {
            // 没有出现换卡
            Log.i("zhang", "---没有出现换卡------");
        } else {
            Log.i("zhang", "---出现换卡------");
            String safenumber = sp.getString("safenumber", "5554");
            // 发一条短信 安全号码
            SmsUtil.sendTextSms("5554", "防盗功能:换卡通知");
        }
    }
}
