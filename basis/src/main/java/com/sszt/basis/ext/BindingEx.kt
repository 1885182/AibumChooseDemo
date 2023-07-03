package com.sszt.basis.ext

import android.app.Activity
import android.content.ContextWrapper
import android.os.SystemClock
import android.view.View
 import com.sszt.basis.R



fun View.determineTriggerSingleClick(
    interval: Int = SingleClickUtil.singleClickInterval,
    isShareSingleClick: Boolean = true,
    listener: View.OnClickListener
) {
    val target = if (isShareSingleClick) getActivity(this)?.window?.decorView ?: this else this
    val millis = target.getTag(R.id.single_click_tag_last_single_click_millis) as? Long ?: 0
    if (SystemClock.uptimeMillis() - millis >= interval) {
        target.setTag(R.id.single_click_tag_last_single_click_millis, SystemClock.uptimeMillis())
        listener.onClick(this)
    }
}

fun getActivity(view: View): Activity? {
    var context = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
