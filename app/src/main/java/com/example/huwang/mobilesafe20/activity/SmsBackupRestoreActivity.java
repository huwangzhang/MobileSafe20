package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.SmsUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/4/4.
 */

public class SmsBackupRestoreActivity extends Activity {
    @ViewInject(R.id.progresstext)
    TextView progresstext;
    @ViewInject(R.id.progressbar)
    ProgressBar progressbar;
    @ViewInject(R.id.backup)
    Button backup;
    @ViewInject(R.id.restore)
    Button restore;

    @OnClick(R.id.restore)
    public void restore(View view)
    {

        //三个阶段

        new AsyncTask<Void, Integer, Void>(){

            //主线程
            //任务前  : 初始化控件

            protected void onPreExecute() {
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setMax(100);
                progressbar.setProgress(0);
                progresstext.setText("正在还原0条短信");
                backup.setEnabled(false);
                restore.setEnabled(false);
            };
            protected  void onProgressUpdate(Integer[] values) {
                progressbar.setProgress(values[0]);
                progresstext.setText("正在还原"+values[0]+"条短信");
            };
            //任务后:  完成提示
            protected void onPostExecute(Void result) {
                progressbar.setProgress(0);
                progressbar.setVisibility(View.GONE);
                progresstext.setText("短信恢复" + progressValue + "条短信");
                backup.setEnabled(true);
                restore.setEnabled(true);
            };
            int progressValue=0;
            //任务中 :逐条备份 +列新界面

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    SmsUtil.OnProgressListener listener=new SmsUtil.OnProgressListener() {
                        @Override
                        public void onStart(int max) {
                            Log.i("wzx", "-onStart--"+max);
                            progressbar.setMax(max);//handler
                        }
                        @Override
                        public void onProgressChage(int progress, int max) {
                            Log.i("wzx", "-onProgressChage--"+progress);
                            progressValue=progress;
                            publishProgress(progress);//-->onProgressupdate 主线程

                        }
                    };
                    SmsUtil.restore(getBaseContext(), listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();


    }

    @OnClick(R.id.backup)
    public void backup(View view) {

        // 第二个参数 publishProgress
        // AsyncTask：1.自动执行 2.有执行顺序
        new AsyncTask<Void, Integer, Void>() {
            int progress=0;
            // 任务前
            protected void onPreExecute() {
                Log.i("wzx", "onPreExecute" + Thread.currentThread().getName());
                // 提示
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setMax(100);
                progressbar.setProgress(0);
                progresstext.setText("正在备份0条短信");
                backup.setEnabled(false);
                restore.setEnabled(false);
            };


            // 任务中
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("wzx", "doInBackground" + Thread.currentThread().getName());
                ///1.使用监听器的角度
                try {
                    //OnClickListener
                    //OnItemClickListener
                    //TextWatcher
                    //....
                    SmsUtil.OnProgressListener listener=new SmsUtil.OnProgressListener(){
                        @Override
                        public void onProgressChage(int progress, int max) {
                            publishProgress(progress);
                        }
                        /**
                         * max 短信总数
                         */
                        @Override
                        public void onStart(int max) {
                            progressbar.setMax(max);
                        }

                    };
                    SmsUtil.backup(getBaseContext(), listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 真实的数据
                return null;
            }


            //			// 进度列新
            protected void onProgressUpdate(Integer[] values) {
                progressbar.setProgress(values[0]);
                progresstext.setText("正在备份" + values[0] + "条短信");
                Log.i("wzx", "onProgressUpdate" + Thread.currentThread().getName() + "   " + values[0]);
            };

            // 任务后
            protected void onPostExecute(Void result) {
                Log.i("wzx", "onPostExecute" + Thread.currentThread().getName());
                progressbar.setProgress(0);
                progressbar.setVisibility(View.GONE);
                progresstext.setText("备份完成" + progress + "条短信");
                backup.setEnabled(true);
                restore.setEnabled(true);
            };

        }.execute();// execute是创建线程 new Thread().start();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_backup_restore);

        ViewUtils.inject(this);
        progressbar.setVisibility(View.GONE);
        progresstext.setText("2015年2月7日");

    }
}
