package com.example.huwang.mobilesafe20.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by huwang on 2017/3/20.
 */

public class FocusTextView extends AppCompatTextView {
    //配置标签的构造方法
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
