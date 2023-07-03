package com.sszt.basis.map.bean

import android.os.Parcelable
import com.baidu.mapapi.model.LatLng


data class PoiBean(var title: String, var address: String,val distant:Int, var latLng: LatLng,var isSelect:Boolean=false)