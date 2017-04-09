package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.utils.FormatUtils;

import java.util.List;

/**
 * Created by huwang on 2017/4/9.
 */

public class AppCacheAdapter extends ArrayAdapter<AppInfo> {
    public AppCacheAdapter(Context context, List<AppInfo> applist) {
        super(context, 0, applist);
    }

    class ViewHolder {
        TextView app_name;
        TextView app_cache;
        ImageView app_icon;
    }

    // 返回列表的行视图 显示指定下标的数据

    // 如果 是复杂ListView优化的代码量是普通列表的N倍
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return setAppCacheInfo(position, convertView);
    }

    private View setAppCacheInfo(int position, View convertView) {
        // 获取行视图 经过优化
        // 1.优化inflate
        // 2.减少findViewById
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_item_app_cache_info, null);
            holder = new ViewHolder();
            holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
            holder.app_cache = (TextView) convertView.findViewById(R.id.app_cache);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo info = getItem(position);
        holder.app_name.setText(info.name);
        holder.app_cache.setText(FormatUtils.formatSize(getContext(), info.cacheSize));
        holder.app_icon.setImageDrawable(info.icon);
        // byte-->K M G
        // 返回视图
        return convertView;
    }
}
