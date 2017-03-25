package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.ContactAdapter;
import com.example.huwang.mobilesafe20.bean.ContactInfo;
import com.example.huwang.mobilesafe20.bean.ContactUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huwang on 2017/3/25.
 */

public class SelectContactActivity extends Activity {
    @ViewInject(R.id.listview)
    ListView listView;
    List<ContactInfo> list = new ArrayList<ContactInfo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ViewUtils.inject(this);

        //假数据
//        for (int i = 0; i < 20; i++) {
//            ContactInfo info = new ContactInfo();
//            info.setName("用户" + i);
//            info.setPhone("123"+ i);
//            list.add(info);
//        }
        List<ContactInfo> temp = ContactUtils.getAllContacts(this);
        list.addAll(temp);
        //创建适配器
        ContactAdapter contactAdapter = new ContactAdapter(list, this);
        //设置数据
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //响应点击
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo info = list.get(position);
                Log.i("zhang", info.getPhone());
                Intent intent = new Intent();
                intent.putExtra("number", info.getPhone());
                setResult(200, intent);
                finish();
            }
        });
    }
}
