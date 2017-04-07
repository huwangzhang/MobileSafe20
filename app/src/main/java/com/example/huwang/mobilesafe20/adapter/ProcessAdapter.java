package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.ProcessInfo;
import com.example.huwang.mobilesafe20.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/6.
 */

public class ProcessAdapter extends ArrayAdapter<ProcessInfo> {

    private List<ProcessInfo> userProcess = new ArrayList<ProcessInfo>();
    private List<ProcessInfo> sysProcess = new ArrayList<ProcessInfo>();

    public ProcessAdapter(Context context, List<ProcessInfo> userProcess, List<ProcessInfo> sysProcess) {
        super(context, 0);// --getView内部直接给定
        this.userProcess = userProcess;
        this.sysProcess = sysProcess;

    }

    // 该有两种行视图

    // 返回行数
    @Override
    public int getCount() {
        SharedPreferences sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean show_sys_processValue = sp.getBoolean("show_sys_process", true);
        if (show_sys_processValue)// 显示系统进程
        {
            return userProcess.size() + sysProcess.size() + 2;
        } else {
            return userProcess.size() + 1;
        }

    }

    // 返回视图种类数
    @Override
    public int getViewTypeCount() {
        return 2; // 0 标题 1 进程信息
    }

    // 返回指定下标使用到视图类型
    @Override
    public int getItemViewType(int position) { // -->getView
        // 0 标题 1 进程信息
        if (position == 0) // 用户进程
        {
            return 0;
        } else if (position == (1 + userProcess.size())) // 系统进程
        {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == 0) {
            convertView = setTitleValue(position, convertView);
            return convertView;
        } else {
            convertView = setProcessInfo(position, convertView);
            return convertView;
        }
    }

    private View setProcessInfo(int position, View convertView) {
        ViewHolderProcess holder = null;
        if (convertView == null) {
            holder = new ViewHolderProcess();
            convertView = View.inflate(getContext(), R.layout.view_item_process_info, null);

            holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
            holder.process_mem = (TextView) convertView.findViewById(R.id.process_mem);
            holder.process_select = (CheckBox) convertView.findViewById(R.id.process_select);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolderProcess) convertView.getTag();
        }

        // 赋值
        ProcessInfo info = null;
        if (0 < position && position < (1 + userProcess.size())) {
            info = userProcess.get(position - 1);
        } else if (position >= (2 + userProcess.size())) {
            info = sysProcess.get(position - 2 - userProcess.size());
        }

        holder.app_name.setText(info.name);
        holder.process_mem.setText(FormatUtils.formatSize(getContext(), info.memorySize));

        // 是否选中
        holder.process_select.setChecked(info.check);
        // 图标
        holder.app_icon.setImageDrawable(info.icon);
        return convertView;
    }

    private View setTitleValue(int position, View convertView) {
        ViewHolderProcess holder = null;
        if (convertView == null) {
            holder = new ViewHolderProcess();
            convertView = View.inflate(getContext(), R.layout.view_item_process_title, null);
            holder.processtitle = (TextView) convertView.findViewById(R.id.processtitle);
            convertView.setTag(holder);
        } else {
            // 重用视图回收返回的视图
            holder = (ViewHolderProcess) convertView.getTag();
        }
        // 赋值
        if (position == 0) {
            holder.processtitle.setText("用户进程:" + userProcess.size());
        } else {
            holder.processtitle.setText("系统进程:" + sysProcess.size());
        }
        return convertView;
    }

    // 返回 行视图 显示指定下标的数据
    // 如果 类型总数据为N 优化的代码量为N倍
    public static class ViewHolderProcess {
        public TextView processtitle;
        public ImageView app_icon;
        public TextView app_name;
        public TextView process_mem;
        public CheckBox process_select;
    }
}
