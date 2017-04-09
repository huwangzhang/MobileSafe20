package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.db.dao.VirusDao;
import com.example.huwang.mobilesafe20.utils.AppUtils;
import com.example.huwang.mobilesafe20.utils.MD5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/9.
 */

public class KillVirusActivity extends Activity {
    @ViewInject(R.id.img_act)
    ImageView img_act;
    @ViewInject(R.id.progresstext)
    TextView progress_text;
    @ViewInject(R.id.scan_status_text)
    TextView scan_status_text;
    @ViewInject(R.id.progressbar)
    ProgressBar progressbar;
    @ViewInject(R.id.report_layout)
    LinearLayout report_layout;
    @ViewInject(R.id.kill)
    Button kill;

    @OnClick(R.id.kill)
    public void kill(View view) {
        for (AppInfo info : virusList) {
            AppUtils.uninstallApk(getBaseContext(), info);
        }
        scan_status_text.setText("已经删除病毒:"+virusList.size());
        virusList.clear();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        img_act.clearAnimation();
    }

    /**
     * 病毒集合
     */
    private List<AppInfo> virusList = new ArrayList<AppInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_virus);
        ViewUtils.inject(this);
        // 动画
        RotateAnimation anim = new RotateAnimation//
                (0, 360,// 角度
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);// 中点

        // 时长
        anim.setDuration(700);
        // 次数
        anim.setRepeatCount(Integer.MAX_VALUE);
        img_act.startAnimation(anim);

        // 时间 界面刷新
        // 杀毒任务
        // 任务开始前: progress 0 1.正在初始化8核引擎 2.状态
        // 任务中 ：100 查完一条
        // 任务完成:显示结果
        new AsyncTask<Void, AppInfo, Void>() {
            // 任务开始前: progress 0 1.正在初始化8核引擎 2.状态
            protected void onPreExecute() {
                progress_text.setTextColor(Color.parseColor("#000000"));
                progressbar.setMax(100);
                progressbar.setProgress(0);
                progress_text.setText("正在初始化8核引擎 ...");
                scan_status_text.setText("正在初始化8核引擎 ...");

            };

            // 获取
            protected void onProgressUpdate(AppInfo[] values) {
                progressbar.setProgress(progress);
                progressbar.setMax(maxProgress);
                progress_text.setText("正在扫描" + values[0].name);
                scan_status_text.setText("扫描中...");
                // 布局报告 item 正常/病毒
                AppInfo info = values[0];
                addReportItem(info);
            }

            private int progress = 0;
            private int maxProgress = 100;

            // 任务中。。
            @Override
            protected Void doInBackground(Void... params) {
                virusList.clear();
                // for (int i = 0; i < 100; i++) {
                // AppInfo info = new AppInfo();
                // info.name = "快播" + i;
                // ++progress;
                // publishProgress(info);// 提交参数给页面
                // if (i == 20) {
                // info.desc = "这是一个av病毒";
                // virusList.add(info);
                // }
                // try {
                // Thread.sleep(50);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                //
                // }
                // ①　遍历所有已经安装apk
                // ②　生成 apk 对应的md5
                // ③　编写 Dao查询病毒库
                // ④　调用删除(Intent root)
                List<AppInfo> infos = AppUtils.findAll(getBaseContext());
                maxProgress = infos.size();
                for (AppInfo info : infos) {
                    try {
                        Log.i("zhang", info.name + "    md5:" + MD5Utils.getMd5FromFile(info.path));
                        String md5 = MD5Utils.getMd5FromFile(info.path);
                        String desc = VirusDao.getVirusDesc(md5);
                        info.desc = desc;
                        if (info.desc == null) {
                            // 正常
                        } else {
                            // 病毒
                            virusList.add(info);
                        }
                        ++progress;
                        publishProgress(info);// 把扫描信息提交给界面 显示
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return null;
            }

            // 任务完成
            protected void onPostExecute(Void result) {
                progressbar.setProgress(progress);
                if (virusList.size() == 0) {
                    progress_text.setText("扫描完成");
                    progress_text.setTextColor(Color.parseColor("#000000"));
                } else {
                    progress_text.setText("发现病毒！");
                    progress_text.setTextColor(Color.parseColor("#D14941"));
                    kill.setVisibility(View.VISIBLE);
                }

                scan_status_text.setText("总共扫描" + progress + "个程序,病毒 " + virusList.size() + "个");
                img_act.clearAnimation();
            };
        }.execute();
    }

    public void addReportItem(AppInfo info) {
        // layout---View 设置值
        View view = null;
        if (info.desc == null)// 正常
        {
            view = View.inflate(getBaseContext(), R.layout.view_scan_report_item, null);

        } else// 病毒
        {
            view = View.inflate(getBaseContext(), R.layout.view_scan_report_item_virus, null);
        }
        TextView descText = (TextView) view.findViewById(R.id.app_name);
        ImageView app_icon = (ImageView) view.findViewById(R.id.app_icon);

        app_icon.setImageDrawable(info.icon);
        descText.setText(info.desc == null ? ("正常:" + info.name) : ("病毒:" + info.name + " " + info.desc));
        // layout.addView();
        report_layout.addView(view, 0);// 保存在第一行
    }

}
