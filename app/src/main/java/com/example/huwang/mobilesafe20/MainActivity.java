package com.example.huwang.mobilesafe20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.huwang.mobilesafe20.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ToastUtil.showInWindow(getBaseContext(), R.layout.view_show_address);
    }
}
