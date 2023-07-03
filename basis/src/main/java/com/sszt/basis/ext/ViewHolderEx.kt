package com.sszt.basis.ext

import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.ext.view.gone
import com.sszt.basis.ext.view.visible

/**
 * @param value 设置的内容为空或者null 隐藏控件  有值 显示控件 并赋值
 */
fun BaseViewHolder.setTextV(@IdRes viewId: Int, value: CharSequence?) {
    this.getView<TextView>(viewId).let {
        if (value.isNullOrEmpty()) {
            it.gone()
        } else {
            it.visible()
            it.setText(value)
        }
    }
}