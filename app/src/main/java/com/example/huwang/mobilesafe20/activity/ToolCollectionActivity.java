package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/4/3.
 */

public class ToolCollectionActivity extends Activity {
    @ViewInject(R.id.address_query)
    TextView address_query;
    @ViewInject(R.id.sms_tools)
    TextView sms_tools;
//    @ViewInject(R.id.sms_backup)
//    TextView sms_backup;
//    @ViewInject(R.id.sms_restore)
//    TextView sms_restore;
    @ViewInject(R.id.apps_lock)
    TextView apps_lock;


    @OnClick(R.id.sms_tools)
    public void startSmstools(View view) {
        startActivity(new Intent(this, SmsBackupRestoreActivity.class));
    }
    @OnClick(R.id.address_query)
    public void getQueryPage(View view) {
        startActivity(new Intent(this, AddressQueryActivity.class));
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_collection);

        ViewUtils.inject(this);
    }
}
