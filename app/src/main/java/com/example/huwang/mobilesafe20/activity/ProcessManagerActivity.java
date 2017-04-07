package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.SafeApplication;
import com.example.huwang.mobilesafe20.adapter.ProcessAdapter;
import com.example.huwang.mobilesafe20.bean.ProcessInfo;
import com.example.huwang.mobilesafe20.service.AutoKillProcessService;
import com.example.huwang.mobilesafe20.service.MoSecurityService;
import com.example.huwang.mobilesafe20.utils.FormatUtils;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;
import com.example.huwang.mobilesafe20.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/6.
 */

public class ProcessManagerActivity extends Activity {
    private List<ProcessInfo> userProcess = new ArrayList<ProcessInfo>();
    private List<ProcessInfo> sysProcess = new ArrayList<ProcessInfo>();

    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.waiting)
    LinearLayout waiting;
    @ViewInject(R.id.process_count)
    TextView process_count;
    @ViewInject(R.id.memory_info)
    TextView memory_info;
    private ProcessAdapter adapter = null;
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if(adapter!=null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.select_all)
    public void selectAll(View view) {
        // check=true;
        for (ProcessInfo p : userProcess) {
            p.check = true;
        }
        for (ProcessInfo p : sysProcess) {
            p.check = true;
        }
        // 刷新
        if (adapter != null) {
            adapter.notifyDataSetChanged();// 重新显示每一行。
        }
    }

    @OnClick(R.id.select_reverse)
    public void selectReverse(View view) {
        // check=!check;
        for (ProcessInfo p : userProcess) {
            p.check = !p.check;
        }
        for (ProcessInfo p : sysProcess) {
            p.check = !p.check;
        }
        // 刷新
        if (adapter != null) {
            adapter.notifyDataSetChanged();// 重新显示每一行。
        }
    }
    @OnClick(R.id.process_setting)
    public void process_setting(View view) {

        startActivity(new Intent(this,ProcessManagerSettingActivity.class));

    }

    /**
     * 清理
     *
     * @param view
     */

    @OnClick(R.id.clean)
    public void clean(View view) {
        List<ProcessInfo> selectList = new ArrayList<ProcessInfo>();
        // check=!check;
        for (ProcessInfo p : userProcess) {
            if (p.check) {
                selectList.add(p);
            }
        }
        for (ProcessInfo p : sysProcess) {// 【加强for循环内部不能调用remove】
            if (p.check) {
                selectList.add(p);
            }
        }
        long totalProcessKilled=0;
        long freeMem=0;
        for (ProcessInfo selectItem : selectList) {
            if (selectItem.isSystem) {
                sysProcess.remove(selectItem);
            } else {
                userProcess.remove(selectItem);
            }
            ++totalProcessKilled;
            freeMem+=selectItem.memorySize;
            ProcessUtils.killprocess(this, selectItem.packageName);//杀死进程
        }
        // 刷新
        if (adapter != null) {
            adapter.notifyDataSetChanged();// 重新显示每一行。
        }
        //显示内存
        availableMem+=freeMem;
        long totalMemo = ProcessUtils.getTotalMemFromProc(this);// byte--->K B M
        memory_info.setText("可用:" + FormatUtils.formatSize(this, availableMem) + " 总共:" + FormatUtils.formatSize(this, totalMemo));
        //进程个数
        totalProcess-=totalProcessKilled;
        process_count.setText("进程个数:"+totalProcess);
        ToastUtil.shortToast(getBaseContext(), "杀死" + totalProcessKilled+ "个进程，释放了"+FormatUtils.formatSize(this, freeMem)+"内存");
    }

    private int totalProcess=0;
    private long availableMem=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        ViewUtils.inject(this);

        startService(new Intent(this, MoSecurityService.class));
        startService(new Intent(this, AutoKillProcessService.class));


//        SafeApplication app=(SafeApplication) getApplication();
//        Log.i("zhang", app.getUsername());
		int processcount = ProcessUtils.getProcessCount(this);
		process_count.setText("进程个数:" + (processcount - 1));// 本应用不删除自己进程
        availableMem = ProcessUtils.getAvailableMem(this);
        long totalMemo = ProcessUtils.getTotalMemFromProc(this);// byte--->K B M
        memory_info.setText("可用:" + FormatUtils.formatSize(this, availableMem) + " 总共:" + FormatUtils.formatSize(this, totalMemo));
        // AsyncTask :对thread+handler封装 耗时代码 避免应用卡顿(不流畅)
        // 过程清析 有自动执行的方法
        // 任务前: 显示等待视图
        // 任务中: 耗时代码:获数据
        // 任务后:隐藏等待视图 显示数据
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                waiting.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // // 用户进程
                // for (int i = 0; i < 5; i++) {
                // ProcessInfo info = new ProcessInfo();
                // info.isSystem = false;
                // info.pid = "200" + i;
                // info.packageName = "com.itheima.kuai" + i;
                // info.memorySize = 1024 * 1024 * 10 + i;
                // info.name = "快播" + i;
                // info.icon = getResources().getDrawable(R.drawable.app);
                // userProcess.add(info);
                // }
                // // 系统进程
                // for (int i = 0; i < 1500; i++) {
                // ProcessInfo info = new ProcessInfo();
                // info.isSystem = true;
                // info.pid = "200" + i;
                // info.packageName = "com.itheima.momo2" + i;
                // info.memorySize = 1024 * 1024 * 10 + i;
                // info.name = "陌陌" + i;
                // info.icon = getResources().getDrawable(R.drawable.app);
                // sysProcess.add(info);
                // }

                List<ProcessInfo> infos = ProcessUtils.findAll(ProcessManagerActivity.this);
                for (ProcessInfo info : infos) {
                    if (info.isSystem) {
                        sysProcess.add(info);
                    } else {
                        if(info.packageName.equals(getPackageName()))
                        {
                            //不进行操作。
                        }else
                        {
                            userProcess.add(info);
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {

                adapter = new ProcessAdapter(ProcessManagerActivity.this, userProcess, sysProcess);
                listview.setAdapter(adapter);
                addListeners();
                // 隐藏等待视图
                waiting.setVisibility(View.GONE);// invisible 点击困难

                totalProcess=userProcess.size()+sysProcess.size();
                process_count.setText("进程个数:" + (userProcess.size()+sysProcess.size()));// 本应用不删除自己进程
            }

        }.execute();

    }

    private void addListeners() {
        // TODO Auto-generated method stub
        listview.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 用户数据
                // 系统应用
                ProcessInfo info = null;
                if (position > 0 && position < (1 + userProcess.size())) {
                    info = userProcess.get(position - 1);
                } else if (position >= (2 + userProcess.size())) {
                    info = sysProcess.get(position - 2 - userProcess.size());
                }

                if (info != null) {
                    // 取反
                    info.check = !info.check;
                    // 同步刷新
                    // if (adapter != null) {
                    // adapter.notifyDataSetChanged();// 所有可见行视图都调用 getView
                    // 刷新==>ListView刷新 一遍
                    // }
                    // view：点击的行视图
                    ProcessAdapter.ViewHolderProcess holder = (ProcessAdapter.ViewHolderProcess) view.getTag();
                    holder.process_select.setChecked(info.check);
                }

            }
        });
    }
}
