package com.sszt.basis.network

/**
 *
 * 描述　: 错误枚举类
 */
enum class Error(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "请求失败，请稍后再试"),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误，请稍后再试"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, "网络连接错误，请稍后重试"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错，请稍后再试"),

    /**
     * Token 过期重新登陆
     */
    TOKEN_ERROR(9000, "登录身份过期，请重新登陆"),

    /**
     * 服务调用不上
     */
    SERVICE_ERROR(503, "服务正在链接中，请稍后重试"),

    /**
     * 权限不足
     */
    JURISDICTION_ERROR(403, "没有查询数据的权限"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "网络连接超时，请稍后重试");


    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}