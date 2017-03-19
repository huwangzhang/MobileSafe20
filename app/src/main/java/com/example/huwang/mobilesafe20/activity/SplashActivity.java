package com.example.huwang.mobilesafe20.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            String name = getPackageName();
            PackageInfo info = pm.getPackageInfo("com.example.huwang.mobilesafe20", 0);
            localVersionCode = info.versionCode;
            String versionName = info.versionName;
            System.out.println(versionName);
            versiontext.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //获取版本信息
        new Thread(){
            @Override
            public void run() {
                //获取数据
                try {
                    String json = HttpUtil.get("http://192.168.161.226:8080/update70/jsoninfo");
                    System.out.println(json);
                    //解析数据
                    //{versioncode:2,versionName:"黑马福利版2.0",url:"/update70/MobileSafe70.apk",desc:"快快下载，年底可以参与抽奖，奖品：与avXXX见面"}
                    if (json != null) {

                        JSONObject obj = new JSONObject(json);
                        int serverversionCode = obj.getInt("versioncode");
                        String versionName = obj.getString("versionName");
                        String desc = obj.getString("desc");
                        String url = obj.getString("url");
                        if (localVersionCode < serverversionCode) {
                            //
                            Message msg = handler.obtainMessage();
                            msg.what = CODE_UPDATE;
                            handler.sendMessage(msg);
                        } else {

                        }


                    } else {
                        System.out.print("数据异常！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

//         new Thread(){
//             public void run() {
//                try {
//                    Thread.sleep(3000);
//                    //意图：激活Activity或者Service的重要对象。
//                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
//                    //关闭
//                    finish();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//             };
//         }.start();
    }
    private int localVersionCode;
    private static final int CODE_UPDATE = 200;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case CODE_UPDATE:
                    //弹出对话框
                    System.out.println("弹出对话框");

                    break;
            }
        }
    };
}
