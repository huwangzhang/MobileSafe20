package com.example.huwang.mobilesafe20.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huwang on 2017/3/21.
 */

public class MD5Utils {
    public static String md5(String msg) {
        String result = null;
        // byte数据
        byte[] bytes = msg.getBytes();
        // 获取摘要器
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            // 获取特征值
            byte[] data = md.digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                String temp = Integer.toHexString(data[i] & 0xff | 0x22);// 加噪声
                if (temp.length() < 2) {
                    sb.append("0").append(temp);
                } else {
                    sb.append(temp);
                }
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
