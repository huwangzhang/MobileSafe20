package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.activity.SmsCallActivity;
import com.example.huwang.mobilesafe20.bean.BlackNumberInfo;

import java.util.List;

/**
 * Created by huwang on 2017/3/30.
 */

public class BlackNumberAdapter extends ArrayAdapter<BlackNumberInfo> {
    public BlackNumberAdapter(Context context, List<BlackNumberInfo> objects) {
        super(context, 0, objects);
    }

    class ViewHolder
    {
        TextView name;
        TextView desc;
        TextView delete;
    }
    // 返回行视图，显示指定下标的数据
    int count=0;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1.setAdapter
        //2.滑动
        //3.notifyDataSetChanged
//		Log.i("zhang", "--getView----");
        // 数据
        final BlackNumberInfo info = getItem(position);// list.get(position);
        ViewHolder holder=null;
        // 视图   打气:layout--View
        // 消耗内存x10000
        //convertView视图回收器返回的视图
        if(convertView==null)//视图回收器没有可返回视图【重用视图】
        {
            ++count;
//			Log.i("zhang", "--inflate---消耗内存-"+count);
            convertView = View.inflate(getContext(), R.layout.view_item_black_number, null);
            holder=new ViewHolder();
            holder. name = (TextView) convertView.findViewById(R.id.name);
            holder. desc = (TextView) convertView.findViewById(R.id.desc);
            holder. delete = (TextView) convertView.findViewById(R.id.delete);
//			Log.i("zhang", "--findviewbyid---消耗性能");
            convertView.setTag(holder);//  //Tag 不是理解标签  View的一个类型为Object名字 tag
        }else//重用。。。滑动
        {
            holder=(ViewHolder) convertView.getTag();
        }


        holder.name.setText(info.number);

        if ("0".equals(info.mode)) {
            holder.desc.setText("拦截电话+短信");
        } else if ("1".equals(info.mode)) {
            holder.desc.setText("拦截电话");
        } else if ("2".equals(info.mode)) {
            holder.desc.setText("拦截短信");
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //删除 与刷新
                SmsCallActivity activity=(SmsCallActivity) getContext();
                activity.delteAndRefreshUI(info);
            }
        });



        return convertView;
    }

}
