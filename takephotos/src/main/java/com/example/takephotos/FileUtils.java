package com.example.takephotos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huwang on 2017/3/30.
 */

public class FileUtils {
    /**
     * 保存图片
     *
     * @param path
     * @param data
     */
    public static void saveImg(String path, byte[] data) {
        // 创建 文件
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();

                // 输出流:由程序往外写
                FileOutputStream out = new FileOutputStream(file);
                // 写入图像数据
                out.write(data, 0, data.length);

                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// 生成新文件
        }

    }

    /**
     * 复制文件
     *
     * @param input
     * @param
     */
    public static void copyFile(InputStream input, String saveDir, String name) {
        // 创建 文件
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(saveDir+"/"+name);
        if (!file.exists()) {
            try {
                file.createNewFile();
                // 输出流:由程序往外写
                FileOutputStream out = new FileOutputStream(file);
                // 写入图像数据
                byte[] buffer = new byte[1024];// 768
                int len = 0;

                while ((len = input.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                out.flush();
                out.close();
                input.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// 生成新文件
        }

    }
}
