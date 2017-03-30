package com.example.smsreceiver;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {

    private SMSContentObserver smsContentObserver;
    protected static final int MSG_INBOX = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INBOX:
                    setSmsCode();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsContentObserver = new SMSContentObserver(MainActivity.this, mHandler);
    }

    private void setSmsCode() {
        Log.i("zhang", "收到短信了！");
//        login_et_sms_code.setText("hahhaha");
        Cursor cursor = null;
        // 添加异常捕捉
//        strColumnName=_id                strColumnValue=48                  //短消息序号
//        strColumnName=thread_id          strColumnValue=16                  //对话的序号（conversation）
//        strColumnName=address            strColumnValue=+8613411884805      //发件人地址，手机号
//        strColumnName=person              strColumnValue=null                //发件人，返回一个数字就是联系人列表里的序号，陌生人为null
//        strColumnName=date                strColumnValue=1256539465022        //日期  long型，想得到具体日期自己转换吧！
//        strColumnName=protocol            strColumnValue=0                    //协议
//        strColumnName=read                strColumnValue=1                    //是否阅读
//        strColumnName=status              strColumnValue=-1                  //状态
//        strColumnName=type                strColumnValue=1                    //类型 1是接收到的，2是发出的
//        strColumnName=reply_path_present  strColumnValue=0                    //
//        strColumnName=subject            strColumnValue=null                //主题
//        strColumnName=body                strColumnValue=您好                                                      //短消息内容
//        strColumnName=service_center      strColumnValue=+8613800755500      //短信服务中心号码编号，

        try {
            cursor = getContentResolver().query(
                    Uri.parse("content://sms"),
                    new String[] { "_id", "address", "body", "date" },
                    null, null, "date desc"); //
            if (cursor != null) {
                String body = "";
                Log.i("zhang", "进入if了");
                while (cursor.moveToNext()) {
                    body = cursor.getString(cursor.getColumnIndex("body"));// 在这里获取短信信息
                    long smsdate = Long.parseLong(cursor.getString(cursor
                            .getColumnIndex("date")));
                    long nowdate = System.currentTimeMillis();
                    // 如果当前时间和短信时间间隔超过60秒,认为这条短信无效
                    if (nowdate - smsdate > 60 * 1000) {
                        break;
                    }
                    // 下面检查短信内容
                   if ("#BJ#".equals(body)) {
                       Log.i("zhang","收到报警信号！");
                   }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (smsContentObserver != null) {
            getContentResolver().registerContentObserver(
                    Uri.parse("content://sms/"), true, smsContentObserver);// 注册监听短信数据库的变化
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (smsContentObserver != null) {
            getContentResolver().unregisterContentObserver(smsContentObserver);// 取消监听短信数据库的变化
        }

    }
}
