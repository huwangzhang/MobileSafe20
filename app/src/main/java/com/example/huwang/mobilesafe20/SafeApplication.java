package com.example.huwang.mobilesafe20;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.huwang.mobilesafe20.db.dao.VirusDao;
import com.example.huwang.mobilesafe20.service.WatchDogService;
import com.example.huwang.mobilesafe20.utils.FileUtils;
import com.example.huwang.mobilesafe20.utils.HttpUtil;

public class SafeApplication extends Application {

	private String username = "";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("zhang", "SafeApplication  应用上下文创建 ......");
		// 测试
		// startService(new
		// Intent(this,MoSecurityService.class));//---background
		// startService(new
		// Intent(this,AutoKillProcessService.class));//---background
		 startService(new Intent(this,WatchDogService.class));//---background

		new Thread() {
			public void run() {
				try {
					InputStream input = getAssets().open("antivirus.db");
					FileUtils.copyFile(input, Environment.getExternalStorageDirectory().getAbsolutePath(), "antivirus.db");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

		// 联网同步病毒
		new Thread() {
			public void run() {

				try {

					String url = "http://192.168.17.83:8080/update70/virusinfo_1721";
					String json = HttpUtil.get(url);
					Log.i("zhang", json);

					// {"version":"1722","list":[{"md5":"sdfdsfsfdsdfsdfsd","desc":"av病毒","type":"6"},{"md5":"sdfdsfsfdsdfsd232d","desc":"bug病毒","type":"6"},{"md5":"sdfdsfsfd23232fsdfsd","desc":"a木马病毒","type":"6"}]}
					JSONObject jsonObj = new JSONObject(json);
					String version=jsonObj.getString("version");
					JSONArray array=jsonObj.getJSONArray("list");
					Log.i("zhang","version "+version);
					VirusDao.updateVersion(version);
					for(int i=0;i<array.length();i++)
					{
						//{"md5":"sdfdsfsfdsdfsdfsd","desc":"av病毒","type":"6"}
						JSONObject item=array.getJSONObject(i);
						String md5=item.getString("md5");
						String desc=item.getString("desc");
						String type=item.getString("type");
						Log.i("zhang", " md5 "+md5 +" desc"+desc+" type"+type);
						VirusDao.addMd5(md5, type, desc);
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			};
		}.start();

	}
}
