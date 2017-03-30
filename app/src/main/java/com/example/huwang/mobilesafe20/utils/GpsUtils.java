package com.example.huwang.mobilesafe20.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.huwang.mobilesafe20.service.GpsService;
import com.example.huwang.mobilesafe20.utils.ModifyOffset.PointDouble;

import java.io.InputStream;

/**
 * Created by huwang on 2017/3/28.
 */

public class GpsUtils {
    /**
     * 开始定位
     *
     * @param context
     */
    public static void startLocation(final Context context) {

        // ①　配置权限
        // ②　创建核心类
        // 获取核心类实例
        final LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // ③　初始化参数
        Criteria c = new Criteria();
        // 设置初始化参数
        c.setCostAllowed(true);// 如果有收费是否同意
        c.setPowerRequirement(Criteria.POWER_HIGH);// gps十分耗电
        c.setAccuracy(Criteria.ACCURACY_FINE);// 精度要求gps
        String provider = lm.getBestProvider(c, false);
        Log.i("zhang", provider);
        // ④　添加监听器获取定位值
        MyLocationListener listener = new MyLocationListener(context, "gps");
        lm.requestLocationUpdates("gps", 0, 0, listener);
        MyLocationListener listener1 = new MyLocationListener(context, "network");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates("network", 0, 0, listener1);
        // }
        // network 基站
        // lm.requestLocationUpdates("gps", 0, 0, listener);
    }

    public static class MyLocationListener implements LocationListener {
        private Context context;
        private String provider;

        public MyLocationListener(Context context, String provider) {
            super();
            this.context = context;
            this.provider = provider;
        }

        // 使用该方法接收位置参数：Location
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            PointDouble p = new PointDouble(lon, lat);
            p = changeWgs84ToGcj02(context, p);
            Log.i("zhang", provider + "  onLocationChanged---lat:" + p.y + " lon" + p.x);
            // 移除监听
            SmsUtil.sendTextSms("5554", provider + "  onLocationChanged---lat:" + p.y + " lon" + p.x);
            Toast.makeText(context, provider + "  onLocationChanged---lat:" + p.y + " lon" + p.x, Toast.LENGTH_SHORT).show();


            //关闭服务
            Intent intent=new Intent(context,GpsService.class);
            context.stopService(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    /**
     * 坐标纠偏
     *
     * @param context
     * @param p
     * @return
     */
    public static PointDouble changeWgs84ToGcj02(Context context, PointDouble p) {
        // 获取资产管理者
        AssetManager am = context.getAssets();
        try {
            Log.i("zhang", "读取纠偏文件成功！");
            InputStream input = am.open("axisoffset.dat");
            ModifyOffset mo = ModifyOffset.getInstance(input);
            p = mo.s2c(p);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return p;
    }
}
