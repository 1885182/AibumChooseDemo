package com.sszt.basis.ext

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.ren.newalbumchoose.weight.SeeMultiPicture
import com.sszt.basis.callback.livedata.StringLiveData
import com.sszt.basis.ext.util.loge
import com.sszt.resources.IRoute
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList

fun Activity.getResColor(color: Int) = ContextCompat.getColor(this, color)
fun Fragment.getResColor(color: Int) = ContextCompat.getColor(requireActivity(), color)

fun Activity.tipDialog(msg: String = "请稍后...") = WaitDialog.show(msg ?: "请稍后")
    .setDialogLifecycleCallback(object : DialogLifecycleCallback<WaitDialog>() {
        override fun onShow(dialog: WaitDialog?) {
            super.onShow(dialog)
            if (KeyboardUtils.isSoftInputVisible(this@tipDialog)) {
                KeyboardUtils.hideSoftInput(this@tipDialog.window)
            }
        }

        override fun onDismiss(dialog: WaitDialog?) {
            super.onDismiss(dialog)
            if (KeyboardUtils.isSoftInputVisible(this@tipDialog)) {
                KeyboardUtils.hideSoftInput(this@tipDialog.window)
            }
        }
    })

fun Fragment.tipDialog(msg: String = "请稍后...") = WaitDialog.show(msg ?: "请稍后")
    .setDialogLifecycleCallback(object : DialogLifecycleCallback<WaitDialog>() {
        override fun onShow(dialog: WaitDialog?) {
            super.onShow(dialog)
            if (KeyboardUtils.isSoftInputVisible(this@tipDialog.requireActivity())) {
                KeyboardUtils.hideSoftInput(this@tipDialog.requireActivity().window)
            }
        }

        override fun onDismiss(dialog: WaitDialog?) {
            super.onDismiss(dialog)
            if (KeyboardUtils.isSoftInputVisible(this@tipDialog.requireActivity())) {
                KeyboardUtils.hideSoftInput(this@tipDialog.requireActivity().window)
            }
        }
    })

fun Activity.successDialog(msg: String?, callback: () -> Unit) =
    TipDialog.show(msg ?: "成功", WaitDialog.TYPE.SUCCESS)
        .setDialogLifecycleCallback(object : DialogLifecycleCallback<WaitDialog>() {
            override fun onDismiss(dialog: WaitDialog?) {
                super.onDismiss(dialog)
                callback.invoke()
            }
        })

fun Fragment.successDialog(msg: String?, callback: () -> Unit) =
    TipDialog.show(msg ?: "成功", WaitDialog.TYPE.SUCCESS)
        .setDialogLifecycleCallback(object : DialogLifecycleCallback<WaitDialog>() {
            override fun onDismiss(dialog: WaitDialog?) {
                super.onDismiss(dialog)
                callback.invoke()
            }
        })

fun Activity.errorDialog(msg: String?) {
    TipDialog.show(msg ?: "失败", WaitDialog.TYPE.ERROR)
    if (msg == "TOKEN超期") {
        SPUtils.getInstance().clear()
        ActivityUtils.finishAllActivities()
        router(IRoute.login_login)
    }


}

fun Fragment.errorDialog(msg: String?) {
    TipDialog.show(msg ?: "失败", WaitDialog.TYPE.ERROR)
    if (msg == "TOKEN超期") {
        SPUtils.getInstance().clear()
        ActivityUtils.finishAllActivities()
        router(IRoute.login_login)
    }
}

fun Activity.getResString(string: Int) = resources.getString(string)
fun Fragment.getResString(string: Int) = resources.getString(string)


fun StringLiveData.valueIsEmpty() = this.value.isNullOrEmpty()

/**
 * 按要求去掉最后一个字符
 * @param lastStr 要去掉的字符
 */
fun StringBuilder.delLastChar(lastStr: String): String {

    if (this.isNullOrEmpty()) return ""

    val last = this.last()
    if (last.toString() == lastStr)
        return this.substring(0, this.length.minus(1))
    else
        return this.toString()

}

fun String.delLastChar(lastStr: String): String {
    if (this.isNullOrEmpty()) return ""
    val last = this.last()
    if (last.toString() == lastStr)
        return this.substring(0, this.length.minus(1))
    else
        return this.toString()

}

fun String.toF(): Float {
    if (this.isNullOrBlank()) {
        return 0f
    } else {
        return this.toFloat()
    }
}

fun String.toI(): Int {
    if (this.isNullOrBlank()) {
        return 0
    } else {
        return this.toInt()
    }
}

fun Long.toTime(): String {
    val time = this / 1000
//时间戳小于60秒
    if (time < 60) {
        val ts: String = if (time < 10) {
            "0$time"

        } else {
            "$time"
        }

        return "00:00:$ts"

    } else if (time in 59..3599) {
//时间戳在60秒和3600秒之间（没到小时）
        val miao = time % 60
        val miaos: String
        miaos = if (miao < 10) {
            "0$miao"
        } else {
            "$miao"
        }
        val min = time / 60
        val mins: String
        mins = if (min < 10) {
            "0$min"
        } else {
            "$min"
        }

        return "00:$mins:$miaos"

    } else if (time > 3599) {
//时间戳大于3600秒（到小时了）
        val h = time / 3600

        val hs: String
        hs = if (h < 10) {
            "0$h"
        } else {
            "$h"
        }
//取余
        val m = time % 3600

        if (m < 60) {
            val miaos: String = if (m < 10) {
                "0$m"
            } else {
                "$m"
            }

            return "$hs:00:$miaos"

        } else {

            val miao = m % 60
            val miaos: String
            miaos = if (miao < 10) {
                "0$miao"
            } else {
                "$miao"
            }

            val min = m / 60
            val mins: String
            mins = if (min < 10) {
                "0$min"
            } else {
                "$min"
            }

            return "$hs:$mins:$miaos"

        }

    }

    return ""

}

/**
 * 分钟转时间
 *
 */
fun Int.timeToFormat(): String {
    if (this == null) return ""

    val time = Math.abs(this)

    when {
        time < 60 -> {
            return ("${time}分钟")
        }
        time in 60..1440 -> {
            val mm = time % 60
            val h = (time - mm).div(60)
            return ("${h}小时${mm}分钟")
        }
        else -> {
            val mm = time % 60
            val h = (time - mm) % 24
            val dd = (time).div(24).div(60)
            return ("${dd}天${h}小时${mm}分钟")
        }
    }
}

fun Date.dateToCalendar(): Calendar {

    val cal = Calendar.getInstance()
    cal.setTime(this)
    return cal
}

/**
 * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
 */
fun String.htmlSetImgWidthAndHeight(context: Context): String? {
    return try {
        val doc: Document = Jsoup.parse(this)
        val elements: Elements = doc.getElementsByTag("img")
        for (element in elements) {
            val width = element.attr("width")
            if (!width.isNullOrEmpty()) {
                try {
                    if (width.toInt() > SizeUtils.px2dp(
                            ScreenUtils.getAppScreenWidth().toFloat()
                        )
                    ) {
                        element.attr("width", "100%").attr("height", "auto")
                    }
                } catch (e: Exception) {

                }
            }
        }
        //设置 <a> 标签不可点击
        val aElements = doc.getElementsByTag("a")
        for (a in aElements) {
            a.attr("style", "pointer-events: none;")
        }
        "处理后的Html-》》》${doc.toString()}".loge()
        doc.toString()

    } catch (e: Exception) {
        "处理后的Html-》报错》》${e}".loge()
        this
    }
}

/**
 * 修改webview样式(assets--css文件)
 *
 * @param webview
 * @param str
 * @param css file:///android_asset/img.css
 */
fun webLoadCss(webview: WebView, str: String, css: String) {
    val linkCss =
        """<link rel="stylesheet" href="file:///android_asset/$css" type="text/css">"""
    val body = ("<html><header>" + linkCss + "</header>" + str
            + "</body></html>")
    webview.loadDataWithBaseURL(linkCss, body, "text/html", "UTF-8", null)
}

/**
 * 修改webview样式(assets--css文件)
 *
 * @param webview
 * @param str
 * @param css file:///android_asset/img.css
 */
fun webLoadCss(webview: com.tencent.smtt.sdk.WebView, str: String, css: String) {
    val linkCss =
        """<link rel="stylesheet" href="file:///android_asset/$css" type="text/css">"""
    val body = ("<html><header>" + linkCss + "</header>" + str
            + "</body></html>")
    webview.loadDataWithBaseURL(linkCss, body, "text/html", "UTF-8", null)
}

fun String.urlGetBitmap(context: Context, callBack: (w: Int, h: Int) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(this)
        .into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {

                "${resource.width}=bitmap高=${resource.height}".loge()
                callBack.invoke(resource.width, resource.height)

            }
        })
}

fun String.urlGetBitmapWidth(context: Context): Int {
    var w = 0
    Glide.with(context)
        .asBitmap()
        .load(this)
        .into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {

                "${resource.width}=bitmap高=${resource.height}".loge()
                w = resource.width

            }
        })

    return w
}


private val srcList = ArrayList<String>()
fun String.htmlGetImgSrc(): ArrayList<String> {
    srcList.clear()
    return try {
        val doc: Document = Jsoup.parse(this)
        val elements: Elements = doc.getElementsByTag("img")
        for (element in elements) {
            val elementsByAttribute = element.attr("src")
            if (elementsByAttribute.isNotEmpty())
                srcList.add(elementsByAttribute)
        }
        srcList
    } catch (e: Exception) {
        srcList
    }
}

fun WebView.getSetting() {
    this.settings?.let {
        it.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        it.javaScriptEnabled = true;//支持js
        it.builtInZoomControls = false;
        it.setSupportZoom(false);
        it.displayZoomControls = false;
//        it.useWideViewPort = true
//        it.loadWithOverviewMode = true
//        it.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
    }

}

fun WebView.imgReset() {
    this.loadUrl(
        """javascript:(function(){
        |var objs = document.getElementsByTagName('img');
        | for(var i=0;i<objs.length;i++){
        | var img = objs[i];
        |  img.style.maxWidth = '100%';
        |   img.style.height = 'auto'; }
        |   })()""".trimMargin()
    )

}

fun com.tencent.smtt.sdk.WebView.getSetting() = this.settings?.let {
    it.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
    it.setJavaScriptEnabled(true);//支持js
    it.builtInZoomControls = false;
    it.setSupportZoom(false);
    it.displayZoomControls = false;
//    it.useWideViewPort = true
//    it.loadWithOverviewMode = true
//    it.layoutAlgorithm = com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS
}


/**
 * 长按图片事件处理
 * 返回true 拦截长按事件 包括复制
 */
fun FragmentActivity.handleLongImage(
    webview: WebView,
    picUrl: java.util.ArrayList<String>
): Boolean {
    val hitTestResult: android.webkit.WebView.HitTestResult =
        webview.hitTestResult
    // 如果是图片类型或者是带有图片链接的类型
    if (hitTestResult.getType() === WebView.HitTestResult.IMAGE_TYPE ||
        hitTestResult.getType() === WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
    ) {
        var pos = 0
        for (i in picUrl.indices) {
            if (picUrl[i] == hitTestResult.extra) {
                pos = i
            }
        }

        SeeMultiPicture.load(picUrl)
            .selection(pos)//当前选中位置
            .indicator(true)//是否显示指示器，默认不显示
            .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
            .start(this, null);


        return true
    }
    return true
}

/**
 * 长按图片事件处理
 * 返回true 拦截长按事件 包括复制
 */
fun FragmentActivity.handleLongImage(
    webview: com.tencent.smtt.sdk.WebView,
    picUrl: java.util.ArrayList<String>
): Boolean {
    val hitTestResult: com.tencent.smtt.sdk.WebView.HitTestResult =
        webview.hitTestResult
    // 如果是图片类型或者是带有图片链接的类型
    if (hitTestResult.getType() === WebView.HitTestResult.IMAGE_TYPE ||
        hitTestResult.getType() === WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
    ) {
        var pos = 0
        for (i in picUrl.indices) {
            if (picUrl[i] == hitTestResult.extra) {
                pos = i
            }
        }

        SeeMultiPicture.load(picUrl)
            .selection(pos)//当前选中位置
            .indicator(true)//是否显示指示器，默认不显示
            .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
            .start(this, null);


        return true
    }
    return true
}

fun FragmentActivity.getSexList() = arrayListOf("男", "女")

/**
 * 虎丘关系
 * @return ArrayList<String>
 */
fun getRelation() = arrayListOf("本人", "子女", "父母", "岳父母", "媳婿", "孙子女", "兄弟姐妹", "祖父母", "租赁", "其他")


