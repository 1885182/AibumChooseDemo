package com.sszt.basis.ext

object SingleClickUtil {
    var singleClickInterval: Int = 1000
        set(value) {
            if (value <= 0) {
                throw IllegalArgumentException("Single click interval must be greater than 0.")
            }
            field = value
        }
}