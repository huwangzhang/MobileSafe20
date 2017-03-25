package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by huwang on 2017/3/22.
 */

public class Setup2Activity extends BaseActivity {
    @ViewInject(R.id.dot2)
    TextView dot2;
    @ViewInject(R.id.bind)
    TextView bind;

    @OnClick(R.id.bind)
    public void bindOrUnbind(View view) {
        if ("未绑定sim卡，请绑定".equals(bind.getText().toString().trim())) {
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //获取序列卡号
            String sim = tm.getSimSerialNumber();
            Log.i("zhang", "Setup2Activity-----"+sim);
            SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
            editor.putString("sim_number", sim);
            editor.commit();
            bind.setText("已绑定，可以解除sim卡绑定");
            bind.setSelected(true);
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
            editor.putString("sim_number", null);
            editor.commit();
            bind.setText("未绑定sim卡，请绑定");
            bind.setSelected(false);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set2);
        ViewUtils.inject(this);
        dot2.setEnabled(false);
        setPreActivity(Setup1Activity.class);
        setNextActivity(Setup3Activity.class);

        String simNumber = getSharedPreferences("config", Context.MODE_PRIVATE).getString("sim_number", null);
        if (simNumber == null) {
            bind.setText("未绑定sim卡，请绑定");
            bind.setSelected(false);

        } else {
            bind.setText("已绑定，可以解除sim卡绑定");
            bind.setSelected(true);
        }
    }
}
