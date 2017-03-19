package com.example.huwang.mobilesafe20.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;

/**
 * Created by huwang on 2017/3/16.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 动画
        // AlphaAnimation anim=new AlphaAnimation(透明度开始,透明度 结束)
        AlphaAnimation anim = new AlphaAnimation(0.5f, 1.0f);
        // 时长
        anim.setDuration(3000);
        // 作用于哪个范围
        findViewById(R.id.root).startAnimation(anim);

        TextView versiontext = (TextView) findViewById(R.id.versiontext);

        // 获取PackageManger:xml解析工具
        PackageManager pm = getPackageManager();
        // PackageInfo info=pm.getPackageInfo(包名, 标签数据);
        try {
            PackageInfo info = pm.getPackageInfo("com.example.huwang.mobilesafe", 0);
            int localVersionCode = info.versionCode;
            String versionName = info.versionName;
            System.out.print(versionName);
            versiontext.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
         new Thread(){
             public void run() {
                try {
                    Thread.sleep(3000);
                    //意图：激活Activity或者Service的重要对象。
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    //关闭
                    finish();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
             };
         }.start();
    }
}
