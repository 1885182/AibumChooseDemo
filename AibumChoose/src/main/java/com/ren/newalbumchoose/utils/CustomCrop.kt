package com.ren.newalbumchoose.utils

import android.app.Activity
import android.content.Intent
import com.ren.newalbumchoose.activity.CropActivity

/**
 *
 * @param time 2021/9/7
 * @param des 裁剪图片
 * @param author Meng
 *
 * @param imagePath 原始图片地址
 * @param width 裁剪宽度
 * @param height 裁剪高度
 */
data class CustomCrop(val imagePath: String, val width: Int?, val height: Int?) {

    companion object {
        val CROP_PATH = "imagePath"  //Intent传值的key
        val CROP_WIDTH = "cropWidth" //Intent传值的key
        val CROP_HEIGHT = "cropHeight" //Intent传值的key
        val CUSTOM_CROP_CODE = 300  //startActivityForResult
    }

    fun start(activity: Activity) {
        val intent = Intent(activity, CropActivity::class.java)
        intent.putExtra(CROP_WIDTH, width)
        intent.putExtra(CROP_HEIGHT, height)
        intent.putExtra(CROP_PATH, imagePath)
        activity.startActivity(intent)
    }

    fun startResult(activity: Activity) {
        val intent = Intent(activity, CropActivity::class.java)
        intent.putExtra(CROP_WIDTH, width)
        intent.putExtra(CROP_HEIGHT, height)
        intent.putExtra(CROP_PATH, imagePath)
        activity.startActivityForResult(intent, CUSTOM_CROP_CODE)
    }


}