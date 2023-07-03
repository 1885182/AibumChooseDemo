package com.sszt.basis.bean

/**
 *
 * @param time 2021/7/26
 * @param des 图片adp
 * @param author Meng
 *
 * @param type 1是添加 0 是图片
 */
data class PicBean(val fileUrl: Any, var type: Int = 0, val fileName: String="")