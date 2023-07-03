package com.sszt.basis.map.util

import java.util.*

/**
 *
 * @param time 2021/6/22
 * @param des 地图更新经纬度
 * @param author Meng
 *
 */
object ObservableMap : Observable() {
    fun updateValue(data: Any?) {
        synchronized(this) {
            setChanged()
            notifyObservers(data)
        }
    }


}