package com.sszt.basis.util

import com.blankj.utilcode.util.RegexUtils

object Utils {

    fun isTelOrPhone(string: String): Boolean {

        if (RegexUtils.isTel(string)) return true
        if (RegexUtils.isMobileSimple(string)) return true

        return false

    }

    fun isPhone(string: String): Boolean {

        if (RegexUtils.isMobileSimple(string)) return true

        return false

    }



    fun isIDCard(string: String): Boolean {

        if (RegexUtils.isIDCard15(string)) return true
        if (RegexUtils.isIDCard18Exact(string)) return true

        return false

    }

}