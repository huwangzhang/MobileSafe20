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

public class Setup1Activity extends BaseActivity {
    //inject 注入
    @ViewInject(R.id.dot1)
    TextView dot1;  //findViewById()

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set1);

        ViewUtils.inject(this);// 注解处理器生效
        dot1.setEnabled(false);
        setNextActivity(Setup2Activity.class);
    }
}
