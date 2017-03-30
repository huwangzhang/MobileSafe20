package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.FileUtils;
import com.example.huwang.mobilesafe20.utils.ToastUtil;

/**
 * Created by huwang on 2017/3/30.
 */

public class TakePicActivity extends Activity {


    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
        SurfaceView surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        // 控制器
        final SurfaceHolder myholder = surfaceview.getHolder();
        myholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 回调:一些满足事件条件的函数的集合
        myholder.addCallback(new SurfaceHolder.Callback() {
            // 条件:销毁
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i("zhang", "surfaceDestroyed---");

                if (camera != null) {
                    // 停止预览
                    camera.stopPreview();//
                    camera.release();
                    camera = null;
                }
            }

            // 条件:创建
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("zhang", "surfaceCreated---");

                try {
                    camera = Camera.open();// 前后
                    // 设置预览
                    camera.setPreviewDisplay(myholder);
                    // 开始
                    camera.startPreview();

                    Message msg = new Message();
                    msg.what = 200;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("zhang", e.getMessage());
                    ToastUtil.shortToast(getBaseContext(), "本机不支持照相");
                    finish();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });

    }
    public void takePhone(View view) {
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                // 生成数据的时候
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.i("zhang", data + "");
                    String path = "data/data/" + getPackageName() + "/img_" + System.currentTimeMillis() + ".jpg";
                    FileUtils.saveImg(path, data);
                    // 预览
                    camera.startPreview();
                }
            });
        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 200) {
                Log.i("zhang", "--handleMessage---");
                takePhone(null);
                finish();
            }
        }

        ;
    };
}
