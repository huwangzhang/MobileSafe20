package com.example.smsreceiver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import static com.example.smsreceiver.MainActivity.MSG_INBOX;

/**
 * Created by huwang on 2017/3/29.
 */

public class SMSContentObserver extends ContentObserver {
    private static final int MSG = 1;
    private Context mContext;
    private Handler mHandler; // 更新UI
    public SMSContentObserver(Context mContext,
                              Handler mHandler) {
        super(mHandler); // 所有ContentObserver的派生类都需要调用该构造方法
        this.mContext = mContext;
        this.mHandler = mHandler;
    }
    /**
     * 当观察到的Uri发生变化时，回调该方法去处理。所有ContentObserver的派生类都需要重载该方法去处理逻辑
     * selfChange:回调后，其值一般为false，该参数意义不大
     */
    @Override
    public void onChange(boolean selfChange) {
        // TODO Auto-generated method stub
        super.onChange(selfChange);
        mHandler.obtainMessage(MSG, "收到了短信！！！").sendToTarget();
    }
}
