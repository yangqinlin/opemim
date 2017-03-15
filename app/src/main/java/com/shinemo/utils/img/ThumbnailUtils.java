package com.shinemo.utils.img;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.shinemo.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ThumbnailUtils {

    private static final int UNCONSTRAINED = -1;

    public static final int THUMBNAIL_SIZE = 300;

    public static final int NORMAL_BITMAP_LENGTH = 180;
    public static final int BIG_SIZE = 1000;

    public static final int ORIGIN_BITMAP_SIZE = 600;
    public static final int ORIGIN_SIZE = 2000;

    public static String writeToFile(Context context, Uri uri) {
        File file = FileUtils.newImageCacheFile(context);
        FileOutputStream os = null;
        InputStream is = null;
        try {
            os = new FileOutputStream(file);
            is = context.getContentResolver().openInputStream(uri);
            byte[] b = new byte[1024];
            int count = 0;
            while (true) {
                int readLength = is.read(b);
                if (readLength == -1) {
                    break;
                }
                os.write(b, 0, readLength);
                count++;
                if (count % 5 == 0) {
                    os.flush();
                }
            }

        } catch (Exception e) {
        } catch (Throwable t) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static File compressAndRotateOriginBitmap(Context context,
                                                     String fromPath, String toPath) {
        return compressAndRotateToBitmapThumbFile(context, fromPath, toPath, ORIGIN_SIZE, ORIGIN_SIZE);
    }

    public static Bitmap compressFileToBitmapThumb(String filePath) {
        return compressFileToBitmapThumb(filePath, BIG_SIZE, BIG_SIZE);
    }

    public static File compressAndRotateToBitmapThumbFile(Context context,
                                                          Uri uri, int width, int height) {
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        if (!"file".equals(uri.getScheme())) {
            path = writeToFile(context, uri);
        }
        return compressAndRotateToBitmapThumbFile(context, path, width, height);
    }

    private static File compressAndRotateToBitmapThumbFile(Context context,
                                                           String path, int width, int height) {
        int degree = getOrientation(context, path, null);
        Bitmap bm = compressFileToBitmapThumb(path, width, height);
        File delFile = new File(path);
        if (delFile.exists()) {
            delFile.delete();
        }
        if (bm == null)
            return null;
        Bitmap rotate = rotateBitmap(bm, degree);

        if (rotate != null) {
            String nameString = UUID.randomUUID().toString() + ".jpg";
            String pathString = FileUtils.getImageCachePath(context);
            int size = getBitmapCompress(bm,  width);
            if (ImageUtils.writeBitmap(pathString, nameString, rotate, size)) {
                rotate.recycle();
                return new File(pathString, nameString);
            }
            rotate.recycle();
        }
        return null;
    }

    public static File compressAndRotateToBitmapThumbFile(Context context,
                                                          String originPath, String targetPath) {
        return compressAndRotateToBitmapThumbFile(context, originPath, targetPath, BIG_SIZE, BIG_SIZE);
    }

    public static File compressAndRotateToBitmapThumbFile(Context context,
                                                          String originPath, String targetPath, int width, int height) {
        int degree = getOrientation(context, originPath, null);
        Bitmap bm = compressFileToBitmapThumb(originPath, width, height);
        if (bm == null)
            return null;
        Bitmap rotate = rotateBitmap(bm, degree);

        if (rotate != null) {
            int size = getBitmapCompress(bm, width);
            if (ImageUtils.writeBitmap(targetPath, rotate, size)) {
                rotate.recycle();
                return new File(targetPath);
            }
            rotate.recycle();
        }
        return null;
    }

    public static Bitmap compressFileToBitmapThumb(String filePath, int width,
                                                   int height) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        FileDescriptor fd = null;
        try {
            fis = new FileInputStream(file);
            fd = fis.getFD();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        int targetSize = Math.min(width, height);
        int maxPixels = width * height;
        if (isLargeImage(options)) {
            if (options.outWidth != 0) {
                maxPixels = options.outHeight * options.outWidth;
                if (options.outWidth >= 800) {
                    maxPixels = maxPixels / (options.outWidth / 400);
                }
            } else {
                maxPixels = maxPixels * 4;
            }
        }
        int sampleSize = computeSampleSize(options, targetSize, maxPixels);
        int maxSample = Math.max(sampleSize, 40);
        options.inJustDecodeBounds = false;

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        for (int index = sampleSize; index <= maxSample; index = index * 2) {
            try {
                options.inSampleSize = index;
                Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null,
                        options);
                if (null != bm) {
                    return bm;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取照片的角度 两种方式:1.根据绝对路径或根据Uri
     *
     * @param imagePath 照片的路径
     * @param context
     * @param photoUri
     */
    public static int getOrientation(Context context, String imagePath,
                                     Uri photoUri) {
        int nOrientation = 0;
        if (!TextUtils.isEmpty(imagePath)) {
            try {
                ExifInterface exif = new ExifInterface(imagePath);
                nOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                switch (nOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_NORMAL:
                        return 0;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (context != null && photoUri != null) {
            Cursor cursor = null;
            try {
                cursor = context
                        .getContentResolver()
                        .query(photoUri,
                                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                                null, null, null);

                if (cursor == null || cursor.getCount() != 1) {
                    return 0;
                }

                cursor.moveToFirst();
                int ret = cursor.getInt(0);
                return ret;
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return 0;
    }

    /**
     * 获取bitmap大小，转化为实际占用空间物理大小
     *
     * @param bm
     * @return
     */
    public static int getBitmapCompress(Bitmap bm, int  imageSize) {

        long size = getBitmapsize(bm);

        int initCompress = 80;

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (size > 0) {
            size = size / 1024 / 8;
        } else {
            size = width * height / 1024 / 8;
        }
        int totalSize = NORMAL_BITMAP_LENGTH;
        if(imageSize > BIG_SIZE){
            totalSize = ORIGIN_BITMAP_SIZE;
        }

        if (bm.getHeight() / bm.getWidth() >= 3) {
            totalSize = totalSize * 2;

        }
        if (size > totalSize*3) {
            initCompress = 50;
        } else if (size > totalSize*2.5) {
            initCompress = 55;
        }else if (size > totalSize*2) {
            initCompress = 60;
        }else if (size >= totalSize*1.5) {
            initCompress = 68;
        }else if (size >= totalSize*1.2) {
            initCompress = 75;
        }

        if(imageSize > BIG_SIZE){
            initCompress+=5;
        }

        int compress = initCompress;
        try {
            if(compress < 80){
                for (int i = compress; i >= 10; i = i - 10) {
                    compress = i;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, i, stream);
                    byte[] imageInByte = stream.toByteArray();
                    long endSize = imageInByte.length / 1024;
                    if (endSize <= totalSize) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            compress = 10;
        } catch (Throwable t) {
            compress = 10;
        }
        return compress;
    }

    public static long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
                .ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
                .min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED)
                && (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, b.getWidth() / 2, b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                return b2;
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return b;
    }

    public static boolean isLargeImage(Options options) {

        return (options.outHeight > 0 && options.outWidth > 0) ? (options.outHeight
                / options.outWidth >= 3)
                : false;
    }
}
