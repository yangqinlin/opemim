package com.shinemo.utils.img;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

	public static final float DISPLAY_WIDTH = 200;
	public static final float DISPLAY_HEIGHT = 200;

	public static boolean writeBitmap(String path, String name, Bitmap bitmap,
                                      int compressRate) {
		if (null == bitmap || TextUtils.isEmpty(path)
				|| TextUtils.isEmpty(name))
			return false;
		boolean bPng = false;
		if (name.endsWith(".png")) {
			bPng = true;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		File _file = new File(path, name);
		boolean bNew = true;
		if (_file.exists()) {
			bNew = false;
			_file = new File(path, name + ".tmp");
			_file.delete();
		}
		FileOutputStream fos = null;
		boolean bOK = false;
		try {
			fos = new FileOutputStream(_file);
			if (bPng) {
				bitmap.compress(Bitmap.CompressFormat.PNG, compressRate, fos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, fos);
			}
			bOK = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
					if (bNew == false && bOK) {
						_file.renameTo(new File(path, name));
					}
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public static boolean writeBitmap(String path, Bitmap bitmap,
                                      int compressRate) {
		if (null == bitmap || TextUtils.isEmpty(path))
			return false;

		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, compressRate, fos);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (Exception e) {
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}

		}
		return buffer;
	}

	public static Bitmap createBitmap(float a, Bitmap bmp){
        if (a == 0) {
            return bmp;
        }
        int w = (int)(a * bmp.getWidth());
        return Bitmap.createBitmap(bmp, 0, 0, w, bmp.getHeight());
    }

	/**
     * 从path中获取图片信息
     * @param path
     * @return
     */
    public static Bitmap decodeBitmap(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
        int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_HEIGHT);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }
}
