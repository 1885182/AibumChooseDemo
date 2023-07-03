package com.sszt.basis.util

import android.app.Activity
import android.app.Application
import com.sszt.basis.ext.util.loge
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.ValueCallback

/**
 *
 * @param time 2021/8/27
 * @param des 腾讯X5WebView的
 * @param author Meng
 *https://x5.tencent.com/docs/access.html
 */

object X5WebUtils {

    fun init(application: Application) {

        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                "QbSdk onViewInitFinished is $arg0".loge()
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(application.applicationContext, cb)
    }

    /**
     * @param activity 只能是 activity 类型的 context
     * @param path  文件路径。格式为 android 本地存储路径格式，例如:/sdcard/Download/xxx.doc. 不支持 file:/// 格式
     * @param callBack filepath error
     * TbsReaderDialogClosed
     *  default browser
     * fileReaderClosed
     * 返回值：
     * 1:用 QQ 浏览器打开
     * 2:用 MiniQB 打开
     * 3:调起阅读器弹框
     *  -1:filePath 为空 打开失败
     * @TODO 调用 openFile()后 在activity销毁方法中一定要调用closeFile()方法
     *
     *@TODO  接入TBS可支持打开文件格式：doc、docx、ppt、pptx、xls、xlsx、pdf、txt、epub
     * 调用QQ浏览器可打开：rar（包含加密格式）、zip（包含加密格式）、tar、bz2、gz、7z（包含加密格式）、doc、docx、ppt、pptx、xls、xlsx、txt、pdf、epub、chm、html/htm、xml、mht、url、ini、log、bat、php、js、lrc、jpg、jpeg、png、gif、bmp、tiff 、webp、mp3、m4a、aac、amr、wav、ogg、mid、ra、wma、mpga、ape、flac
     */
    fun openFile(activity: Activity, path: String, callBack: ValueCallback<String>) {
        val params = HashMap<String, String>();
        params.put("style", "1")
        params.put("local", "true")
        params.put("topBarBgColor", "#3292ff")
//        params.put("memuData", jsondata);

        QbSdk.openFileReader(activity, path, params, callBack);
        //context: 调起 miniqb 的 Activity 的 context。此参数只能是 activity 类型的 context，
        // 不能设置为 Application 的 context。 filePath: 文件路径。格式为 android 本地存储路径格式，
        // 例如:/sdcard/Download/xxx.doc. 不支持 file:/// 格式。暂不支持在线文件。 extraParams:
        // miniqb 的扩展功能。为非必填项，若无需特殊配置，默认可传入null。扩展功能参考“文件功能定制” ValueCallback:
        // 提供 miniqb 打开/关闭时给调用方回调通知,以便应用层做相应处理，您可以在出现以下回调时结束您的进程，节约内存占用。主要回调值如下：
    }

    /**
     * @param activity 只能是 activity 类型的 context
     * @TODO 调用 openFile()后 在activity销毁方法中一定要调用此方法
     */
    fun closeFile(activity: Activity) {
        QbSdk.closeFileReader(activity)
    }

}