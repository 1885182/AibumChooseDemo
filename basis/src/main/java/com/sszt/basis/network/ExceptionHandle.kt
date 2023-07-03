package com.sszt.basis.network

import android.net.ParseException
import android.util.Log
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *
 * 描述　: 根据异常返回相关的错误信息工具类
 */
object ExceptionHandle {

    /** todo 打包时关闭
     * log开关
     */
    var isLog = true

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    return when {
                        it.code() == 503 -> AppException(Error.SERVICE_ERROR, e)
                        it.code() == 403 -> AppException(Error.JURISDICTION_ERROR, e)
                        it.code() == 9000 -> AppException(Error.TOKEN_ERROR, e)
                        else -> {
                            AppException(Error.NETWORK_ERROR, e)
                        }
                    }
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR, e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR, e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }
                is AppException -> {
                    return if (it.errCode == 9000) {
                        ex = AppException(Error.TOKEN_ERROR, e)
                        ex
                    } else {
                        it
                    }
                }

                else -> {
                    ex = AppException(Error.UNKNOWN, e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN, e)
        return ex
    }
}