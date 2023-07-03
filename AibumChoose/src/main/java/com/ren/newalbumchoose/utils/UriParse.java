package com.ren.newalbumchoose.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * Uri解析工具类
 * Author: JPH
 * Date: 2015/8/26 0026 16:23
 */
public class UriParse {
    private static final String TAG =  "UriParse";

    /**
     * 将scheme为file的uri转成FileProvider 提供的content uri
     *
     * @param context
     * @param uri
     * @return
     */
    public static Uri convertFileUriToFileProviderUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return getUriForFile(context, new File(uri.getPath()));
        }
        return uri;

    }

    /**
     * 获取一个临时的Uri, 文件名随机生成
     *
     * @param context
     * @return
     */
    public static Uri getTempUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String timeStampStart= context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(timeStampStart + timeStamp + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * 获取一个临时的Uri, 通过传入字符串路径
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getTempUri(Context context, String path) {
        File file = new File(path);
        return getTempUri(context, file);
    }

    /**
     * 获取一个临时的Uri, 通过传入File对象
     *
     * @param context
     * @return
     */
    public static Uri getTempUri(Context context, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     *
     * @param context
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context,  getFileProviderName(context), file);
    }
    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }
    /**
     * 将TakePhoto 提供的Uri 解析出文件绝对路径
     *
     * @param uri
     * @return
     */
    public static String parseOwnUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path;
        if (TextUtils.equals(uri.getAuthority(),  getFileProviderName(context))) {
            path = new File(uri.getPath().replace("camera_photos/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    /**
     * 通过URI获取文件的路径
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/6/6 0006 20:01
     */
    public static String getFilePathWithUri(Uri uri, Activity activity) throws Exception {
        if (uri == null) {
            Log.w(TAG, "uri is null,activity may have been recovered?");
            throw new Exception("uri is null,activity may have been recovered?");
        }
        File picture = getFileWithUri(uri, activity);
        String picturePath = picture == null ? null : picture.getPath();
        if (TextUtils.isEmpty(picturePath)) {
            throw new Exception("TExceptionType.TYPE_URI_PARSE_FAIL");
        }
        if (! checkMimeType(activity,  getMimeType(activity, uri))) {
            throw new Exception("TExceptionType.TYPE_NOT_IMAGE");
        }
        return picturePath;
    }
    /**
     * To find out the extension of required object in given uri
     * Solution by http://stackoverflow.com/a/36514823/1171484
     */
    public static String getMimeType(Activity context, Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            //If scheme is a content
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            if (TextUtils.isEmpty(extension)) {
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            }
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file
            // name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            if (TextUtils.isEmpty(extension)) {
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            }
        }
        if (TextUtils.isEmpty(extension)) {
            extension = getMimeTypeByFileName( getFileWithUri(uri, context).getName());
        }
        return extension;
    }
    public static String getMimeTypeByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
    /**
     * 检查文件类型是否是图片
     *
     * @param minType
     * @return
     */
    public static boolean checkMimeType(Context context, String minType) {
        boolean isPicture =
                TextUtils.isEmpty(minType) ? false : ".jpg|.gif|.png|.bmp|.jpeg|.webp|".contains(minType.toLowerCase()) ? true : false;
        if (!isPicture) {
            Toast.makeText(context, "不是图片", Toast.LENGTH_SHORT).show();
        }
        return isPicture;
    }

    /**
     * 通过URI获取文件
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/10/25
     */
    public static File getFileWithUri(Uri uri, Activity activity) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex >= 0) {
                picturePath = cursor.getString(columnIndex);  //获取照片路径
            } else if (TextUtils.equals(uri.getAuthority(),  getFileProviderName(activity))) {
                picturePath = parseOwnUri(activity, uri);
            }
            cursor.close();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }

    /**
     * 通过从文件中得到的URI获取文件的路径
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/6/6 0006 20:01
     */
    public static String getFilePathWithDocumentsUri(Uri uri, Activity activity) throws Exception {
        if (uri == null) {
            Log.e(TAG, "uri is null,activity may have been recovered?");
            return null;
        }
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && uri.getPath().contains("document")) {
            File tempFile =  getTempFile(activity, uri);
            try {
                 inputStreamToFile(activity.getContentResolver().openInputStream(uri), tempFile);
                return tempFile.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new Exception("TExceptionType.TYPE_NO_FIND");
            }
        } else {
            return getFilePathWithUri(uri, activity);
        }
    }

    /**
     * InputStream 转File
     */
    public static void inputStreamToFile(InputStream is, File file) throws Exception {
        if (file == null) {
            Log.i(TAG, "inputStreamToFile:file not be null");
            throw new Exception("inputStreamToFile:file not be null");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int i;
            while ((i = is.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            Log.e(TAG, "InputStream 写入文件出错:" + e.toString());
            throw new Exception("InputStream 写入文件出错:\" + e.toString()");
        } finally {
            try {
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取临时文件
     *
     * @param context
     * @param photoUri
     * @return
     */
    public static File getTempFile(Activity context, Uri photoUri) throws Exception {
        String minType = getMimeType(context, photoUri);
        if (!checkMimeType(context, minType)) {
            throw new Exception("TExceptionType.TYPE_NOT_IMAGE");
        }
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        File photoFile = new File(filesDir, UUID.randomUUID().toString() + "." + minType);
        return photoFile;
    }


}
