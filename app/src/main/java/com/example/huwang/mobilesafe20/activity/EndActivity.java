package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.huwang.mobilesafe20.R;

/**
 * Created by huwang on 2017/3/22.
 */

public class EndActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        setPreActivity(Setup4Activity.class);
    }
    public void choosePre(View view) {
        startActivity(new Intent(getBaseContext(), Setup1Activity.class));
        finish();
    }

    public void savePage(View view) {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        finish();
    }
}
