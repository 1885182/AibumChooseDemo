package com.sszt.basis.network.interceptor

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.sszt.basis.bean.PublicAnyBean
import com.sszt.basis.ext.util.loge
import com.sszt.basis.network.NETWORKSUCCESSCODE
import okhttp3.*
import org.json.JSONObject


/**
 *
 * @param time 2021/8/6
 * @param des 处理返回的数据
 * @param author Meng
 *
 */
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        var respBytes = response.body()!!.bytes()
        try {
            val respString = String(respBytes)
            val json = JSONObject(respString)
            val code = json.getInt("code")

            val msg = json.getString("msg")
            val data = json.getString("data")

            if (code != NETWORKSUCCESSCODE) {
                respBytes =
                    Gson().toJson(PublicAnyBean(code = code, msg = msg, data = null))
                        .toByteArray()
            } else if (data == """null""" || data.isNullOrEmpty() || data == "\"null\"") {
                respBytes =
                    Gson().toJson(PublicAnyBean(code = code, msg = msg, data = Any()))
                        .toByteArray()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.eTag("ResponseInterceptor", "intercept: $e")

        }

        val mediaType: MediaType? = response.body()!!.contentType()
        return response.newBuilder()
            .body(ResponseBody.create(mediaType, respBytes))
            .build()

    }
}