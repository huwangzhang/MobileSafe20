package com.example.huwang.mobilesafe20.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.utils.AdminUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/3/22.
 */

public class Setup4Activity extends BaseActivity {

    @ViewInject(R.id.dot4)
    TextView dot4;
    @ViewInject(R.id.checkbox)
    CheckBox checkbox;
    @ViewInject(R.id.start)
    TextView start;

    @OnClick(R.id.start)
    public void start(View view) {
        if (AdminUtils.isActivie(this)) {
            AdminUtils.destoryAdmin(this);
            start.setText("超级管理员账户未激活");
        } else {
            AdminUtils.createAdmin(this);
            start.setText("超级管理员已账户激活");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AdminUtils.isActivie(this)) {
            start.setText("超级管理员已账户激活");
        } else {
            start.setText("超级管理员账户未激活");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set4);
        ViewUtils.inject(this);


        if (AdminUtils.isActivie(this)) {
            start.setText("超级管理员账户已激活");
        } else {
            start.setText("超级管理员账户未激活");
        }

        dot4.setEnabled(false);
        boolean checkBoolean = getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("open_check", false);
        checkbox.setChecked(checkBoolean);
        setPreActivity(Setup3Activity.class);
        setNextActivity(EndActivity.class);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //响应选中
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
                editor.putBoolean("open_check", isChecked);
                editor.commit();
            }
        });
    }
}
