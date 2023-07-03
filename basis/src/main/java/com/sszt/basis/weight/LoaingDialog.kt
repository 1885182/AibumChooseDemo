package com.sszt.basis.weight

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
 import com.sszt.basis.R

class LoaingDialog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v =
            LayoutInflater.from(context).inflate(R.layout.layout_custom_progress_dialog_view, null)

        this.let { dialog ->
            dialog.setContentView(v)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(true)
            val window = dialog.window
            val dm = DisplayMetrics()
            window?.apply {
                windowManager.defaultDisplay.getMetrics(dm)
                setBackgroundDrawable(ColorDrawable(0x00000000))
                setGravity(Gravity.CENTER)

                setDimAmount(-1F)

            }
        }

    }


}