package com.example.huwang.mobilesafe20.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.huwang.mobilesafe20.MainActivity;
import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.activity.HomeActivity;

/**
 * Created by huwang on 2017/4/7.
 */

public class MoSecurityService extends Service {
    private static final int NOTIFICATION_FLAG = 1;
    private NotificationManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        // 重启
        Log.i("zhang", "---守护进程打开Timer--" + System.currentTimeMillis());
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        // 提升 级别
        // startForeground(整型 不为0 , 状态栏通知);//background --foreground
// 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                // bar上显示的提示文字
                .setContentTitle("Notification Title")// 设置在下拉status
                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText("This is the notification message")// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFICATION_FLAG, notify);

        // Notification:状态栏通知对象 包含 标题 图标 描述 运行时间 【绑定程序】
        // PendingIntent: 将要发生 "点击"才发生的Intent
        // Notification notification=new Notification(标题栏上的小图标, 标题栏上的小字, 时间)
//        Notification notification = new Notification(R.drawable.ic_launcher, "快播保护你...", System.currentTimeMillis());
////        // notification.setLatestEventInfo(上下文, 大字标题, 描述 , 绑定的程序);
//////        // 绑定的程序
//////        // <action android:name="ooo.aaa.bbb" />
//////        // <category android:name="android.intent.category.DEFAULT" />
//        Intent intent = new Intent();
//        intent.setAction("ooo.aaa.bbb");
//        intent.addCategory("android.intent.category.DEFAULT");
//        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
//////        notification.setLatestEventInfo(getBaseContext(), "快播卫士", "多敲代码少看电影 多打游戏", pIntent);
//        startForeground(1, notification);// background --foreground

    }

    private CountDownTimer timer = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "-onDestroy--守护进程 销毁---xxx-");
        // 3000 100 30 29 28 .. 1
        // timer=new CountDownTimer(距离时间,时间间隔) {
        timer = new CountDownTimer(3000, 100) {
            int count = 30;

            // 滴答
            @Override
            public void onTick(long millisUntilFinished) {
                count--;
                Log.i("zhang", "onTick" + count + "");
            }

            @Override
            public void onFinish() {
                count--;
                Log.i("zhang", "onFinish" + count + "");
                startService(new Intent(getBaseContext(), MoSecurityService.class));
            }
        };

        timer.start();
        // 清除id为NOTIFICATION_FLAG的通知
        manager.cancel(NOTIFICATION_FLAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
