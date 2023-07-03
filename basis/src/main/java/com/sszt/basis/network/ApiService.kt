package com.sszt.basis.network

import com.sszt.basis.bean.*
import com.sszt.basis.bean.command.MsgBean
import com.sszt.basis.dict.DictResponse
import com.sszt.basis.weight.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        val SERVER_URL = Url.url
    }

    /**
     * 上传文件
     */
    @POST("sszt-cqjjqxf/base/uploder/uploadFile")
    suspend fun uploadFile(
        @Body requestBody: RequestBody
    ): ApiResponse<FileBean>


}