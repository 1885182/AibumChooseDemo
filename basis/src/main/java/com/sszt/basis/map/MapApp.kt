package com.sszt.basis.map

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.sszt.basis.BuildConfig

class MapApp:Application() {
    override fun onCreate() {
        super.onCreate()
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //定位SDK默认输出GCJ02坐标，地图SDK默认输出BD09ll坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}