package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.AppCacheAdapter;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.utils.AppCacheUtil;
import com.example.huwang.mobilesafe20.utils.AppUtils;
import com.example.huwang.mobilesafe20.utils.FormatUtils;
import com.example.huwang.mobilesafe20.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/9.
 */

public class CacheCleanActivity extends Activity {
    @ViewInject(R.id.list_view)
    ListView list_view;
    @ViewInject(R.id.waiting)
    LinearLayout waiting;
    @ViewInject(R.id.app_name_scan)
    TextView app_name_scan;

    List<AppInfo> cacheList = new ArrayList<AppInfo>();

    AppCacheAdapter adapter;
    @OnClick(R.id.cleanall)
    public void cleanall(View view) {
        AppCacheUtil.deleteAppsCacheFiles(this);
        long freeSize = 0;
        for (AppInfo item : cacheList) {
            freeSize += item.cacheSize;
        }
        ToastUtil.shortToast(getBaseContext(), "本次为你节省" + FormatUtils.formatSize(this, freeSize) + "空间");
        cacheList.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_clean);
        ViewUtils.inject(this);
        // 任务前 显示等待视图
        // 任务中 更新 应用名称
        // 任务结束 隐藏等待视图 显示ListView
        // AppInfo:第二个参数提交到界面要求更新
        new AsyncTask<Void, AppInfo, Void>() {

            protected void onPreExecute() {
                waiting.setVisibility(View.VISIBLE);
            }

            // 刷新
            protected void onProgressUpdate(AppInfo[] values) {
                AppInfo info = values[0];
                app_name_scan.setText("正在扫描缓存:" + info.name);

            }

            // 任务
            @Override
            protected Void doInBackground(Void... params) {
                // 所有已经安装的apk
                List<AppInfo> list = AppUtils.findAll(CacheCleanActivity.this);
                for (AppInfo app : list) {
                    publishProgress(app);// 提交数据到界面 显示正在扫描
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    AppCacheUtil.getAppCacheSize(CacheCleanActivity.this, app);
                    cacheList.add(app);// 才能显示在列表上

                }
                return null;
            }

            protected void onPostExecute(Void result) {
                waiting.setVisibility(View.GONE);
                Log.i("zhang", "显示列表");

                // 集合
                // 适配器
                adapter = new AppCacheAdapter(getBaseContext(), cacheList);
                list_view.setAdapter(adapter);
                // 控件
            }
        }.execute();
    }
}
