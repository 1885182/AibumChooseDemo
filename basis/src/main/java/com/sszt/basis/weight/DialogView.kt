package com.sszt.basis.weight

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.sszt.basis.R
import com.sszt.basis.ext.init
import java.util.*

/**
 *
 * @param time 2021/7/13
 * @param des 确认取消对话框
 * @param author Meng
 *
 */
object DialogView {

    const val timeFormat24 = "yyyy-MM-dd HH:mm:ss"
    const val timeFormatDate = "yyyy-MM-dd"
    const val timeFormatTime24 = "HH:mm"
    const val timeFormatTime24ss = "HH:mm:ss"
    const val timeFormatTime12 = "hh:mm"

    /**
     * 展示确认取消弹框
     * @param content 提示的文字
     * @param title 标题
     * @param callBack 点击确认回调
     */
    fun showConfirmCancelDialog(content: String, title: String = "提示", callBack: () -> Unit) {
        CustomDialog.show(object : OnBindView<CustomDialog>(R.layout.dialog_confirm_cancel_layout) {
            override fun onBind(dialog: CustomDialog?, v: View?) {
                v?.let { KeyboardUtils.hideSoftInput(it) }
                v?.apply {
                    findViewById<TextView>(R.id.dialogCanfirmCancelTitle).text = title
                    findViewById<TextView>(R.id.dialogCanfirmCancelContent).text = content
                    findViewById<TextView>(R.id.dialogCanfirmCancelCancel).setOnClickListener {
                        dialog?.dismiss()
                    }
                    findViewById<TextView>(R.id.dialogConfirmCancelConfirm).setOnClickListener {
                        callBack.invoke()
                        dialog?.dismiss()
                    }


                }

            }
        }, CustomDialog.ALIGN.CENTER)
            .setMaskColor(Color.parseColor("#40000000"))
    }

    fun showConfirmCancelBtnDialog(
        content: String,
        title: String = "提示",
        confirmText: String = "确定",
        cancelText: String = "取消",
        callBack: (string: String) -> Unit
    ) {
        CustomDialog.show(object : OnBindView<CustomDialog>(R.layout.dialog_confirm_cancel_layout) {
            override fun onBind(dialog: CustomDialog?, v: View?) {
                v?.let { KeyboardUtils.hideSoftInput(it) }
                v?.apply {
                    findViewById<TextView>(R.id.dialogCanfirmCancelTitle).text = title
                    findViewById<TextView>(R.id.dialogCanfirmCancelContent).text = content
                    findViewById<TextView>(R.id.dialogCanfirmCancelCancel).apply {
                        text = cancelText
                        setOnClickListener {
                            callBack.invoke(cancelText)
                            dialog?.dismiss()
                        }
                    }
                    findViewById<TextView>(R.id.dialogConfirmCancelConfirm).apply {
                        text = confirmText
                        setOnClickListener {
                            callBack.invoke(confirmText)
                            dialog?.dismiss()
                        }
                    }


                }

            }
        }, CustomDialog.ALIGN.CENTER)
            .setMaskColor(Color.parseColor("#40000000"))
    }

    /**
     * 只有一个确定按钮
     */
    fun showConfirmNotCancelBtnDialog(
        content: String,
        title: String = "提示",
        confirmText: String = "确定",
        callBack: (string: String) -> Unit
    ) {
        CustomDialog.show(object : OnBindView<CustomDialog>(R.layout.dialog_confirm_layout) {
            override fun onBind(dialog: CustomDialog?, v: View?) {
                v?.let { KeyboardUtils.hideSoftInput(it) }
                v?.apply {
                    findViewById<TextView>(R.id.dialogCanfirmCancelTitle).text = title
                    findViewById<TextView>(R.id.dialogCanfirmCancelContent).text = content

                    findViewById<TextView>(R.id.dialogConfirmCancelConfirm).apply {
                        text = confirmText
                        setOnClickListener {
                            callBack.invoke(confirmText)
                            dialog?.dismiss()
                        }
                    }


                }

            }
        }, CustomDialog.ALIGN.CENTER)
            .setCancelable(false)
            .setMaskColor(Color.parseColor("#40000000"))
    }

    /**
     * 没有标题的确认取消弹框
     */
    fun showConfirmCancelDialogNoTit(content: String = "确定要清楚缓存吗？", callBack: () -> Unit) {
        CustomDialog.show(object :
            OnBindView<CustomDialog>(R.layout.dialog_confirm_cancel_no_title_layout) {
            override fun onBind(dialog: CustomDialog?, v: View?) {
                v?.let { KeyboardUtils.hideSoftInput(it) }
                v?.apply {
                    findViewById<TextView>(R.id.dialogNoTitleContent).text = content

                    findViewById<TextView>(R.id.dialogNoTitleCancel).setOnClickListener { dialog?.dismiss() }
                    findViewById<TextView>(R.id.dialogNoTitleOk).setOnClickListener {
                        callBack.invoke()
                        dialog?.dismiss()
                    }

                }

            }
        }, CustomDialog.ALIGN.CENTER)
            .setMaskColor(Color.parseColor("#40000000"))
    }


    fun getData(activity: Activity, list: ArrayList<String>, callBack: (position: Int) -> Unit) {

        if (KeyboardUtils.isSoftInputVisible(activity))
            KeyboardUtils.hideSoftInput(activity.window)

        CustomDialog.show(object : OnBindView<CustomDialog>(R.layout.dialog_bottom_data_layout) {
            override fun onBind(dialog: CustomDialog?, v: View?) {
                v?.apply {
                    val adpBottomData = DialogBottomDataAdapter(list)
                    KeyboardUtils.hideSoftInput(this)
                    findViewById<RecyclerView>(R.id.dialogDataRV).init(
                        LinearLayoutManager(context),
                        adpBottomData
                    )

//                     this.viewTreeObserver.addOnGlobalLayoutListener {
//                         val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//                         imm!!.hideSoftInputFromWindow(this.getWindowToken(), 0) //强制隐藏键盘
//                     }

                    adpBottomData.setOnItemClickListener { _, _, position ->
                        callBack.invoke(position)
                        dialog?.dismiss()
                    }

                }

            }
        }, CustomDialog.ALIGN.BOTTOM)
            .setMaskColor(Color.parseColor("#40000000"))

    }



}