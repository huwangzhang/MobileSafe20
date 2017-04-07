package com.example.huwang.mobilesafe20.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by huwang on 2017/4/6.
 */

public class ProcessInfo {
    public String packageName;//程序编号
    public String pid;//进程编号
    public String name;
    public long memorySize;
    public boolean check;
    public boolean isSystem;
    public Drawable icon;
}
