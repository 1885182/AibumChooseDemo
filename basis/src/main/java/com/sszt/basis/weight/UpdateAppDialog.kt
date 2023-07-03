package com.sszt.basis.weight

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.sszt.basis.R
import com.sszt.basis.base.dialog.WindowParam
import java.util.*

/**
 *
 * @param time 2021/8/17
 * @param des 更新App
 * @param author Meng
 *
 */
@WindowParam(
    gravity = Gravity.CENTER,
    noAnim = true,
    dimAmount = 0.25f,
    outSideCanceled = true
)
class UpdateAppDialog(
    val context: Activity,
    val views: View
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.layout_empty_parent_view, null)
        val param = javaClass.getAnnotation(WindowParam::class.java)!!
        val gravity = param.gravity
        val outSideCanceled = param.outSideCanceled
        val canceled = param.canceled
        val dimAmount = param.dimAmount

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

            setWindowAnimations(R.style.DialogCenterAnimator)

            if (dimAmount != -1f) {
                setDimAmount(dimAmount)
            }

        }

        view.apply {

            findViewById<FrameLayout>(R.id.emptyParentFL).let {
                it.removeView(views)
                it.addView(views)
            }

        }




        return dialog
    }
}