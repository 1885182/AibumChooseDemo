package com.sszt.basis.base.dialog

import android.view.Gravity

/**
 *
 * @param time 2021/7/8
 * @param des Dialog注解类
 * @param author Meng
 *
 */
@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class WindowParam(
    // TODO gravity 是dialog中的Window的 setGravity(gravity)方法
    val gravity: Int = Gravity.CENTER,
    // TODO outSideCanceled 是 dialog.setCanceledOnTouchOutside(outSideCanceled) 点击外部是否可以取消
    val outSideCanceled: Boolean = true,
    // TODO noAnim 是指是否使用进场出场动画
    val noAnim: Boolean = false,
    // TODO animRes是我们的进场出场动画资源
    val animRes: Int = -1,
    // TODO canceled 是 dialog.setCancelable(canceled)
    val canceled: Boolean = true,
    // TODO dimAmount 是window的setDimAmount(dimAmount) 用于控制弹窗后灰色蒙版的透明度
    val dimAmount: Float = -1f
)
