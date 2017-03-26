package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;

/**
 * Created by huwang on 2017/3/20.
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;

    public HomeAdapter(Context context) {
        super();
        this.context = context;
    }

    private String[] names = new String[]{
            "手机防盗",
            "通讯卫士",
            "软件管家",
            "进程管理",
            "流量统计",
            "手机杀毒",
            "缓存清理",
            "高级工具",
            "设置中心",
    };

    private int[] images = new int[]{
            R.drawable.safe,
            R.drawable.callmsgsafe, R.drawable.ic_app_selector,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
    };

    //getCount 返回格子数
    //getView 返回网格视图 显示制定下标的数据
    //getItem 返回制定下标数据
    //getItemId 跟点击事件有关决定视图Id
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //getView 返回网格视图 显示制定下标的数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = names[position];
        int resid = images[position];
        //视图
        //把布局转换成对象View：布局膨胀
        View view = View.inflate(context, R.layout.view_item_gridview, null);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView nameView = (TextView) view.findViewById(R.id.name);
        iconView.setImageResource(resid);
        nameView.setText(name);
        return view;
    }
}
