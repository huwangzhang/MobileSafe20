package com.example.huwang.mobilesafe20.bean;

import android.graphics.drawable.Drawable;


/**
 * Created by huwang on 2017/4/4.
 */
public class AppInfo {
	public String packageName;//id
	public String name;
	public String location;
	public Drawable icon;
	public long size;
	public String path;
	public String desc;////av病毒
	
	public int uid;
	
	public boolean  isSys=false;
	public boolean  isInPhone=false;
	public boolean isLock=false;
	
	public long  cacheSize=0;
	public long  mobileUpload=0;
	public long  mobileDownLoad=0;
	public long  mobileTotal=0;
}
