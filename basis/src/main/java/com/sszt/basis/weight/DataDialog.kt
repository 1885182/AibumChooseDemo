package com.sszt.basis.weight

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.listener.OnItemSelectedListener
import com.contrarywind.view.WheelView
import com.sszt.basis.R
import com.sszt.basis.base.dialog.WindowParam
import com.sszt.basis.ext.toast


/**
 * <pre>
 *  @author: Meng
 *  @time： 2020/11/10
 *  @dec：   底部弹窗dialog
 *
 * <pre>
 */
@WindowParam(
    gravity = Gravity.BOTTOM,
    noAnim = true,
    dimAmount = 0.25f,
    outSideCanceled = true
)
class DataDialog(
    val context: Activity,
    val mOptionsItems: MutableList<String>,
    val callBack: (position: Int) -> Unit
) : DialogFragment() {
    private var position = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_data_layout, null)
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
//            if (!noAnim) {
            setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
//            } else {
//                setWindowAnimations(animRes)
//            }
            if (dimAmount != -1f) {
                setDimAmount(dimAmount)
            }

        }

        view.let {
            if (mOptionsItems.isNullOrEmpty()) {
                toast("暂无数据")
                this.dismiss()
                return@let
            }

            //
            it.findViewById<TextView>(R.id.dialogDataConfim).setOnClickListener {
                callBack.invoke(position)
                this.dismiss()
            }
            it.findViewById<TextView>(R.id.dialogDataCancel).setOnClickListener {

                this.dismiss()
            }
            it.findViewById<WheelView>(R.id.dialogDataWheel).let { it1 ->
                it1.setTextSize(16f)
                it1.setDividerType(WheelView.DividerType.FILL)
                it1.setDividerColor(ContextCompat.getColor(context, R.color.color_e4e2e2))
                it1.setTextColorCenter(ContextCompat.getColor(context, R.color.color_333333))
                it1.setTextColorOut(ContextCompat.getColor(context, R.color.color_999999))
                it1.setLineSpacingMultiplier(3f)
                it1.setCyclic(false)
                if (mOptionsItems.size > 0) {
                    it1.adapter = ArrayWheelAdapter(mOptionsItems)
                    it1.setOnItemSelectedListener(OnItemSelectedListener { index ->
                        position = index

                    })
                    it1.currentItem = 0
                }

            }

        }

        return dialog
    }
}