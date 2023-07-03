package com.sszt.basis.weight

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.sszt.basis.R
import com.sszt.basis.base.dialog.WindowParam
import com.sszt.basis.ext.dateToCalendar
import com.sszt.basis.weight.DialogView.timeFormatDate
import java.util.*

/**
 *
 * @param time 2021/8/17
 * @param des 选择时间
 * @param author Meng
 *
 *  startsDate 和 endsDate 当两个都有值是相互制约的话会导致时间选择范围越缩越小
 * @TODO startsDate and endsDate  必须其中一个有值 要是两个都有值会导致 范围越缩 越小
 * @param startsDate 开始时间
 * @param endsDate 结束时间
 * @param selectedDate 当前选中的时间
 * @param title 底部弹框的标题
 * @param booleanArray 显示年月日时分秒 booleanArrayOf(true, true, true, false, false, false)
 *
 */
@WindowParam(
    gravity = Gravity.BOTTOM,
    noAnim = true,
    dimAmount = 0.25f,
    outSideCanceled = true
)
class TimeDialog(
    val context: Activity,
    val title: String,
    val booleanArray: BooleanArray,
    var selectedDate: String = "",
    var endsDate: String = "",
    var startsDate: String = "",
    val callBack: (date: Date) -> Unit
) : DialogFragment() {
    var pvTime: TimePickerView? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_time_layout, null)
        val param = javaClass.getAnnotation(WindowParam::class.java)!!
        val gravity = param.gravity
        val outSideCanceled = param.outSideCanceled
        val canceled = param.canceled
        val dimAmount = param.dimAmount
        val noAnim = param.noAnim
        val animRes = param.animRes

        val dialog = Dialog(context)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(outSideCanceled)
        dialog.setCancelable(canceled)

        val window = dialog.window
        val dm = DisplayMetrics()
        window?.apply {
            windowManager.defaultDisplay.getMetrics(dm)
            setLayout(dm.widthPixels, window.attributes.height)
            setBackgroundDrawable(ColorDrawable(0x00000000))
            setGravity(gravity)
//            if (noAnim) {
//                setWindowAnimations(R.style.DialogBottomAnimator)
//            } else {
//                setWindowAnimations(animRes)
//            }

            setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            if (dimAmount != -1f) {
                setDimAmount(dimAmount)
            }

        }

        view.apply {


            when {
                selectedDate.isNotEmpty() -> {

                }
                startsDate.isEmpty() && endsDate.isEmpty() -> {
                    selectedDate = TimeUtils.date2String(Date())
                }

                startsDate.isNotEmpty() -> {
                    selectedDate = startsDate
                }
                endsDate.isNotEmpty() -> {
                    selectedDate = endsDate
                }
            }

            val selectedDate = TimeUtils.string2Date(selectedDate, timeFormatDate)
            val selectedCalendar = selectedDate.dateToCalendar()


            if (startsDate.isEmpty()) {
                startsDate = "1900-01-01 12:00:00"
            }

            val startDate = TimeUtils.string2Date(startsDate, timeFormatDate)
            val startCalendar = startDate.dateToCalendar()


            if (endsDate.isEmpty()) {
                endsDate = "2099-01-01 12:00:00"
            }

            val endDate = TimeUtils.string2Date(endsDate, timeFormatDate)
            val endCalendar = endDate.dateToCalendar()


            val fL = view.findViewById<FrameLayout>(R.id.dialogTimeFL)
            pvTime = TimePickerBuilder(context) { date, v -> //选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                callBack.invoke(date)
            }.setLayoutRes(
                R.layout.pickerview_custom_time
            ) { v ->
                v.findViewById<TextView>(R.id.pickerCustomTimeConfim)
                    .setOnClickListener {
                        pvTime?.returnData()
                        pvTime?.dismiss()
                        dialog?.dismiss()
                    }

                v.findViewById<TextView>(R.id.pickerCustomTimeCancel)
                    .setOnClickListener {
                        pvTime?.dismiss()
                        dialog?.dismiss()
                    }

                v.findViewById<TextView>(R.id.pickerCustomTimeTitle).text = title

            }.setType(booleanArray)
                .setLabel("年", "月", "日", "时", "分", "秒") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(ContextCompat.getColor(context, R.color.color_e4e2e2))
                .setContentTextSize(16)
                .setTextColorCenter(ContextCompat.getColor(context, R.color.color_333333))
                .setLineSpacingMultiplier(3f)
                .setOutSideCancelable(false)
                .setTextColorOut(ContextCompat.getColor(context, R.color.color_999999))
                .setDate(selectedCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setDecorView(fL) //非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .build()

            pvTime?.show()
            dialog.show()
        }




        return dialog
    }
}