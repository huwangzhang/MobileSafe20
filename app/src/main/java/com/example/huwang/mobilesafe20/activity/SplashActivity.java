package com.example.huwang.mobilesafe20.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.HttpUtil;
import com.example.huwang.mobilesafe20.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by huwang on 2017/3/16.
 */

public class SplashActivity extends Activity {
    private TextView process_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        process_text = (TextView)findViewById(R.id.progress_text);
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
        new Thread() {
            @Override
            public void run() {
                //获取数据
                try {
                    String json = HttpUtil.get("http://192.168.161.226:8088/update70/jsoninfo");
//                    String json = HttpUtil.get("http://192.168.31.201:8088/update70/jsoninfo");
                    System.out.println(json);
                    //解析数据
                    //{versioncode:2,versionName:"黑马福利版2.0",url:"/update70/MobileSafe70.apk",desc:"快快下载，年底可以参与抽奖，奖品：与avXXX见面"}
                    if (json != null) {

                        JSONObject obj = new JSONObject(json);
                        int serverversionCode = obj.getInt("versioncode");
                        String versionName = obj.getString("versionName");
                        desc = obj.getString("desc");
                        url = obj.getString("url");
                        if (localVersionCode < serverversionCode) {
                            //有新版本
                            Message msg = handler.obtainMessage();
                            msg.what = CODE_UPDATE;
                            handler.sendMessage(msg);
                        } else {
                            //无更新
                            Message msg = handler.obtainMessage();  //
                            msg.what = CODE_NOUPDATE;
                            handler.sendMessage(msg);
                        }
                    } else {
                        System.out.print("网络异常！");
                        Message msg = handler.obtainMessage();  //
                        msg.what = CODE_NET_ERROR;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    Message msg = handler.obtainMessage();// obtainMessage查找有没有旧的有就重用，没用就创建
                    msg.what = CODE_NET_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private String savePath;
    private String desc;
    private String url;
    private int localVersionCode;
    private static final int CODE_UPDATE = 200;
    private static final int CODE_NOUPDATE = -200;
    private static final int CODE_NET_ERROR = -100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE:
                    showUpdateDialog();

                    break;
                case CODE_NOUPDATE:
                    System.out.println("当前已是最新版本");
                    ToastUtil.shortToast(getBaseContext(), "当前是最新版本");
                    //进入主页
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    System.out.println("网络出错");
                    ToastUtil.shortToast(getBaseContext(), "网络出错");
                    enterHome();
                    break;
            }
        }
    };
    /**
     * 进入主页
     */
    private void enterHome() {

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showUpdateDialog() {
        //弹出对话框
        System.out.println("弹出对话框");
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        //设置标题
        builder.setTitle("更新提示");
        //更新提示
        builder.setMessage(desc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() { //响应用户操作
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载安装
                download("http://192.168.161.226:8088" + url);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //直接进入主页
                enterHome();
            }
        });
        Dialog dialog = builder.create();
        //返回键
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                enterHome();
            }
        });
        dialog.show();
    }

    public void download(String url) {
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/***.exe";
        System.out.println(url);
        //创建核心类
        HttpUtils httpUtils = new HttpUtils(5000);
//        httpUtils.download(文件地址，保存路径， 获取下载参数的回掉)；
        //RequestCallBack获取下载过程中的参数
        httpUtils.download(url,savePath, new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                //显示在界面上
                System.out.println(current + "/" + total);
                process_text.setText(current + "/" + total);
            }

            @Override
            public void onSuccess(ResponseInfo<File> info) {
                //下载成功
                System.out.println("Onsuccess+" + info.result.getAbsolutePath());
                //弹出安装对话框
                showInstallDialog(SplashActivity.this);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("Onfailure");
                //进入主页
            }
        });
    }
    /*
    弹出安装对话框
     */
    private void showInstallDialog (Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        //设置标题
        builder.setTitle("安装提示");
        //更新提示
        builder.setMessage("当前是最前版本,请安装...");
        builder.setPositiveButton("确定安装", new DialogInterface.OnClickListener() { //响应用户操作
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("安装...");
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");// 动作
                intent.addCategory("android.intent.category.DEFAULT");// 类别
                Uri data = Uri.parse("file://" + savePath);
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                // startActivity(intent); 不能调用onActivityResult在处理完毕之后
                startActivityForResult(intent, 0);
            }
        });
        builder.setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.out.println("残忍拒绝");
                enterHome();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
    /**
     * 调用onActivityResult在处理完毕之后 在请求页面关闭之后
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }
}
