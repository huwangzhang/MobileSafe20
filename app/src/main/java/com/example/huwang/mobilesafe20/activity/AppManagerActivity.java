package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.AppManagerAdapter;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.utils.AppUtils;
import com.example.huwang.mobilesafe20.utils.FormatUtils;
import com.example.huwang.mobilesafe20.utils.ToastUtil;
import com.example.huwang.mobilesafe20.utils.WindowUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by huwang on 2017/4/4.
 */

public class AppManagerActivity extends Activity {
    private List<AppInfo> userapps = new ArrayList<AppInfo>();
    private List<AppInfo> sysapps = new ArrayList<AppInfo>();

    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.inner_space)
    TextView inner_space;
    @ViewInject(R.id.sd_space)
    TextView sd_space;
    AppManagerAdapter adapter;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        long innerSpace = Environment.getDataDirectory().getFreeSpace();// 手机内部
        long sdSpace = Environment.getExternalStorageDirectory().getFreeSpace();// sd
        inner_space.setText("手机剩余:" + FormatUtils.formatSize(this, innerSpace));
        sd_space.setText("sd卡剩余" + FormatUtils.formatSize(this, sdSpace));
    }

    BroadcastReceiver receiver;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 广播
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 有序的 支持 abortBroadcast();
                // 无序的 abortBroadcast就报错
                ToastUtil.shortToast(getBaseContext(), "卸载成功!");
                Log.i("zhang", "刷新");
                queryAppList();
                // 关闭菜单
                if (windowPop != null) {
                    windowPop.dismiss();
                    windowPop = null;
                }
            }
        };
        // 参数
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");// 【 只有这个特殊】
        registerReceiver(receiver, filter);

        setContentView(R.layout.activity_app_manager);
        ViewUtils.inject(this);

        queryAppList();

    }

    /**
     * 查询
     */
    private void queryAppList() {
        findViewById(R.id.view_upcase).setVisibility(View.GONE);
        // 任务前:显示等待视图,把集合清空
        // 任务中:获取数据 网络数据 多数据
        // 任务后：使用复杂列表显示 隐藏等待视图 1.setAdapter 2.notifyDateSetChange();
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                userapps.clear();
                sysapps.clear();
                findViewById(R.id.waiting).setVisibility(View.VISIBLE);
            }

            protected void onPostExecute(Void result) {
                findViewById(R.id.waiting).setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new AppManagerAdapter(getBaseContext(), userapps, sysapps);
                    listview.setAdapter(adapter);
                    addListeners();
                } else {
                    adapter.notifyDataSetChanged();// 减少adpater创建
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                List<AppInfo> list = AppUtils.findAll(getBaseContext());
                for (AppInfo info : list) {
                    if (info.isSys) {
                        // 添加系统应用程序列表
                        sysapps.add(info);
                    } else {
                        //用户应用程序
                        if (!getPackageName().equals(info.packageName)) {
                            userapps.add(info);
                        }

                    }
                }

                Comparator<AppInfo> cmp = new Comparator<AppInfo>() {
                    // 比较方法
                    @Override
                    public int compare(AppInfo lhs, AppInfo rhs) {
                        String lhsPinyin = FormatUtils.toPinYin(lhs.name).toUpperCase();
                        String rhsPinyin = FormatUtils.toPinYin(rhs.name).toUpperCase();
                        return lhsPinyin.compareTo(rhsPinyin);
                    }
                };
                Collections.sort(sysapps, cmp);
                Collections.sort(userapps, cmp);
                // //
                // for (int i = 0; i < 5; i++) {
                // AppInfo info = new AppInfo();
                // info.name = "快播" + i;
                // info.size = 201423;
                // info.icon = getResources().getDrawable(R.drawable.app);
                // info.location = "手机内部";
                // userapps.add(info);
                // }
                // // 初始化数据
                // for (int i = 0; i < 15; i++) {
                // AppInfo info2 = new AppInfo();
                // info2.name = "看电影 " + i;
                // info2.size = 20142223;
                // info2.icon =
                // getResources().getDrawable(R.drawable.ic_launcher);
                // info2.location = "手机内部";
                // sysapps.add(info2);
                // }
                // try {
                // Thread.sleep(3000);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                return null;
            }
        }.execute();
        // 初始化数据
    }

    PopupWindow windowPop;

    private void addListeners() {

        listview.setOnItemClickListener(new ListView.OnItemClickListener() {

            // position下标
            // view点中的行视图
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (windowPop != null) {
                    windowPop.dismiss();
                    windowPop = null;
                }
                int[] location = new int[2];
                view.getLocationOnScreen(location);// 获坐标
                View popView = View.inflate(getBaseContext(), R.layout.view_menu_pop, null);
                windowPop = WindowUtils.showPopWindow(AppManagerActivity.this, view, popView, location[0] + 60, location[1] + 10);

                TextView setting = (TextView) popView.findViewById(R.id.setting);
                TextView uninstall = (TextView) popView.findViewById(R.id.uninstall);
                TextView shared = (TextView) popView.findViewById(R.id.shared);
                TextView start = (TextView) popView.findViewById(R.id.start);

                AppInfo info = null;
                if (position > 0 && position < (userapps.size() + 1)) {
                    info = userapps.get(position - 1);
                } else if (position >= (userapps.size() + 2)) {
                    info = sysapps.get(position - 2 - userapps.size());
                }

                myOnClickListener = new MenuOnClickListener(info);
                start.setOnClickListener(myOnClickListener);
                shared.setOnClickListener(myOnClickListener);
                uninstall.setOnClickListener(myOnClickListener);
                setting.setOnClickListener(myOnClickListener);
                // 动画 汽泡动画 1.透明度 0.5 1.0 500 2.缩放动画 50% 100% 500 中心

                // AlphaAnimation anim1=new AlphaAnimation(开始 透明度, 结束 透明度);
                AlphaAnimation anim1 = new AlphaAnimation(0.5f, 1.0f);
                anim1.setDuration(200);
                // 缩放
                ScaleAnimation anim2 = new ScaleAnimation(//
                        0.5f, 1.0f, 0.5f, 1.0f, // 宽高
                        ScaleAnimation.RELATIVE_TO_SELF, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);// 中心
                anim2.setDuration(200);

                AnimationSet set = new AnimationSet(false);
                set.addAnimation(anim1);
                set.addAnimation(anim2);
                popView.startAnimation(set);
            }
        });
        listview.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                // scrollState1.TOUCH 2.FLing惯性滑动 3.IDLE
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                        if (windowPop != null) {
                            windowPop.dismiss();
                            windowPop = null;
                        }

                        int position = listview.getFirstVisiblePosition();// 获取第一行;
                        Log.i("zhang", "position--" + position);
                        String letter = "";
                        int type = adapter.getItemViewType(position);
                        if (type == 1)// 应用程序信息
                        {
                            AppInfo info = null;
                            if (position > 0 && position < (userapps.size() + 1)) {
                                info = userapps.get(position - 1);
                            } else if (position >= (2 + userapps.size())) {
                                info = sysapps.get(position - 2 - userapps.size());
                            }
                            // Log.i("zhang",
                            // "SCROLL_STATE_TOUCH_SCROLL---"+FormatUtils.toPinYin(info.name).toUpperCase().substring(0,1));
                            // sysapps;
                            letter = FormatUtils.toPinYin(info.name).toUpperCase().substring(0, 1);
                        } else {
                            if (position == 0) {
                                letter = FormatUtils.toPinYin(userapps.get(0).name).toUpperCase().substring(0, 1);
                            } else {
                                letter = FormatUtils.toPinYin(sysapps.get(0).name).toUpperCase().substring(0, 1);
                            }

                        }
                        findViewById(R.id.view_upcase).setVisibility(View.VISIBLE);
                        TextView letterView = (TextView) findViewById(R.id.letter);
                        letterView.setText(letter);

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.i("zhang", "SCROLL_STATE_IDLE 取消---");
                        findViewById(R.id.view_upcase).setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
    }

    ;

    private MenuOnClickListener myOnClickListener = null;

    private class MenuOnClickListener implements View.OnClickListener {
        private AppInfo info = null;

        public MenuOnClickListener(AppInfo info) {
            super();
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.uninstall) {
                AppUtils.uninstallApk(AppManagerActivity.this, info);
            } else if (v.getId() == R.id.start) {
                AppUtils.startApk(AppManagerActivity.this, info);
            } else if (v.getId() == R.id.shared) {
                AppUtils.shareApk(AppManagerActivity.this, info);
//                AppUtils.shareApk(getBaseContext(), info);
            } else if (v.getId() == R.id.setting) {
                AppUtils.showApkDetail(getBaseContext(), info);
            }

        }


    }

    ;
}
