package com.example.huwang.mobilesafe20.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by huwang on 2017/3/22.
 */

public class Setup4Activity extends BaseActivity {

    @ViewInject(R.id.dot4)
    TextView dot4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set4);
        ViewUtils.inject(this);

        dot4.setEnabled(false);
        setPreActivity(Setup3Activity.class);
        setNextActivity(EndActivity.class);
    }
}
