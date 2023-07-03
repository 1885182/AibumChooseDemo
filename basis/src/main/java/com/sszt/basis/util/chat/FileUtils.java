package com.sszt.basis.util.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.ren.newalbumchoose.activity.PlayVideoActivity;
import com.ren.newalbumchoose.weight.SeeMultiPicture;
import com.sszt.basis.R;
import com.tencent.smtt.sdk.QbSdk;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author 嘻哈
 * @DateTime 2022/7/27 17:49
 */
public class FileUtils {
    public static byte[] autoCreateAESKey = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    public static Handler mHandler;
    public static DownLoadFileInterface mMobCallback;
    private static final int CODE_SUCCESS = 200;//成功
    private static final int CODE_ERROR = 300;//失败
    private static final int CODE_PROGRESS = 188;//进度

    public static final String DOWN_LOAD_FILE = "/document/";




    public static void downloadFile(String url, String savePath, String name, int index, DownLoadFileInterface chatView) {
        mMobCallback = chatView;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mMobCallback == null) {
                    return;
                }
                switch (msg.what) {
                    case CODE_SUCCESS:
                        mMobCallback.onSuccess(msg.arg2);
                        break;
                    case CODE_ERROR:
                        mMobCallback.onError(msg.obj + "");
                        break;
                    case CODE_PROGRESS:
                        mMobCallback.onProgress(msg.arg1, msg.arg2);

                        Log.i("DOWNLOAD_HANDLE", msg.arg1 + "--");
                        break;
                }
            }
        };


        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD", "startTime=" + startTime);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD", "download failed");
                Message msg = Message.obtain();
                msg.obj = e.getMessage();
                msg.what = CODE_ERROR;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                CipherInputStream cipherInputStream = null;
                byte[] buf = new byte[1024];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = BaseApplication.instance.getExternalFilesDir(GlobalConstant.DOWN_LOAD_FILE).getPath();
                try {
                    is = response.body().byteStream();
                    //加密方法开始
                    SecretKeySpec key = new SecretKeySpec(autoCreateAESKey, "AES");
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    // 初始化加密器
                    cipher.init(Cipher.ENCRYPT_MODE, key,
                            new IvParameterSpec(new byte[cipher.getBlockSize()]));
                    cipherInputStream = new CipherInputStream(is, cipher);
                    //加密方法结束
                    long total = response.body().contentLength();
                    File file = new File(savePath, name);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    int lastProgress = 0;
                    while ((len = cipherInputStream.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);

                        // 下载中
                        Log.i("DOWNLOAD", progress + "--" + index);
                        if (progress > lastProgress || progress == 100) {
                            lastProgress = progress + 5;
                            Message msg = Message.obtain();
                            msg.arg1 = progress;
                            msg.arg2 = index;
                            msg.what = CODE_PROGRESS;
                            mHandler.sendMessage(msg);
                        }

                    }
                    fos.flush();
                    Message msg = Message.obtain();
                    msg.arg2 = index;
                    msg.what = CODE_SUCCESS;
                    mHandler.sendMessage(msg);
                    // 下载完成
                    //                    listener.onDownloadSuccess();
                    Log.i("DOWNLOAD", "download success");
                    Log.i("DOWNLOAD", "totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.obj = e.getMessage();
                    msg.what = CODE_ERROR;
                    mHandler.sendMessage(msg);
                    //                    listener.onDownloadFailed();
                    Log.i("DOWNLOAD", "download failed" + e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (cipherInputStream != null)
                            cipherInputStream.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

        }
        return "";
    }


    private static String ALGORITHM_AES = "AES";
    private static int AES_KEY_LEN = 128;


    private static byte[] getAutoCreateAESKey() throws Exception {

        // 实例化一个AES加密算法的密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
        // 初始化密钥生成器，指定密钥位数为128位
        keyGenerator.init(AES_KEY_LEN, new SecureRandom());
        // 生成一个密钥
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static boolean isDownLoad(long size, Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(DOWN_LOAD_FILE) + "/" + fileName);
        if (file.exists() && file.isFile()) {
            Log.e("tagggggggg", "文件大小" + file.length() + "----" + size);
            if (file.length() >= size) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDownLoad(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(DOWN_LOAD_FILE) + "/" + fileName);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }


    /**
     * 文件展示图片
     */
    public static int getFileIcon(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "";
        }
        fileName = fileName.toLowerCase();
        int fileIcon = R.drawable.ic_chat_file_default;
        if (fileName.endsWith(".txt")) {
            fileIcon = R.drawable.ic_chat_file_txt;
        } else if (fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") ||
                fileName.endsWith(".gif") ||
                fileName.endsWith(".pic")) {
            fileIcon = R.drawable.ic_chat_file_image;
        }/* else if (fileName.endsWith(".mp3") ||
                fileName.endsWith(".amr") ||
                fileName.endsWith(".ram") ||
                fileName.endsWith(".wav") ||
                fileName.endsWith(".aif")) {
            fileIcon = R.mipmap.ic_launcher;
        }*/ else if (fileName.endsWith(".mp4") ||
                fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") ||
                fileName.endsWith(".rm") ||
                fileName.endsWith(".mpg")) {
            fileIcon = R.drawable.ic_chat_file_video;
        } else if (fileName.endsWith(".doc") ||
                fileName.endsWith(".docx")) {
            fileIcon = R.drawable.ic_chat_file_word;
        } else if (fileName.endsWith(".xls") ||
                fileName.endsWith(".xlsx")) {
            fileIcon = R.drawable.ic_chat_file_excel;
        } else if (fileName.endsWith(".ppt") ||
                fileName.endsWith(".pptx")) {
            fileIcon = R.drawable.ic_chat_file_ppt;
        } else if (fileName.endsWith(".pdf")) {
            fileIcon = R.drawable.ic_chat_file_pdf;
        } else {
            //不支持的文件格式
            fileIcon = R.drawable.ic_chat_file_default;
        }
        return fileIcon;
    }


    /**
     * 文件打开方式
     */
    public static int getFileOpenWith(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "";
        }
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".txt") ||
                fileName.endsWith(".doc") ||
                fileName.endsWith(".docx") ||
                fileName.endsWith(".ppt") ||
                fileName.endsWith(".pptx") ||
                fileName.endsWith(".pdf") ||
                fileName.endsWith(".xls") ||
                fileName.endsWith(".xlsx")) {
            return 1;
        } else if (fileName.endsWith(".mp4") ||
                fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") ||
                fileName.endsWith(".rm") ||
                fileName.endsWith(".mpg")) {
            return 2;
        } else if (fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") ||
                fileName.endsWith(".gif") ||
                fileName.endsWith(".pic")) {
            return 3;
        } else {
            return 0;
        }

    }


    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString() + "K";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                + "TB";
    }


    public static final String DOC = "application/msword";
    public static final String TEXT = "text/plain";

    public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final String XLS = "application/vnd.ms-excel application/x-excel";

    public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static final String PPT = "application/vnd.ms-powerpoint";

    public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    public static final String PDF = "application/pdf";
    public static final String JPG = "image/jpg";
    public static final String PNG = "image/png";
    public static final String JPEG = "image/jpeg";
    public static final String MP4 = "video/mp4";
    public static final String AVI = "video/avi";
    public static final String MOV = "video/mov";


    public static final int CHECK_FILE_REQUEST_CODE = 318;

    public static void checkFile(Activity context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] str = new String[]{"video/*", "image/*", "text/*", DOC, DOCX, XLS, XLSX, PPT, PPTX, PDF, JPG, PNG, JPEG, MP4, AVI, MOV, TEXT};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, str);
        intent.setType("image/*;video/*");  //   /*/ 此处是任意类型任意后缀
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        context.startActivityForResult(intent, CHECK_FILE_REQUEST_CODE);
    }


    public static void openFile(Activity context, String fileName, String fileUrl) {
        String baseCachePath = context.getExternalCacheDir().getPath() + FileUtils.DOWN_LOAD_FILE;
        String baseFilePath = context.getExternalFilesDir(FileUtils.DOWN_LOAD_FILE).getPath();
        if (FileUtils.getFileOpenWith(fileName) == 0) {
            //dialog.dismiss()
            ToastUtils.showShort("APP暂不支持打开此类型文件~~~~~");
            return;
        } else if (FileUtils.getFileOpenWith(fileName) == 2) {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.putExtra(PlayVideoActivity.URL_VIDEO, fileUrl);
            context.startActivity(intent);
        } else if (FileUtils.getFileOpenWith(fileName) == 3) {
            SeeMultiPicture.load(fileUrl)
                    .start(context);
        } else if (FileUtils.getFileOpenWith(fileName) == 1) {
            if (FileUtils.isDownLoad(context, fileName)) { //是否下载
                if (QbSdk.getTbsVersion(context) == 0) {
                    ToastUtils.showShort("插件下载失败,暂不支持打开.");
                    //dialog.dismiss()
                    return;
                }
                try {
                    //解密
                    FileUtils.aesDecryptFile(
                            context,
                            baseFilePath +"/"+fileName,
                            baseCachePath + fileName
                    );
                    //打开文档
                    HashMap<String, String> params = new HashMap<String, String>();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pkgName", context.getPackageName());
                    params.put("style", "1");
                    params.put("local", "false");//进入文件查看器
                    params.put("memuData", jsonObject.toString());
                    QbSdk.openFileReader(
                            context, baseCachePath + fileName, params, s -> {
                                if (s == "fileReaderClosed") {
                                    //文件阅读关闭
                                    //删除此文件baseCachePath + fileName
                                    File file = new File(baseCachePath + fileName);
                                    if (file.isFile()) {
                                        file.delete();
                                    }
                                }
                                //dialog.dismiss()
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("taggggg", e.toString());
                    ToastUtils.showShort("文件损坏");
                    //dialog.dismiss()
                    return;
                }

            } else {

                FileUtils.downloadFile(
                        fileUrl,
                        baseFilePath,
                        fileName,
                        0,
                        new DownLoadFileInterface() {
                            @Override
                            public void onSuccess(int index) {
                                openFile(context, fileName, fileUrl);
                            }

                            @Override
                            public void onProgress(long progress, int index) {

                            }

                            @Override
                            public void onError(String str) {

                            }
                        }
                );
                ToastUtils.showShort("正在下载,请稍后..");

            }
        }
    }


    /**
     * AES加密
     *
     * @param sourceFile  源文件
     * @param encryptFile 加密文件
     * @throws Exception 抛出异常
     */
    public static void aesEncryptFile(Context context, String sourceFile, String encryptFile) throws Exception {

        // 创建AES密钥
        SecretKeySpec key = new SecretKeySpec(autoCreateAESKey, "AES");
        // 创建加密引擎(CBC模式)。Cipher类支持DES，DES3，AES和RSA加加密
        // AES：算法名称
        // CBC：工作模式
        // PKCS5Padding：明文块不满足128bits时填充方式（默认），即在明文块末尾补足相应数量的字符，
        // 且每个字节的值等于缺少的字符数。另外一种方式是ISO10126Padding，除最后一个字符值等于少的字符数
        // 其他字符填充随机数。
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, key,
                new IvParameterSpec(new byte[cipher.getBlockSize()]));
        // 原始文件流
        FileInputStream inputStream = new FileInputStream(sourceFile);
        String newPath = context.getExternalFilesDir(DOWN_LOAD_FILE).getPath();
        File file = new File(newPath);

        if (!file.exists()) {
            file.mkdir();
        }
        // 加密文件流
        FileOutputStream outputStream = new FileOutputStream(encryptFile);
        // 以加密流写入文件
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] tmpArray = new byte[1024];
        int len;
        while ((len = cipherInputStream.read(tmpArray)) != -1) {

            outputStream.write(tmpArray, 0, len);
            outputStream.flush();
        }
        cipherInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * AES解密
     *
     * @param encryptFile 加密文件
     * @param decryptFile 解密文件
     * @throws Exception 抛出异常
     */
    public static void aesDecryptFile(Context context, String encryptFile, String decryptFile) throws Exception {

        // 创建AES密钥，即根据一个字节数组构造一个SecreteKey
        // 而这个SecreteKey是符合指定加密算法密钥规范

        SecretKeySpec key = new SecretKeySpec(autoCreateAESKey, "AES");
        // 创建解密引擎(CBC模式)
        // Cipher类支持DES，DES3，AES和RSA加解密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key,
                new IvParameterSpec(new byte[cipher.getBlockSize()]));
        String newPath = context.getExternalCacheDir() + "/document";
        File file = new File(newPath);

        if (!file.exists()) {
            file.mkdir();
        }
        // 加密文件流
        FileInputStream fileInputStream = new FileInputStream(encryptFile);
        // 解密文件流
        FileOutputStream fileOutputStream = new FileOutputStream(decryptFile);
        // 以解密流写出文件
        CipherOutputStream cipherOutputStream =
                new CipherOutputStream(fileOutputStream, cipher);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fileInputStream.read(buffer)) >= 0) {

            cipherOutputStream.write(buffer, 0, len);
        }
        cipherOutputStream.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

    /*获取屏幕宽度*/
    public static int getWindowsPixelWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 取得窗口属性
        manager.getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        return dm.widthPixels;
    }

    /*获取屏幕高度*/
    public static int getWindowsPixelHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 取得窗口属性
        manager.getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        return dm.heightPixels;
    }

}
