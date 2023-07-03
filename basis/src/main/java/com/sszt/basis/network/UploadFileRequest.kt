package com.sszt.basis.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.bean.*
import com.sszt.basis.bean.command.MsgBean
import com.sszt.basis.ext.request
import com.sszt.basis.state.ResultState
import okhttp3.RequestBody

/**
 * 上传文件
 */
class UploadFileRequest : BaseViewModel() {
    val fileResult = MutableLiveData<ResultState<FileBean>>()


    fun uploadFile(requestBody: RequestBody) {
        request({ apiService.uploadFile(requestBody) }, fileResult)
    }

}