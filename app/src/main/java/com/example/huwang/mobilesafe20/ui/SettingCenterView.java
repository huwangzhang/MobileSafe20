package com.example.huwang.mobilesafe20.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huwang.mobilesafe20.R;


/**
 * Created by huwang on 2017/3/25.
 */

public class SettingCenterView extends LinearLayout {
    private String titleString;
    private TextView desc;
    private CheckBox check;
    private String descOnString;
    private String descOffString;
    /**
     * @param context
     * @param attrs   当前标签配置的属性与属性值
     *                android 与zhang 属性
     */
    public SettingCenterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_item_setting, this);
        String namespace = "http://schemas.android.com/apk/res-auto";
        titleString = attrs.getAttributeValue(namespace, "m_title");
        descOffString = attrs.getAttributeValue(namespace, "desc_off");
        descOnString = attrs.getAttributeValue(namespace, "desc_on");
        Boolean checkBoolean = attrs.getAttributeBooleanValue(namespace, "check", false);
        Log.i("zhang", titleString + "");
        TextView title = (TextView) view.findViewById(R.id.title);
        desc = (TextView) view.findViewById(R.id.desc);
        check = (CheckBox) view.findViewById(R.id.check);
        title.setText(titleString == null ? "" : titleString);
        check.setChecked(checkBoolean);
        if (checkBoolean) {
            desc.setText(titleString == null ? "" : descOnString);
            desc.setTextColor(Color.parseColor("#00ff00"));
        } else {
            desc.setText(titleString == null ? "" : descOffString);
            desc.setTextColor(Color.parseColor("#ff0000"));
        }


    }

    /**
     * @return 是否选中
     */
    public boolean isCheck() {
        return check.isChecked();
    }

    public void setCheck(boolean ischeck) {
        if (ischeck) {
            desc.setText(titleString == null ? "" : descOnString);
            desc.setTextColor(Color.parseColor("#00ff00"));
        } else {
            desc.setText(titleString == null ? "" : descOffString);
            desc.setTextColor(Color.parseColor("#ff0000"));
        }
        check.setChecked(ischeck);
    }
}
