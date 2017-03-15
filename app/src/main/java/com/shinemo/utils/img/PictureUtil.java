package com.shinemo.utils.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.shinemo.openim.service.msg.attachment.ImageAttachment;
import com.shinemo.openim.utils.Md5Util;
import com.shinemo.utils.FileUtils;

import java.io.File;

public class PictureUtil {


    public static ImageAttachment compressAndRotateToBitmapThumbFile(Context context, String path) {
        return compressAndRotateToBitmapThumbFile(context, path, ThumbnailUtils.BIG_SIZE);
    }

    public static ImageAttachment compressAndRotateToBitmapThumbFile(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        if (!"file".equals(uri.getScheme())) {
            path = ThumbnailUtils.writeToFile(context, uri);
        }
        return compressAndRotateToBitmapThumbFile(context, path);
    }


    public static String getPath(String path, int size){
        return "local:" + path + "_" +size;
    }

    public static ImageAttachment compressAndRotateToBitmapThumbFile(Context context,
                                                                     String path, int imageSize) {
        String destName = Md5Util.getStringMD5(getPath(path, imageSize));
        String destPath = FileUtils.getImageCachePath(context);
        File file = new File(destPath, destName);
        if (file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            } catch (Exception e) {
            }
            if (bitmap != null) {
                ImageAttachment vo = new ImageAttachment();
                vo.setWidth(bitmap.getWidth());
                vo.setHeight(bitmap.getHeight());
                vo.setPath(file.getAbsolutePath());
                bitmap.recycle();
                return vo;
            }
        }
        int degree = ThumbnailUtils.getOrientation(context, path, null);
        Bitmap bm = ThumbnailUtils.compressFileToBitmapThumb(path, imageSize, imageSize);
        if (bm == null)
            return null;
        Bitmap rotate = ThumbnailUtils.rotateBitmap(bm, degree);

        if (rotate != null) {
            int size = ThumbnailUtils.getBitmapCompress(bm, imageSize);
            if (ImageUtils.writeBitmap(destPath, destName, rotate, size)) {
                ImageAttachment vo = new ImageAttachment();
                vo.setWidth(rotate.getWidth());
                vo.setHeight(rotate.getHeight());
                vo.setPath(destPath + File.separator + destName);
                rotate.recycle();
                return vo;
            }
            rotate.recycle();
        }
        return null;
    }
}
