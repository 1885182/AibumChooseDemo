package com.sszt.basis.weight

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.sszt.basis.R
import java.lang.ref.WeakReference

object Loading {
//
//    private var loading: WeakReference<LoadingDialog>? = null
//    private var act: WeakReference<FragmentActivity>? = null

    private var loading: WeakReference<Dialog>? = null
    private var act: WeakReference<FragmentActivity>? = null


    fun showLoading(activity: FragmentActivity) {
        if (activity == act?.get()) return
        act = WeakReference(activity)
        loading = WeakReference(Dialog(activity))
        loading?.get()?.let { dialog ->
            val v =
                LayoutInflater.from(activity)
                    .inflate(R.layout.layout_custom_progress_dialog_view, null)

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


            dialog.setOnCancelListener {
                loading?.get()?.dismiss()
                loading?.clear()
                loading = null
                act?.clear()
            }

        }
        loading?.get()?.show()



    }

    fun dismissLoading() {
        if (loading?.get()?.isShowing == true) {
            loading?.get()?.dismiss()
            loading?.clear()
            loading = null
            act?.clear()


        }
    }


}