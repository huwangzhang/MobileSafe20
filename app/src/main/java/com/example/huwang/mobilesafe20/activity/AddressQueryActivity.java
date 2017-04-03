package com.example.huwang.mobilesafe20.activity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.db.dao.AddressDao;
import com.example.huwang.mobilesafe20.utils.FileUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huwang on 2017/4/3.
 */

public class AddressQueryActivity extends Activity {
    @ViewInject(R.id.address_show)
    TextView address_show;
    @ViewInject(R.id.input_number)
    TextView input_number;
    @ViewInject(R.id.report_error)
    TextView report_error;
    @OnClick(R.id.report_error)
    public void report(View view) {
        //        xml->Animation对象
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.shake);
        report_error.startAnimation(anim);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_query);

        ViewUtils.inject(this);

        final AddressDao dao =  new AddressDao(this);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address_show.setText(dao.findAddressByNumber(s.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        input_number.addTextChangedListener(tw);


    }
}
