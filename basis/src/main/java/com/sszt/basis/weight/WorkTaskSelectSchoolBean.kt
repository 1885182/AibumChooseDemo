package com.sszt.basis.weight

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class WorkTaskSelectSchoolBean(
    val code: Int,
    val `data`: List<WorkTaskSelectSchoolItemBean>,
    val msg: String
)
@Parcelize
data class WorkTaskSelectSchoolItemBean(
    val aqname: String,
    val aqqname: String,
    val aqqtel: String,
    val aqtel: String,
    val fzname: String,
    val fztel: String,
    var select: Boolean=false,
    val fzzw: String,
    val gridname: String,
    val id: Int,
    val principal: String,
    val pritel: String,
    val remark: String,
    val schaddr: String,
    val schname: String,
    val schtype: String,
    val sgrid: String,
    val uuid: String,
    val xzdept: String,
    val zaname: String,
    val zanum: Int,
    val zatel: String,
    val zxnum: Int
): Parcelable