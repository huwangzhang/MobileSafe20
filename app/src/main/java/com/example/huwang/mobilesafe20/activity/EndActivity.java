package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.prefs.Preferences;

import static com.example.huwang.mobilesafe20.R.id.safenumber;

/**
 * Created by huwang on 2017/3/22.
 */

public class EndActivity extends BaseActivity {
    @ViewInject(R.id.safe_number)
    TextView safe_number;
    @ViewInject(R.id.protect_open)
    TextView protect_open;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        ViewUtils.inject(this);
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("finish_set", true);
        editor.commit();
        String safenumberString = sp.getString("safenumber", "");
        Boolean protectopenBoolean = sp.getBoolean("open_check", false);
        safe_number.setText(safenumberString);
        protect_open.setSelected(protectopenBoolean);

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
