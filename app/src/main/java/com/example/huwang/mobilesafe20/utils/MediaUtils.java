package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by huwang on 2017/3/28.
 */

public class MediaUtils {
    /**
     * 循环播放 指定 的音频文件
     * @param context
     * @param resid
     */
    public static void play(Context context, int resid) {
        // 创建播放器
        MediaPlayer player = MediaPlayer.create(context, resid);
        // 最大声
        player.setVolume(1.0f, 1.0f);// 0f 1.0f
        // 循环
        player.setLooping(true);
        // 开始
        player.start();
    }
}
