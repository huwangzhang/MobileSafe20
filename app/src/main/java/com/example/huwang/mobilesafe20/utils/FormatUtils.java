package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.text.format.Formatter;

import java.util.regex.Pattern;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * Created by huwang on 2017/4/3.
 */

public class FormatUtils {
    /**
     * 是否是手机号
     * @param number
     * @return
     */
    public static boolean isPhone(String number) {
        String format = "^[1]([3][0-9]{1}|59|58|88|89|87)[0-9]{8}$";
        // 格式
        Pattern p = Pattern.compile(format);// 生成正则对象
        boolean flag = p.matcher(number).find();
        return flag;
    }

    /**
     * byte-->K M  G
     * @param context
     * @param size
     * @return
     */
    public static String formatSize(Context context, long size)
    {
        return Formatter.formatFileSize(context, size);//K M G  1024byte
    }

    /**
     * 生成拼音
     * @param msg
     * @return
     */
    public static String toPinYin(String msg)
    {
        String pinyin= PinyinHelper.convertToPinyinString(msg, "", PinyinFormat.WITHOUT_TONE);
        return pinyin;
    }
}
