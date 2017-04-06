package com.example.huwang.mobilesafe20;

import android.util.Log;

import com.example.huwang.mobilesafe20.utils.FormatUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huwang on 2017/4/4.
 */

public class TestPinHelper {
    @Test
    public void testConvertToPinyinStringStringStringPinyinFormat() {
        // 快播: KUAIBO KUAI,BO
        // 内涵段子:NEIHANDUANZI
        // PinyinHelper.convertToPinyinString(汉字, 拼音分隔符,音调)
        // String pinyin=PinyinHelper.convertToPinyinString("快播",
        // "",PinyinFormat.WITHOUT_TONE);
        // Log.i("zhang", pinyin+"");

        List<String> list = new ArrayList<String>();

        list.add("北京");
        list.add("澳门");
        list.add("东莞");
        // Comparator:java提供比较器 代表一种比较标准
        Comparator<String> cmp = new Comparator<String>() {

            // 比较方法
            @Override
            public int compare(String lhs, String rhs) {
                String lhsPinYin = FormatUtils.toPinYin(lhs);
                String rhsPinYin = FormatUtils.toPinYin(rhs);
                return lhsPinYin.compareTo(rhsPinYin);// 0 相等待 -1 小于 1大于
            }
        };
        // Collections:操作集合的工具 ：排序
        Collections.sort(list, cmp);
        for (String temp : list) {
            Log.i("zhang", temp);
        }
    }
}
