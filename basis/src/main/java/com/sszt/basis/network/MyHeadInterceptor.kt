package com.sszt.basis.network

import com.blankj.utilcode.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("token", SPUtils.getInstance().getString("token")?:"").build()
        builder.addHeader("User-Agent", "android").build()
        return chain.proceed(builder.build())
    }

}