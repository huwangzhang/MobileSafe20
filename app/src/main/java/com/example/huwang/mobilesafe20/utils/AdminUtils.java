package com.example.huwang.mobilesafe20.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.huwang.mobilesafe20.receiver.MyAdminReceiver;

/**
 * Created by huwang on 2017/3/30.
 */

public class AdminUtils {
    /**
     * 锁屏:必须在忆条件下调用
     *
     * @param context
     */
    public static void lockScreen(Context context) {
        // 设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 设置密码
        dpm.resetPassword("123", 0);
        // 锁屏
        dpm.lockNow();
    }

    /**
     * 清理数据:必须在忆条件下调用
     *
     * @param context
     */
    public static void wipeData(Context context) {
        // 设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        // 清除数据
        dpm.wipeData(0);// 恢复出厂设置 一键还原
        // dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//sd格式化
        // dpm.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);//data
        // 格式化
    }

    /**
     * 创建账号
     *
     * @param act
     */
    public static void createAdmin(Activity act) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 权限文件配置的组件
        // ComponentName:描述 组件 Activity或者service
        ComponentName cn = new ComponentName(act, MyAdminReceiver.class);
        // 管理员账号
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
        act.startActivityForResult(intent, 0);
    }

    /**
     * 删除 账号
     * @param
     */
    public static void destoryAdmin(Context context) {

        ComponentName cn = new ComponentName(context.getPackageName(), MyAdminReceiver.class.getName());
        // 设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        //删除账号
        dpm.removeActiveAdmin(cn);

    }
    /**
     * 是否激活
     * @param
     */
    public static boolean isActivie(Context context) {

        ComponentName cn = new ComponentName(context.getPackageName(), MyAdminReceiver.class.getName());
        // 设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        return dpm.isAdminActive(cn);

    }
}
