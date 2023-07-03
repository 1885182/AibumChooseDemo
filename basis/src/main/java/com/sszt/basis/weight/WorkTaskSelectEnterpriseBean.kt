package com.sszt.basis.weight

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/12/30 16:51<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */


data class WorkTaskSelectEnterpriseBean(
    val countId: String?,
    val current: Int?,
    val hitCount: Boolean?,
    val maxLimit: String?,
    val optimizeCountSql: Boolean?,
    val orders: List<String>?,
    val pages: Int?,
    val records: ArrayList<WorkTaskSelectEnterpriseItemBean>?,
    val searchCount: Boolean?,
    val size: Int?,
    val total: Int?
)

@Parcelize
data class WorkTaskSelectEnterpriseItemBean(
    val address: String?,
    val approtime: String?,
    val certList: String?,
    val createTime: String?,
    val createUserUuid: String?,
    val degres: String?,
    val degresShow: String?,
    val emplList: String?,
    val enterName: String?,
    val ename: String?,
    val enterStatus: Int?,
    val enterType: String?,
    val enterTypeShow: String?,
    val expbookList: String?,
    val goveName: String?,
    val govePhone: String?,
    val gridEnterprise: String?,
    val gridHouseUuid: String?,
    val haveparty: String?,
    val havepartyShow: String?,
    val id: Int?,
    val incbookList: String?,
    val labour: String?,
    val labourCount: String?,
    val labourShow: String?,
    val lat: String?,
    val legidcard: String?,
    val legname: String?,
    val logoName: String?,
    val logoPath: String?,
    val lon: String?,
    val overseas: String?,
    val overseasShow: String?,
    val partycount: String?,
    val partyorg: String?,
    val partyorgShow: String?,
    val perCertCode: String?,
    val perCertType: String?,
    val perCertTypeShow: String?,
    val perName: String?,
    val perPhone: String?,
    val regauth: String?,
    val socialcode: String?,
    val source: String?,
    val updateTime: String?,
    val updateUserUuid: String?,
    val uuid: String?,
    val womens: String?,
    val womensCount: String?,
    val womensShow: String?,
    val yeartagList: String?,
    val youth: String?,
    val youthCount: String?,
    val youthShow: String?,
    var select:Boolean=false
):Parcelable