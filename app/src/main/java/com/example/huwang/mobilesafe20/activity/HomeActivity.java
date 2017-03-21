package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.HomeAdapter;
import com.example.huwang.mobilesafe20.utils.ToastUtil;

/**
 * Created by huwang on 2017/3/16.
 */


public class HomeActivity extends Activity {
    private GridView gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化数据
        gridView = (GridView)findViewById(R.id.gridview);
        //创建内容
        HomeAdapter adapter = new HomeAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机防盗
                        //弹出对话框
                        String pwd = getSharedPreferences("config", Context.MODE_PRIVATE).getString("pwd", null);
                        if (pwd == null) {
                            showRegisterDialog();
                        } else {
                            showLoginDialog();
                        }
                        break;
                }
            }
        });
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        final View dialogView = View.inflate(getBaseContext(), R.layout.view_dialog_register, null);

        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        dialog.show();
        final EditText pwd1View = (EditText)dialogView.findViewById(R.id.pwd1);
        final EditText pwd2View = (EditText)dialogView.findViewById(R.id.pwd2);
        Button ok = (Button)dialogView.findViewById(R.id.ok);
        Button cancel = (Button)dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = pwd1View.getText().toString().trim();
                String pwd2 = pwd2View.getText().toString().trim();
                if ("".equals(pwd1) || "".equals(pwd2)) {
                    ToastUtil.shortToast(HomeActivity.this, "密码不能为空");
                    return;
                }
                if (!pwd1.equals(pwd2)) {
                    ToastUtil.shortToast(HomeActivity.this, "密码不一样");
                    return;
                }
                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
                editor.putString("pwd", "123");
                editor.commit();
                System.out.println("注册成功");
                dialog.dismiss();
            }
        });
    }
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        View dialogView = View.inflate(getBaseContext(), R.layout.view_dialog_login, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        dialog.show();
        final EditText pwd1View = (EditText)dialogView.findViewById(R.id.pwd1);
        final CheckBox show_password = (CheckBox)dialogView.findViewById(R.id.show_password);
        Button ok = (Button)dialogView.findViewById(R.id.ok);
        Button cancel = (Button)dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = pwd1View.getText().toString().trim();
                if ("".equals(pwd1)) {
                    ToastUtil.shortToast(HomeActivity.this, "密码不能为空");
                    return;
                }
                String pwd = getSharedPreferences("config", Context.MODE_PRIVATE).getString("pwd", null);
                if (!pwd1.equals(pwd1)) {
                    ToastUtil.shortToast(HomeActivity.this, "密码错误");
                    return;
                }
                System.out.println("登陆成功");
                dialog.dismiss();
            }
        });
    }
}
