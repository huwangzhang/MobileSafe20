package com.example.huwang.mobilesafe20.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.huwang.mobilesafe20.db.dao.BlackNumberDao;
import com.example.huwang.mobilesafe20.utils.CallLogUtil;
import com.example.huwang.mobilesafe20.utils.SmsUtil;

import java.lang.reflect.Method;
import java.util.List;

import static android.content.IntentFilter.SYSTEM_HIGH_PRIORITY;

/**
 * Created by huwang on 2017/3/30.
 */

//长期运行于后台的服务，选择用startService
public class SmsCallProtectService extends Service {
    // ①　继承
    // ②　重写
    // ③　配置
    // ④　启动
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private BlackNumberDao dao = null;

    // 接收修改信号的对象 5.0
    private class MyContentObserver extends ContentObserver {
        private String number;

        // 低版本 2.2 2.x
        public MyContentObserver(String numberPhone) {
            super(new Handler());
            number = numberPhone;
        }

        public void onChange(boolean selfChange) {
            Log.i("zhang", "ContentObserver-onChange-2.x");
            CallLogUtil.deleteCallLogByNumber(getBaseContext(), number);
            getContentResolver().unregisterContentObserver(this);
        };

        // 高版本 4.x
        public void onChange(boolean selfChange, android.net.Uri uri) {
            Log.i("zhang", "ContentObserver-onChange-4.x");
            CallLogUtil.deleteCallLogByNumber(getBaseContext(), number);
            getContentResolver().unregisterContentObserver(this);
        };
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zhang", "SmsCallProtectService 创建");

        dao = new BlackNumberDao(getBaseContext());
        // 拦截短信 广播接收者
        receiver = new BroadcastReceiver() {
            // 处理
            @Override
            public void onReceive(Context context, Intent intent) {
                // intent pdus -->SmsMessage
                List<SmsMessage> smslist = SmsUtil.parseSms(intent);
                for (SmsMessage sms : smslist) {
                    String mode = dao.findModeByNumber(sms.getOriginatingAddress());
                    // 0 全部
                    boolean item_blacknumberBoolean = getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("item_blacknumber", false);
                    if (item_blacknumberBoolean) {
                        // 2 短信
                        if ("0".equals(mode) || "2".equals(mode)) {
//                            try {
//                                Thread.sleep(100000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            Log.i("zhang", "被拦截的短信" + sms.getMessageBody() + "   " + sms.getOriginatingAddress());
                            abortBroadcast();
                        }
                        // 敏感词 baoxiojie iso 我是房东 woshifangdong
                        if (sms.getMessageBody().contains("baoxiaojie")) {
                            Log.i("zhang", "垃圾短信" + sms.getMessageBody() + "   " + sms.getOriginatingAddress());
                            abortBroadcast();
                        }
                    }

                }

            }

        };
        // IntentFilter：给组件绑定请求参数
        IntentFilter filter = new IntentFilter();
        // 设置广播类型
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);// 最大优先级
//        Log.i("zhang", String.valueOf(filter.getPriority()));
        // 代码注册
        registerReceiver(receiver, filter);
        // <!-- priority优先级 -->
        // <!-- 短信接收 -->
        // <receiver
        // android:name="com.itheima.mobilesafe70.receiver.CommandReceiver" >
        // <intent-filter android:priority="2147483647" >
        // <action android:name="android.provider.Telephony.SMS_RECEIVED" />
        // </intent-filter>
        // </receiver>
        // Button OnClickListener监听点击
        // 电话状态 PhoneStateListener 监听器
        // 获取通话管理者
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // tm.listen(监听器, 事件);
        psListener = new PhoneStateListener() {
            // 来电号码incomingNumber
            //回调 ：  通话状态 改变  会调用的函数onCallStateChanged
            //监听器 ： 监听 到 通话事件 onCallStateChanged
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                // 2.1 响铃 2.2 通话中 2.3 挂断/空闲
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.i("zhang", "CALL_STATE_IDLE---");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.i("zhang", "CALL_STATE_RINGING---");

                        boolean item_blacknumberBoolean = getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("item_blacknumber", false);
                        if (item_blacknumberBoolean) {
                        Log.i("zhang", "incomingNumber"+incomingNumber);
                            String mode = dao.findModeByNumber(incomingNumber);
                            if ("0".equals(mode) || "1".equals(mode)) {
                                Log.i("zhang", "挂断一个 黑名单上的电话---" + incomingNumber);
                                endCall(incomingNumber);
                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:// OFFHOOK 上海滩电话
                        Log.i("zhang", "CALL_STATE_OFFHOOK---");
                        break;
                }
            }

        };// 监听一般都是接口
        tm.listen(psListener, PhoneStateListener.LISTEN_CALL_STATE);// 添加监听器

    }

    /**
     * 挂断电话
     * @param incomingNumber
     */
    private void endCall(String incomingNumber) {
        // TODO Auto-generated method stub
        // Stub存根
        // ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        try {
            // 获取类的字节文件
            Class<?> clz = Class.forName("android.os.ServiceManager");
            // 静态方法
            Method getService = clz.getMethod("getService", String.class);
            // 调用
            // getService.invoke(实例变量 null静态方法, 参数...);//
            IBinder ibinder = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);//
            Log.i("zhang", ibinder + "");
            ITelephony itephony = ITelephony.Stub.asInterface(ibinder);
            // 调用挂断电话
            itephony.endCall();// 不确定的时间 2 3 4
            // delete from calls where number=10010;
            // CallLogUtil.deleteCallLogByNumber(getBaseContext(),
            // incomingNumber);

            MyContentObserver observer = new MyContentObserver(incomingNumber);

            Uri uri = Uri.parse("content://call_log/calls");
            // Uri uri=Uri.parse("content://call_log/calls/1");
            // getContentResolver().registerContentObserver(访问地址, 是否处理子路径,
            // 观察者实例);
            getContentResolver().registerContentObserver(uri, true, observer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    PhoneStateListener psListener;
    BroadcastReceiver receiver;
    TelephonyManager tm;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "SmsCallProtectService 销毁");
        // 移除注册
        unregisterReceiver(receiver);
        tm.listen(psListener, PhoneStateListener.LISTEN_NONE);// 什么也不监听 移除监听器
    }

}
