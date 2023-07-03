package com.sszt.basis.bean

data class PublicBean(val code: Int?, val msg: String?)

data class PublicStringBean(val code: Int, val msg: String?, val data: String?)
data class PublicAnyBean(val code: Int, val msg: String?, val data: Any?)