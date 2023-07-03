package com.sszt.basis.util.chat;

/**
 * @Author 嘻哈
 * @DateTime 2022/9/1 16:14
 */
public interface DownLoadFileInterface {
    void onSuccess(int index);

    void onProgress(long progress, int index);

    void onError(String str);
}
