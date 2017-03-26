package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.ui.SettingCenterView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by huwang on 2017/3/25.
 */

public class SettingCenterActivity extends Activity {
    @ViewInject(R.id.item_update)
    SettingCenterView item_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);

        ViewUtils.inject(this); //注解生效

        item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zhang", "自动更行点击");
                boolean flag = item_update.isCheck();
                item_update.setCheck(!flag);

                //存储自动更新状态
                SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putBoolean("item_update", item_update.isCheck());
                editor.commit();

            }
        });
    }

    /**
     * 显示时候执行
     */
    @Override
    protected void onStart() {
        super.onStart();
        Boolean flag = getSharedPreferences("config", MODE_PRIVATE).getBoolean("item_update", false);
        item_update.setCheck(flag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
