package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/4/8.
 */

public class AppPasswordEnterActivity extends Activity {
    private String packagename = "";
    @ViewInject(R.id.app_icon)
    ImageView app_icon;
    @ViewInject(R.id.app_name)
    TextView app_name;
    @ViewInject(R.id.et_password)
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);
        ViewUtils.inject(this);
        // 获取激活自己的Intent
        Intent intent = getIntent();
        packagename = intent.getStringExtra("packagename");

        // 通PackageManager查询 图标 应用名
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packagename, 0);
            // <application android:logo >
            ApplicationInfo applicatoinInfo = info.applicationInfo;
            // 图标
            Drawable icon = applicatoinInfo.loadIcon(pm);
            // 应用名
            String appName = applicatoinInfo.loadLabel(pm).toString();
            app_icon.setImageDrawable(icon);
            app_name.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick(R.id.unlock)
    public void click(View view) {
        // 123 与输入框 一致就解锁
        String inputString = et_password.getText().toString().trim();
        Log.i("zhang", inputString);
        if ("123".equals(inputString)) {
            // 通知看门狗 把pakcagename 放行
            Intent intent = new Intent();
            intent.setAction("com.huwang.action.watchdog.letgo");
            intent.putExtra("packagename", packagename);
            // 发送广播
            sendBroadcast(intent);

            finish();
        }
    }

    // true 消费事件 因为自己做了处理 不需要 系统处理
    // false 系统处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //返回Home
//			 <intent-filter>
//             <action android:name="android.intent.action.MAIN" />
//             <category android:name="android.intent.category.HOME" />
//             <category android:name="android.intent.category.DEFAULT" />
//             <category android:name="android.intent.category.MONKEY"/>
//         </intent-filter>
            Intent intent=new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.MONKEY");

            startActivity(intent);
            finish();
            return true;// 自己处理不要系统
        }

        return super.onKeyDown(keyCode, event);
    }
}
