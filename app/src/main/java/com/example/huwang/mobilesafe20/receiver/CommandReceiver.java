package com.example.huwang.mobilesafe20.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.activity.TakePicActivity;
import com.example.huwang.mobilesafe20.service.GpsService;
import com.example.huwang.mobilesafe20.utils.AdminUtils;
import com.example.huwang.mobilesafe20.utils.MediaUtils;

/**
 * Created by huwang on 2017/3/28.
 */

public class CommandReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ToastUtil.shortToast(context, "hahhahahahh");

        //pdus短信单位pdu
        //解析短信内容
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : pdus) {
            //封装短信参数的对象
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
            String number = sms.getOriginatingAddress();
            String body = sms.getMessageBody();
            if ("#BJ#".equals(body.trim())) {//BJ
                Log.i("zhang", "BJ");
                MediaUtils.play(context, R.raw.alarm);
                abortBroadcast();
            } else if ("#PZ#".equals(body.trim())) {
                Log.i("zhang", "PZ");
                Intent intent2=new Intent(context,TakePicActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//服务调用
                context.startActivity(intent2);
                abortBroadcast();
            } else if ("#SP#".equals(body.trim())) {
                Log.i("zhang", "SP");
                AdminUtils.lockScreen(context);
                abortBroadcast();
            } else if ("#XH#".equals(body.trim())) {
                Log.i("zhang", "XH");
                AdminUtils.wipeData(context);
                abortBroadcast();
            } else if ("#GPS#".equals(body.trim())) {
                Log.i("zhang", "GPS");
                context.startService(new Intent(context, GpsService.class));
                //忽略短信
                abortBroadcast();
            }
        }
    }
}
