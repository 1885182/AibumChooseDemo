package com.sszt.basis.network

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/12/28 14:49<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
data class GetUserUuidBean(
    val deptName: String="",
    val deptUuid: String="",
    val description: Any="",
    val gmtCreate: Any="",
    val gmtModified: Any="",
    val gridUuid: String="",
    val id: Any="",
    val personName: String="",
    val personUuid: String="",
    val mobile: String="",
    val roleName: Any="",
    val roleSym: Any="",
    val stts: Any="",
    val userName: String="",
    val userPwd: Any="",
    val uuid: String=""
)