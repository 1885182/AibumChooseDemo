package com.sszt.basis.dict

data class DictResponse(val dicts: ArrayList<DictResponseList>?)

data class DictResponseList(
    val sym: String?,
    val title: String?,
    val children: ArrayList<DictResponseItem>?
)

/**
 * @param type 区分是不是自定义活的卡片
 */
data class DictResponseItem(val title: String?, val value: String?, var type: String = "")