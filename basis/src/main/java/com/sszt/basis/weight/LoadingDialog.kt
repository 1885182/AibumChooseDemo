package com.sszt.basis.weight

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sszt.basis.R
import java.lang.reflect.Field


/**
 *
 * @param time 2021/8/19
 * @param des 加载框
 * @param author Meng
 *
 */
class LoadingDialog(
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = View.inflate(activity, R.layout.layout_custom_progress_dialog_view, null)
        val gravity = Gravity.CENTER
        val outSideCanceled = false
        val canceled = false
        val dimAmount = -1F
        val noAnim = true
//        val animRes = R.style.DialogCenterAnimator

        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(view)
        dialog?.setCanceledOnTouchOutside(outSideCanceled)
        dialog?.setCancelable(canceled)

        val window = dialog?.window
        val dm = DisplayMetrics()
        window?.apply {
            windowManager.defaultDisplay.getMetrics(dm)
//            setLayout(dm.widthPixels, window.attributes.height)
            setBackgroundDrawable(ColorDrawable(0x00000000))
            setGravity(gravity)
//            if (!noAnim) {
//                setWindowAnimations(R.style.MD_WindowAnimation)
//            } else {
//                setWindowAnimations(R.style.DialogCenterAnimator)
//            }
            setDimAmount(dimAmount)

        }

        return dialog!!
    }

    override fun onDestroy() {
        super.onDestroy()
        this.dismiss()
    }


    fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.setAccessible(true)
            dismissed.set(this, false)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        try {
            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.setAccessible(true)
            shown.set(this, true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}