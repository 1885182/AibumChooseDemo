package com.sszt.basis.weight

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/12/29 9:08<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */


data class SelectPersonMainBean(
    val gridRegisterUuid: String?,
    val id: Int?,
    val idcard: String?,
    val popuName: String?,
    val gridItemName: String?,
    var gridHouseUuid: String?,
    var gridHouseName: String?,
    val natplace: String?,
    val phone: String?,
    val gridItemUuid: String?,
    val personName: String?,
    val personUuid: String?,
    val birthday: String?,
    val occutypeShow: String?,
    val occutype: String?,
    val mobile: String?,
    val headPicPath: String?,
    val settled: Int?,
    val sex: String?,
    val sexShow: String?,
    val uuid: String?,
    var select:Boolean=false
)