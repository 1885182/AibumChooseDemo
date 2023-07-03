package com.sszt.basis.util

import android.app.Activity
import android.graphics.Color
import com.gyf.immersionbar.ImmersionBar
import com.sszt.basis.R


object StatusBar {
    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    fun fullScreen(activity: Activity, isDarkFont: Boolean = false) {
        val statusBarDarkFont = ImmersionBar.with(activity)
            .transparentStatusBar()
            .statusBarDarkFont(isDarkFont, 0.4f)

        if (!ImmersionBar.isSupportStatusBarDarkFont())
            statusBarDarkFont.statusBarColor(R.color.statusBarColor)
         statusBarDarkFont.init()

    }
    /** 根据百分比改变颜色透明度  */
    fun changeAlpha(color: Int, fraction: Float): Int {
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
    }
}