package com.example.huwang.mobilesafe20;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.huwang.mobilesafe20.bean.ProcessInfo;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/7.
 */

public class TestProcessInfos extends AndroidTestCase {
    @Test
    public void testProcess() {
        List<ProcessInfo> list = new ArrayList<ProcessInfo>();
        list = ProcessUtils.findAll(mContext);
        Log.i("zhang", String.valueOf(list.size()));
        for (ProcessInfo info : list) {
            Log.i("zhang", info.packageName);
        }
    }
}
