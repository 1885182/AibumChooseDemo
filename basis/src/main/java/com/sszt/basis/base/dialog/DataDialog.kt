package com.sszt.basis.base.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sszt.basis.R
import com.sszt.basis.base.appContext
import com.sszt.basis.ext.init
import com.sszt.basis.weight.DialogBottomDataAdapter
import java.util.*

@WindowParam(
    gravity = Gravity.BOTTOM,
    noAnim = false,
    dimAmount = 0f,
    outSideCanceled = true
)
class DataDialog(
    val list: ArrayList<String>, val callBack: (position: Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_time_layout, null)
        val param = javaClass.getAnnotation(WindowParam::class.java)!!
        val gravity = param.gravity
        val outSideCanceled = param.outSideCanceled
        val canceled = param.canceled
        val dimAmount = param.dimAmount
        val noAnim = param.noAnim
        val animRes = param.animRes

        val dialog = Dialog(appContext)
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
            if (noAnim) {
                setWindowAnimations(R.style.DialogBottomAnimator)
            } else {
                setWindowAnimations(animRes)
            }
            if (dimAmount != -1f) {
                setDimAmount(dimAmount)
            }

        }

        view.apply {
            val adpBottomData = DialogBottomDataAdapter(list)

            findViewById<RecyclerView>(R.id.dialogDataRV).init(
                LinearLayoutManager(context),
                adpBottomData
            )
            adpBottomData.setOnItemClickListener { _, _, position ->
                callBack.invoke(position)
                dialog?.dismiss()
            }
        }




        return dialog
    }
}