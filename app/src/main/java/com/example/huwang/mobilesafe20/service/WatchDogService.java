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
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.huwang.mobilesafe20.activity.AppPasswordEnterActivity;
import com.example.huwang.mobilesafe20.db.dao.LockAppDao;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;

import java.util.List;

/**
 * Created by huwang on 2017/4/8.
 */

public class WatchDogService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ①　看门狗使用Service实现

    // ④　存在：密码解锁 不存在：放行
    private boolean isRunning = false;
    private Thread waitThread = null;

    private LockAppDao dao = null;
    BroadcastReceiver receiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "看门狗    其实可能是单身狗....onDestroy");
        isRunning = false;
        try {
            if (waitThread != null) {
                waitThread.stop();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        unregisterReceiver(receiver);
        getContentResolver().unregisterContentObserver(observer);
    }

    private String letgoPackageName = "";

    private List<String> lockapps = null;
    ContentObserver observer ;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // ContentObserver:内容观察者 用来接收修改信号的对象
        observer = new ContentObserver(new Handler()) {
            // 只执行一个根据版本的新旧来决定
            @Override
            public void onChange(boolean selfChange) {// 旧版本api 2.2 2.3

                Log.i("zhang", "onChange 旧版本api 2.2 2.3");
                //同步集合
                lockapps=dao.findAllLockApps();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {// 新版本api 4.1
                Log.i("zhang", "onChange 新版本api 4.1");
                //同步集合
                lockapps=dao.findAllLockApps();
            }
        };
        Uri uri = Uri.parse("content://" + LockAppDao.class.getName());
        // content://com.itheiam.safe.update
        // content://com.itheiam.safe.update/1 true 有效 false无效
        // getContentResolver().registerContentObserver(访问地址对象 表示信号类型, 子路径 是否有效,
        // 内容观察者对象);
        getContentResolver().registerContentObserver(uri, true, observer);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 事件处理放行
                Log.i("zhang", "看门狗 。。。。接收广播" + intent.getAction());
                if ("com.huwang.action.watchdog.letgo".equals(intent.getAction())) {
                    Log.d("zhang", " 接收到 密码解锁界面的放行广播  ");
                    String packagename = intent.getStringExtra("packagename");
                    letgoPackageName = packagename;
                } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    // 用户可能要操作应用程序
                    startDogWorking();
                } else {
                    isRunning = false;
                    try {
                        if (waitThread != null) {
                            waitThread.stop();
                        }
                    } catch (Exception e) {
                    }
                    Log.i("zhang", "狗 休息一下...");
                }
            }
        };
        // 绑定参数
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.huwang.action.watchdog.letgo");
        filter.addAction(Intent.ACTION_SCREEN_OFF);// 锁屏
        filter.addAction(Intent.ACTION_SCREEN_ON);// 解锁屏幕
        registerReceiver(receiver, filter);

        Log.i("zhang", "看门狗    其实可能是单身狗....oncreate");
        dao = new LockAppDao(getBaseContext());
        lockapps = dao.findAllLockApps();// 整张表的数据载入内存。
        startDogWorking();

    }

    private void startDogWorking() {
        if (isRunning) {
            return;
        }
        Log.i("zhang", "狗开始工作了...");
        isRunning = true;
        waitThread = new Thread() {
            public void run() {
                while (isRunning) {
                    // 查看 栈顶元素。。。
                    // ②　使用无限循环线程 查看栈 顶元素 actiivty-->包名
                    String packagename = ProcessUtils.getCurrAppOnScreen(getBaseContext());
//                    Log.i("zhang", "用户正在操作..." + packagename);
                    // boolean flag = dao.isLock(packagename);//使用数据库访问本质是I/O
                    boolean flag = lockapps.contains(packagename);// 低于内存访问速度
                    // ③　包名查询数据库
                    if (flag)// 1加锁
                    {
                        Log.i("zhang", "要求密码解锁." + packagename);
                        if (letgoPackageName.equals(packagename)) {
                            // 放行
                            Log.i("zhang", "临时 放行 加锁程序" + letgoPackageName);
                        } else // 2.不放行
                        {
                            // 要求密码解锁
                            // service 打开Activity flag 必须要flag_new_task;
                            Intent intent = new Intent(getBaseContext(), AppPasswordEnterActivity.class);// singleTop
                            intent.putExtra("packagename", packagename);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    } else {
                        // 放行。。。
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            };
        };
        waitThread.start();
    }
}
