package com.example.huwang.mobilesafe20.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.db.dao.AddressDao;
import com.example.huwang.mobilesafe20.utils.ToastUtil;



/**
 * Created by huwang on 2017/4/4.
 */

public class AddressShowService extends Service {
    private View view;
    private TelephonyManager tm;
    private PhoneStateListener listener;
    private AddressDao dao = null;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zhang", "AddressShowService服务创建");
        dao = new AddressDao(this);
        //获取通话管理器
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //1.响铃2.通话3.挂断
        listener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:  //挂断
                        Log.i("zhang", "AddressShowService----dismiss");
                        if (view != null){
                            ToastUtil.dismissInWindow(getBaseContext(), view);
                            view = null;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: //响铃
                        Log.i("zhang", "AddressShowService----CALL_STATE_RINGING" + incomingNumber);
                        String address = dao.findAddressByNumber(incomingNumber);
//                        ToastUtil.shortToast(getBaseContext(), address);
                        Log.i("zhang", "AddressShowService---------------CALL_STATE_RINGING" + incomingNumber);
                        showAddress(address);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;

                }
            }
        };
        //添加监听器
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        // --------------------------------外拨-----------------
        receiver = new BroadcastReceiver() {
            // 处理接收到的广播
            @Override
            public void onReceive(Context context, Intent intent) {
                String outNumber = getResultData();// getResultData返回外拨电话
                Log.i("zhang", "ACTION_NEW_OUTGOING_CALL 外拨 " + outNumber);
                String address = dao.findAddressByNumber(outNumber);
                // ToastUtil.longToast(getBaseContext(), address);
                showAddress(address);
            }
        };
        // 创建 IntnetFilter: 绑定广播的类型
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);// 外拨电话广播
        registerReceiver(receiver, intentFilter);
    }
    private int[]  styleBackGrounds=new int[]{
            R.drawable.call_locate_white,//
            R.drawable.call_locate_orange,//
            R.drawable.call_locate_blue,//
            R.drawable.call_locate_gray,//
            R.drawable.call_locate_green//
    };
    private void showAddress(String address) {

        view = ToastUtil.showInWindow(getBaseContext(), R.layout.view_show_address);
        //添加 touch
        //OnTouchListener触摸事件
        view.setOnTouchListener(new View.OnTouchListener() {
            private float xPosition;
            private float yPosition;
            //MotionEvent封装了事件的坐标 操作类型 相关参数
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        xPosition=event.getRawX();
                        yPosition=event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://拖动

                        float newxPosition=event.getRawX();
                        float newyPosition=event.getRawY();

                        float offX=newxPosition-xPosition;
                        float offY=newyPosition-yPosition;
                        //更改view x y
//					addView(view,params) x y
                        WindowManager.LayoutParams params=(WindowManager.LayoutParams) view.getLayoutParams();
                        //坐标偏移量
                        params.x+=offX;
                        params.y+=offY;
                        //0 ,0
                        if(params.x<0)
                        {
                            params.x=0;
                        }
                        if(params.y<0)
                        {
                            params.y=0;
                        }
                        //修改位置
                        WindowManager wm=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        int  SCREEN_WIDHT=wm.getDefaultDisplay().getWidth();//获取屏幕宽
                        int  SCREEN_HEIGHT=wm.getDefaultDisplay().getHeight();//获取屏幕高
                        if(params.x>(SCREEN_WIDHT-params.width))//layout_width
                        {
                            params.x=SCREEN_WIDHT-params.width;
                        }
                        if(params.y>(SCREEN_HEIGHT-params.height))//layout_width
                        {
                            params.y=SCREEN_HEIGHT-params.height;
                        }
                        Log.i("zhang", "x max"+(SCREEN_WIDHT-params.width)+" y max"+(SCREEN_HEIGHT-params.height));

                        //
//					params.height;
//					params.width;
                        wm.updateViewLayout(view, params);
                        xPosition=event.getRawX();
                        yPosition=event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://松开
                        xPosition=event.getRawX();
                        yPosition=event.getRawY();
                        break;
                }
                return false;
            }
        });
        int style_index = getSharedPreferences("config", Context.MODE_PRIVATE).getInt("style_index", 0);
        //必须从sp
        Drawable background=getResources().getDrawable(styleBackGrounds[style_index]);
        view.setBackgroundDrawable(background);//2.x
        TextView addressView = (TextView) view.findViewById(R.id.address);
        addressView.setText(address);
        //延时移除
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (view != null){
                    ToastUtil.dismissInWindow(getBaseContext(), view);
                    view = null;
                }
            }
        }, 10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "AddressShowService销毁");
        //移除监听器
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
