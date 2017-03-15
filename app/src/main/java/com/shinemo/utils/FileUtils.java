package com.shinemo.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Created by panjiejun on 2017/3/3.
 */

public class FileUtils {

    private static final String basePath = "/demo/";
    private static final String DIR_RECORD = "local_record";
    private static final String DIR_IMAGE = "local_image";

    public static String getCachePath(Context context) {
        String state = android.os.Environment.getExternalStorageState();
        String path = "";
        if (state != null && state.equals(android.os.Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= 8) {
                File file = context.getExternalCacheDir();
                if (file != null) {
                    path = file.getAbsolutePath();
                }
                if (TextUtils.isEmpty(path)) {
                    path = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();
                }
            } else {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
            }
        } else if (context.getCacheDir() != null) {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }

    public static String getImageCachePath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + DIR_IMAGE;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static File newImageCacheFile(Context context) {
        String root = getImageCachePath(context);
        if (!TextUtils.isEmpty(root)) {
            return new File(root, UUID.randomUUID().toString() + ".jpg");
        }
        return null;
    }

    public static File getRecordPath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = new File(path).getParentFile() + File.separator + DIR_RECORD;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        }
        return null;
    }


    /**
     * 检测内存卡是否可用
     */
    public static boolean isSDcardUsable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取内存卡路径
     */
    public static File getSDcardDir() {
        if (isSDcardUsable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return Environment.getDataDirectory();
        }
    }

    public static String getCameraPath(){
        String name = "demo/";
        String path = FileUtils.getSDcardDir() + basePath + name ;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public static String newCameraPath() {
        return getCameraPath() + "/"+ System.currentTimeMillis() + ".jpg";
    }

    public static String getFileMimeType(String path) {
        File f = new File(path);
        try {
            return f.toURL().openConnection().getContentType();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
