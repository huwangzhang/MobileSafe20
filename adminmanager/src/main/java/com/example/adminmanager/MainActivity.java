package com.example.adminmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.adminmanager.utils.AdminUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void lock(View view){
        AdminUtils.lockScreen(this);
    }
    public void wipe(View view){
        AdminUtils.wipeData(this);
    }

    public void active(View view) {
        AdminUtils.createAdmin(this);
    }

    public void inactive(View view) {
        AdminUtils.descAdmin(this);
    }
}
