package com.example.huwang.mobilesafe20;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.example.huwang.mobilesafe20.bean.BlackNumberInfo;
import com.example.huwang.mobilesafe20.db.dao.BlackNumberDao;
import com.example.huwang.mobilesafe20.utils.CallLogUtil;

import org.junit.Test;

/**
 * Created by huwang on 2017/4/3.
 */
public class TestBlackNumberDao extends AndroidTestCase {
    @Test
    public void testAdd() {

        BlackNumberDao dao = new BlackNumberDao(mContext);

        for (int j = 1; j <= 10; j++) {
            // 每一页数据
            for (int i = 0; i < 20; i++) {
                BlackNumberInfo info = new BlackNumberInfo();
                info.id = "100" + i;
                info.number = "第" + j + "页数据" + i;
                info.mode = "0";
                dao.add(info);
            }
        }

    }
    @Test
    public void testDeleteLogs(){
        CallLogUtil.deleteCallLogByNumber(mContext, "10010");
    }
}
