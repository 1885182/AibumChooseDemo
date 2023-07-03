package com.sszt.basis.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.sszt.basis.ext.view.textString

fun TextView.setColor(res: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, res))
}

fun Int.sp() = SizeUtils.sp2px(this.toFloat())
fun Int.dp() = SizeUtils.dp2px(this.toFloat())
fun Float.sp() = SizeUtils.sp2px(this)
fun Float.dp() = SizeUtils.sp2px(this)


/**
 * @param res 设置的图片
 * @param direction 0：上 1 下 2 左 3 右 4不设置
 */
fun TextView.setDrawable(res: Int, direction: Int = 2) {
    ContextCompat.getDrawable(context, res)?.let {
        it.setBounds(0, 0, it.getMinimumWidth(), it.getMinimumHeight())
        when (direction) {
            0 -> this.setCompoundDrawables(null, it, null, null)
            1 -> this.setCompoundDrawables(null, null, null, it)
            2 -> this.setCompoundDrawables(it, null, null, null)
            3 -> this.setCompoundDrawables(null, null, it, null)
            4 -> this.setCompoundDrawables(null, null, null, null)
        }
    }

}


fun EditText.addWatchListener(callBack:(str:String)->Unit) {
    this.addTextChangedListener(object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            callBack.invoke(this@addWatchListener.textString())
        }
    })

}