package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.adapter.HomeAdapter;

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
                        showLoginDialog();
                        break;
                }
            }
        });
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        View dialogView = View.inflate(getBaseContext(), R.layout.view_dialog_register, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
    }
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        View dialogView = View.inflate(getBaseContext(), R.layout.view_dialog_login, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
    }
}
