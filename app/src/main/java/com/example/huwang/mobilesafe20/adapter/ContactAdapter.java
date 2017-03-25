package com.example.huwang.mobilesafe20.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by huwang on 2017/3/25.
 */

public class ContactAdapter extends BaseAdapter {
    private List<ContactInfo> list = new ArrayList<ContactInfo>();
    private Context context;

    public ContactAdapter(List<ContactInfo> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //视图
        View view = View.inflate(context, R.layout.view_item_contact, null);
        //数据
        ContactInfo info = list.get(position);
        //设置值
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView phone = (TextView)view.findViewById(R.id.phone);
        if (info.getName() == null ){
            name.setText(info.getPhone());
        } else {
            name.setText(info.getName());
        }
        phone.setText(info.getPhone());
        return view;
    }
}
