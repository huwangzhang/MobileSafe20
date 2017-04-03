package com.example.huwang.mobilesafe20.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by huwang on 2017/3/19.
 */

public class HttpUtil {
    public static String get(String urlString) {
        String result = null;
        //创建链接
        try {
            URL url = new URL(urlString);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置get连接时长5000
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            if (200 == conn.getResponseCode()) {
                InputStream input = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                //准备一个缓冲区
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = input.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                byte[] bytes = out.toByteArray();
                out.close();
                result = new String(bytes);
                //关闭流
                input.close();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
