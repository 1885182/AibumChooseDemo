package com.sszt.basis.network

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2022/2/11 15:00<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */


data class SysDictBean(
    val description: String?,
    val gmtCreate: String?,
    val gmtModified: String?,
    val id: Int?,
    val imgUrl: Any?,
    val leafNode: Any?,
    val levelNum: Int?,
    val lft: Int?,
    val parentUuid: Any?,
    val rgt: Int?,
    val value: String?,
    val text: String?,
    val uuid: String?,
    val sym: String?
)