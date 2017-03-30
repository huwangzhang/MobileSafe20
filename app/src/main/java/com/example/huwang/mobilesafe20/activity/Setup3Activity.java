package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/3/22.
 */

public class Setup3Activity extends BaseActivity {
    @ViewInject(R.id.dot3)
    TextView dot3;
    @ViewInject(R.id.safenumber)
    EditText safenumber;

    @OnClick(R.id.select_number)
    public void select(View view) {
        Log.i("zhang", "Setup3Activity---------------");
        startActivityForResult(new Intent(this, SelectContactActivity.class), 100);
//        startActivity(new Intent(this, SelectContactActivity.class));
    }

    //处理响应
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 200) {
            String number = data.getStringExtra("number");
            Log.i("zhang", "Setup3Activity"+number);
            safenumber.setText(number);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set3);
        ViewUtils.inject(this);
        dot3.setEnabled(false);
        setPreActivity(Setup2Activity.class);
        setNextActivity(Setup4Activity.class);


        //显示保存安全号码
        String numberString = getSharedPreferences("config", Context.MODE_PRIVATE).getString("safenumber", "");
        safenumber.setText(numberString+"");
        safenumber.addTextChangedListener(new TextWatcher() {
            //
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //正在输入
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("zhang", "onTextChanged ----"+ s.toString().trim());
                SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putString("safenumber", s.toString().trim());
                editor.commit(); //生成标签
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
