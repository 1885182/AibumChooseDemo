package com.ren.newalbumchoose.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片旋转角度修正工具类
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:3.0.0
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class ImageRotateUtil {

    public static ImageRotateUtil of() {
        return new ImageRotateUtil();
    }

    private ImageRotateUtil() {
    }


    /**
     * 纠正照片的旋转角度
     *
     * @param path
     */
    public void correctImage(Context context, Uri path) {

        String imagePath = UriParse.parseOwnUri(context, path);
        int degree = getBitmapDegree(imagePath);
        if ((degree) != 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null) {
                return;
            }
            Bitmap resultBitmap = rotateBitmapByDegree(bitmap, degree);
            if (resultBitmap == null) {
                return;
            }
            try {
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(imagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 纠正照片的旋转角度
     *
     * @param path   在android11下 /storage/emulated/0/	Environment.getExternalStoragePublicDirectory(type)	外部九大公有目录
     *               new FileOutputStream(new File(imagePath)) 使用这个方法  会报错：文件找不到的错误  所以才多了一个 {@link  #degree}
     * @param degree 图片方向
     */
    public void correctImage(Context context, Uri path, int degree) {

        String imagePath = UriParse.parseOwnUri(context, path);

        if ((degree) != 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null) {
                return;
            }
            Bitmap resultBitmap = rotateBitmapByDegree(bitmap, degree);
            if (resultBitmap == null) {
                return;
            }
            try {
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(imagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 纠正照片的旋转角度
     *
     * @param path   在android11下 /storage/emulated/0/	Environment.getExternalStoragePublicDirectory(type)	外部九大公有目录
     *               new FileOutputStream(new File(imagePath)) 使用这个方法  会报错：文件找不到的错误  所以才多了一个 {@link  #degree}
     * @param degree 图片方向
     */
    public void correctImage(Context context, String imagePath, int degree) {


        if ((degree) != 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null) {
                return;
            }
            Bitmap resultBitmap = rotateBitmapByDegree(bitmap, degree);
            if (resultBitmap == null) {
                return;
            }
            try {
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(imagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @return角度 获取从相册中选中图片的角度
     */

    public static float readPictureDegree(String path) {
        int degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:

                    degree = 90;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:

                    degree = 180;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:

                    degree = 270;

                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return degree;

    }

    /**
     * 旋转图片，使图片保持正确的方向。
     */

    public static Bitmap rota(float degrees, Bitmap bitmap) {
        Matrix matrix = new Matrix();

        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (null != bitmap) {
            bitmap.recycle();

        }

        return bmp;

    }

}
