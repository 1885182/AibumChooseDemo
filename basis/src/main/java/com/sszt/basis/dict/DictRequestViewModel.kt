package com.sszt.basis.dict

import androidx.lifecycle.MutableLiveData
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.ext.request
import com.sszt.basis.network.apiService
import com.sszt.basis.state.ResultState
import okhttp3.RequestBody

/**
 *
 * @param time 2021/7/26
 * @param des 字典表
 * @param author Meng
 *
 */
class DictRequestViewModel : BaseViewModel() {
    val dictList = MutableLiveData<ResultState<DictResponse>>()

    fun dict(requestBody: RequestBody) {

//        request({ apiService.dict(requestBody) }, dictList)

    }


}