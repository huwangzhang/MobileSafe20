package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.AppStateAdapter;
import com.example.huwang.mobilesafe20.bean.AppInfo;
import com.example.huwang.mobilesafe20.utils.AppUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/9.
 */

public class NetStateActivity extends Activity {
    List<AppInfo> apps = new ArrayList<AppInfo>();

    @ViewInject(R.id.list_view)
    ListView list_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_state);
        ViewUtils.inject(this);

        apps= AppUtils.findAll(this);
//		for (int i = 0; i < 20; i++) {
//			AppInfo info = new AppInfo();
//			info.name = "流量 app" + i;
//			info.icon = getResources().getDrawable(R.drawable.app);
//			info.mobileDownLoad = 1024 * 10;
//			info.mobileUpload = 1024 * 10;
//			info.mobileTotal = 1024 * 20;
//			apps.add(info);
//		}

        // 创建适配器
        AppStateAdapter adapter=new AppStateAdapter(this,apps);
        list_view.setAdapter(adapter);

    }
}
