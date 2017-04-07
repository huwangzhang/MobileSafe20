package com.example.huwang.mobilesafe20.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.receiver.MyWidagetReceiver;
import com.example.huwang.mobilesafe20.utils.FormatUtils;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huwang on 2017/4/7.
 */

public class WidgetService extends Service{
    private Timer timer=null;
    private TimerTask task=null;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i("zhang", "WidgetService---创建");
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
//				SimpleDateFormat:格式 化时间的对象
                //2015年12月25日 18:00:11

                int processCount= ProcessUtils.getProcessCount(getBaseContext());
                long availablemem=ProcessUtils.getAvailableMem(getBaseContext());
                RemoteViews remoteView=new RemoteViews(getPackageName(), R.layout.view_appwidget_proceess_clean);
                //setTextViewText  实现 findViewById   setText
                remoteView.setTextViewText(R.id.process_count,"正在运行的软件:"+processCount);
                remoteView.setTextViewText(R.id.mem,"可用内存:"+ FormatUtils.formatSize(getBaseContext(), availablemem));

                //PendingIntent:点击式的Intent
                //  setOnClickPendingIntent          实现 findViewById setOnClick
                Intent intent=new Intent();
                intent.setAction("com.example.huwang.mobilesafe20.action.CLEAN");
                PendingIntent pIntent=PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
                remoteView.setOnClickPendingIntent(R.id.clean, pIntent);
                //com.itheima.widget.receiver.MyWidagetReceiver
//				RemoteView:不是真正的View 用于进程间的对view操作。
                ComponentName name=new ComponentName(getPackageName(),MyWidagetReceiver.class.getName());

                AppWidgetManager.getInstance(getBaseContext()).updateAppWidget(name, remoteView);
//				AppWidgetManager.getInstance(getBaseContext()).updateAppWidget(更新的widget的类名, RemoteView :);

            }
        };
        timer.schedule(task, 0, 1000);//每隔一秒执行一次
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("zhang", "WidgetService---销毁");
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
