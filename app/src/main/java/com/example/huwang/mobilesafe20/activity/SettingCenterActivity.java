package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.ui.SettingCenterView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static com.example.huwang.mobilesafe20.R.id.desc_style;

/**
 * Created by huwang on 2017/3/25.
 */

public class SettingCenterActivity extends Activity {
    @ViewInject(R.id.item_update)
    SettingCenterView item_update;
    @ViewInject(R.id.item_blacknumber)
    SettingCenterView item_blacknumber;
    @ViewInject(R.id.select_style)
    LinearLayout select_style;
    String[] items = new String[] {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
    @OnClick(R.id.select_style)
    public void showSelectDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("归属地提示框风格");
        int style_index = getSharedPreferences("config", Context.MODE_PRIVATE).getInt("style_index", 0);
        builder.setSingleChoiceItems(items, style_index, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
                editor.putInt("style_index", which);
                TextView desc_style = (TextView) findViewById(R.id.desc_style);
                desc_style.setText(items[which]);
                editor.commit();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
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
        item_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zhang", "黑名单设置开关");
                boolean flag = item_blacknumber.isCheck();
                item_blacknumber.setCheck(!flag);

                //存储自动更新状态
                SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putBoolean("item_blacknumber", item_blacknumber.isCheck());
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
        Boolean numberflag = getSharedPreferences("config", MODE_PRIVATE).getBoolean("item_blacknumber", false);
        item_blacknumber.setCheck(numberflag);
        int style_index = getSharedPreferences("config", Context.MODE_PRIVATE).getInt("style_index", 0);
        TextView desc_style = (TextView) findViewById(R.id.desc_style);
        desc_style.setText(items[style_index]);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
