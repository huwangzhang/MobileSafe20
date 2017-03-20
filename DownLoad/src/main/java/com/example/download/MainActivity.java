package com.example.download;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void download(View view) {
        String url = "http://192.168.31.201:8088/update70/jsoninfo";
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/***.txt";
        System.out.println(savePath);
        //创建核心类
        HttpUtils httpUtils = new HttpUtils(5000);
//        httpUtils.download(文件地址，保存路径， 获取下载参数的回掉)；
        httpUtils.download(url,savePath, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> info) {
                //下载成功
                System.out.println("Onsuccess+" + info.result.getAbsolutePath());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("Onfailure");
            }
        });
    }
}
