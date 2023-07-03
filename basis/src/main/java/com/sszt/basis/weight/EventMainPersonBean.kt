package com.sszt.basis.weight

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class EventMainPersonBean(
    val total: Int,
    val list: ArrayList<EventMainPersonItemBean>,
    val isLastPage: Boolean
)

@Parcelize
data class EventMainPersonItemBean(
    val name: String?,
    val uuid: String?,
    val organizationUuid: String?,
    val mobile: String?,
    var select: Boolean = false
) : Parcelable