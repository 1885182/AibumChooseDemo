package com.sszt.basis.util

import androidx.lifecycle.ViewModel
import com.sszt.basis.callback.livedata.StringLiveData

class PublicValueModel:ViewModel() {
    val keyWord=StringLiveData()
    val grid=StringLiveData()
}