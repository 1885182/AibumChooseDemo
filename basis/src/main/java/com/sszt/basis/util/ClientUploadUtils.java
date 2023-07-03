package com.sszt.basis.util;

import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 */
public class ClientUploadUtils {

    public static void upload(String url, String filePath, String fileName, String token, Callback callBack) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();
        builder.connectTimeout(1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(1000, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.w("httpMessage", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.build();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("video/*"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .addHeader("access_token", token)
                .addHeader("User-Agent", "android")
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);

        call.enqueue(callBack);


    }

    public static void postFormBody(String url, String token,FormBody formBody, Callback callBack) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();
        builder.connectTimeout(1000, TimeUnit.MILLISECONDS)
                .readTimeout(1000, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.w("httpMessage", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.build();
        Request request = new Request.Builder()
                .addHeader("access_token", token)
                .addHeader("User-Agent", "android")
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(callBack);


    }


}
