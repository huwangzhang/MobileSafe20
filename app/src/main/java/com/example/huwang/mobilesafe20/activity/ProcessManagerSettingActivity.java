package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by huwang on 2017/4/6.
 */

public class ProcessManagerSettingActivity extends Activity {

    @ViewInject(R.id.show_sys_process)
    CheckBox show_sys_process;
    @ViewInject(R.id.lock_screen_clean)
    CheckBox lock_screen_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager_setting);
        ViewUtils.inject(this);

        show_sys_process.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
                editor.putBoolean("show_sys_process", isChecked);
                editor.commit();//生效
            }
        });
        lock_screen_clean.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
                editor.putBoolean("lock_screen_clean", isChecked);
                editor.commit();//生效
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean lock_screen_cleanValue = sp.getBoolean("lock_screen_clean", false);
        boolean show_sys_processValue = sp.getBoolean("show_sys_process", true);

        show_sys_process.setChecked(show_sys_processValue);
        lock_screen_clean.setChecked(lock_screen_cleanValue);
    }
}
