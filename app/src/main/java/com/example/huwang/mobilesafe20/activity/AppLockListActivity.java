package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.fragment.TypeLockAppFragment;
import com.example.huwang.mobilesafe20.fragment.TypeUnLockAppFragment;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import static com.example.huwang.mobilesafe20.utils.ProcessUtils.isNoSwitch;

/**
 * Created by huwang on 2017/4/8.
 */

public class AppLockListActivity extends FragmentActivity {
    @ViewInject(R.id.type_unlock)
    TextView type_unlock;
    @ViewInject(R.id.type_lock)
    TextView type_lock;
    @ViewInject(R.id.app_list_layout)
    FrameLayout app_list_layout;
    @ViewInject(R.id.app_status)
    TextView app_status;


    private TypeLockAppFragment lockAppFragment = new TypeLockAppFragment();
    private TypeUnLockAppFragment unLockAppFragment = new TypeUnLockAppFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_locklist);
        ViewUtils.inject(this);
        type_unlock.setEnabled(false);
        //获取管理者
        FragmentManager fm = getSupportFragmentManager();
        //打开事务
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.app_list_layout, unLockAppFragment);
        //提交
        ft.commit();

//        Boolean action_usage_flag = getSharedPreferences("config", MODE_PRIVATE).getBoolean("action_usage_flag", false);
//        Log.i("zhang", "---------action_usage_flag-----" + action_usage_flag);
//        Log.i("zhang", "---------ProcessUtils.isNoSwitch(getBaseContext())-----" + ProcessUtils.isNoSwitch(getBaseContext()));
        if (!ProcessUtils.isNoSwitch(getBaseContext())) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

            startActivity(intent);
        }
        app_list_layout.setBackgroundColor(Color.parseColor("#555223"));
        app_status.setText("未加锁程序:");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取管理者
                FragmentManager fm = getSupportFragmentManager();
                //打开事务
                FragmentTransaction ft = fm.beginTransaction();
                if (v.getId() == R.id.type_unlock) {
                    type_unlock.setEnabled(false);
                    type_lock.setEnabled(true);
                    app_list_layout.setBackgroundColor(Color.parseColor("#555223"));
//                    app_status.setText("未加锁程序:10个");

                    ft.replace(R.id.app_list_layout, unLockAppFragment);
                } else if (v.getId() == R.id.type_lock) {
                    type_unlock.setEnabled(true);
                    type_lock.setEnabled(false);
                    app_list_layout.setBackgroundColor(Color.parseColor("#ffffff"));
//                    app_status.setText("已加锁程序:20个");
                    ft.replace(R.id.app_list_layout, lockAppFragment);
                }
                //提交
                ft.commit();
            }
        };
        type_unlock.setOnClickListener(listener);
        type_lock.setOnClickListener(listener);
    }
}
