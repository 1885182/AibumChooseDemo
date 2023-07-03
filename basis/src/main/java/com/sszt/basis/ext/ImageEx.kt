package com.sszt.basis.ext

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ren.newalbumchoose.weight.SeeMultiPicture
import com.sszt.basis.R

//fun ImageView.load(url: Any) {
//    Glide.with(context).load(url).into(this)
//}

fun ImageView.load(
    url: Any,
    placeholderId: Int = R.mipmap.pic_load_loading,
    errorId: Int = R.mipmap.pic_load_loading
) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions().error(errorId).placeholder(placeholderId))
        .into(this)
}

fun FragmentActivity.isHttp(str: String, str1: String): String {
    if (str1.length > 4 && str1.substring(0, 4) == "http") {
        return str1
    }
    if (str.length > 4 && str.substring(0, 4) == "http") {

        return str
    }
    return str1
}


/**
 * 查看大图
 */
fun View.lookBigPicture(activity: Activity, url: String) {
      if (url.isEmpty()) {
          activity.toast("图片解析错误")
        return
    }
    SeeMultiPicture.load(url)
        .start(activity, this);
}

/**
 * 查看大图
 */
fun Activity.lookBigPicture(url: String) {
    if (url.isEmpty()) {
        toast("图片解析错误")
        return
    }
    SeeMultiPicture.load(url)
        .start(this);
}

/**
 * 查看大图
 */
fun View.lookBigPicture(fragment: Fragment, url: String) {
      if (url.isEmpty()) {
          fragment.toast("图片解析错误")
        return
    }
    SeeMultiPicture.load(url)
        .start(fragment, this);
}

/**
 * 查看大图
 */
fun View.lookBigPicture(activity: Activity, url: List<Any>, position: Int) {
      if (url.isEmpty()) {
          activity.toast("图片解析错误")
        return
    }
    SeeMultiPicture.load(url)
        .selection(position)//当前选中位置
        .start(activity, this);
}

/**
 * 查看大图
 */
fun View.lookBigPicture(fragment: Fragment, url: List<String>, position: Int) {
      if (url.isEmpty()) {
          fragment.toast("图片解析错误")
        return
    }
    SeeMultiPicture.load(url)
        .selection(position)//当前选中位置
        .start(fragment, this);
}
