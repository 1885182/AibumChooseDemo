package com.sszt.basis.base.dialog

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.blankj.utilcode.util.KeyboardUtils
import com.contrarywind.view.WheelView
import com.hjq.toast.ToastUtils
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.sszt.basis.R

/**
 *
 * @param time 2021/7/8
 * @param des 滚轮
 * @param author Meng
 *
 */
object WheelUtil {

    /**
     * @param title 标题
     * @param defIndex 默认滚动到哪一个
     * @param mOptionsItems 数据
     * @param callBack 回调
     */
    fun showWheel(
        context: Activity,
        title: String = "请选择",
        mOptionsItems: ArrayList<String>,
        callBack: (index: Int, dialog: CustomDialog) -> Unit
    ) {
        if (mOptionsItems.isNullOrEmpty()) {
            ToastUtils.show("暂无数据")
            return
        }
        if (KeyboardUtils.isSoftInputVisible(context)) {
            KeyboardUtils.hideSoftInput(context)
        }
        val dialog =
            CustomDialog.show(object : OnBindView<CustomDialog>(R.layout.whell_dialog_layout) {
                override fun onBind(dialog: CustomDialog?, v: View?) {

                    v?.apply {
                        v.findViewById<TextView>(R.id.whellDialogTitle).setText(title)
                        v.findViewById<WheelView>(R.id.whellDialogLoop).let { it1 ->
                            it1.setTextSize(20f)
                            it1.setDividerType(WheelView.DividerType.FILL)
                            it1.setDividerColor(Color.parseColor("#7D7D7D"))
                            it1.setTextColorCenter(Color.parseColor("#52BF90"))
                            it1.setTextColorOut(Color.parseColor("#333333"))
                            it1.setCyclic(false)
                            if (mOptionsItems.size > 0) {
                                it1.adapter = ArrayWheelAdapter(mOptionsItems)
                                it1.setOnItemSelectedListener { index ->
                                    if (dialog != null) {
                                        callBack.invoke(index, dialog)
                                    }
                                }
                            }

                        }

                    }

                }
            }, CustomDialog.ALIGN.BOTTOM).setMaskColor(Color.parseColor("#40000000"))

    }

}