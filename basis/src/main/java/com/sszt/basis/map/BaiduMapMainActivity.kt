package com.sszt.basis.map

import android.Manifest
import android.app.AlertDialog
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.map.Text
import com.baidu.mapapi.model.CoordUtil
import com.baidu.mapapi.model.LatLng
import com.blankj.utilcode.util.NotificationUtils
import com.blankj.utilcode.util.PermissionUtils
import com.permissionx.guolindev.PermissionX
import com.sszt.basis.R
import com.sszt.basis.ext.router
import com.sszt.basis.map.util.BaiduMapUtil
import com.sszt.resources.IRoute
import java.util.*
import kotlin.collections.ArrayList


class BaiduMapMainActivity : AppCompatActivity(), SensorEventListener {
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private val mLatLngList = ArrayList<LatLng>()
    private var myLocationData: MyLocationData? = null

    private val TAG = "BaiduMapMainActivity"

    private val iconMarket = arrayListOf(
        R.mipmap.icon_mark1,
        R.mipmap.icon_mark2,
        R.mipmap.icon_mark3,
        R.mipmap.icon_mark4,
        R.mipmap.icon_mark5,
        R.mipmap.icon_mark6,
        R.mipmap.icon_mark7,
        R.mipmap.icon_mark8,
        R.mipmap.icon_mark9,
        R.mipmap.icon_mark10
    )
    private var mSensorManager: SensorManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_du_map_main)

        // 获取传感器管理服务
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        // 为系统的方向传感器注册监听器
        mSensorManager?.registerListener(
            this, mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_UI
        )

        requestPermission()

        findViewById<Button>(R.id.mapSelectLoc).setOnClickListener {
            router(IRoute.map_select)
        }

        //判断是否打开通知权限
        if (!NotificationUtils.areNotificationsEnabled()) {
            AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                .setCancelable(false)
                .setPositiveButton(
                    "确定"
                ) { dialog, _ ->
                    PermissionUtils.launchAppDetailsSettings()

                }
                .setNegativeButton(
                    "取消"
                ) { dialog, _ -> }.show()
        }

        //
        mLatLngList.add(LatLng(38.049139, 114.561549))
        mLatLngList.add(LatLng(38.059139, 114.461549))
        mLatLngList.add(LatLng(38.049539, 114.511549))
        mLatLngList.add(LatLng(38.059139, 114.531549))
        mLatLngList.add(LatLng(38.012139, 114.561549))
        mLatLngList.add(LatLng(38.069139, 114.551549))

        mBaiduMap = BaiduMapUtil.initMap(findViewById(R.id.mMapView))

//        mLocationClient = BaiduMapUtil.initLocationClient(this)
        var sumM = 0.0
        var startM = LatLng(0.0, 0.0)
        mLatLngList.forEach {
            mBaiduMap?.let { it1 ->
                BaiduMapUtil.addMarket(
                    it, iconMarket[Random().nextInt(9)],
                    it1
                )
            }
            if (startM.longitude != 0.0 && startM.latitude != 0.0)
                sumM += getDistance(startM, it)
            startM = it
        }
        Log.e(TAG, "onCreate: 总距离$sumM 米")

      /*  mapGoTrajectory.setOnClickListener {

            ARouter.getInstance()
                .build(IRoute.map_trajectory)
                .withString("name", "老王546")
                .withString("routeralue", "routeraluessssssss")
                .withInt("age", 231)
                .navigation()
        }
*/
        BaiduMapUtil.initLocationClient(this)
        BaiduMapUtil.adCode.observe(this, androidx.lifecycle.Observer {

            if (it.isNullOrEmpty()) return@Observer

            BaiduMapUtil.getWeather(it) { weatherResult ->
                Log.e("天气天气天气天气","baiduMapMain")
                weatherResult.forecastHours
                if (weatherResult.status != 0) return@getWeather
                val realTimeWeather = weatherResult.realTimeWeather

                Log.e(TAG, "onCreate: 天气${weatherResult}===${realTimeWeather} ")

                findViewById<TextView>(R.id.mapTv).text =
                    "温度：${weatherResult.realTimeWeather.temperature}℃\n天气：${weatherResult.realTimeWeather.phenomenon}"

            }

        })

        BaiduMapUtil.mCurrentLat.observe(this, androidx.lifecycle.Observer {
            if (BaiduMapUtil.isChangeState) return@Observer
            mBaiduMap?.let { it1 -> BaiduMapUtil.setMapStatus(it, BaiduMapUtil.mCurrentLon, it1) }
        })


    }

    private fun getDistance(p1LL: LatLng?, p2LL: LatLng?): Double {
        return if (p1LL != null && p2LL != null) {
            val p1 = CoordUtil.ll2point(p1LL)
            val p2 = CoordUtil.ll2point(p2LL)
            if (p1 != null && p2 != null) CoordUtil.getDistance(p1, p2) else -1.0
        } else {
            -1.0
        }
    }


    /**
     * 请求权限
     */
    private fun requestPermission() {
        val permission10 = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permission10.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        PermissionX.init(this)
            .permissions(permission10)
            .request { allGranted, grantedList, deniedList ->

            }

    }


    override fun onResume() {
        findViewById<MapView>(R.id.mMapView).onResume()
        super.onResume()

    }

    override fun onPause() {
        findViewById<MapView>(R.id.mMapView).onPause()
        super.onPause()

    }

    override fun onDestroy() {
        mLocationClient?.stop()
        mBaiduMap?.isMyLocationEnabled = false
        findViewById<MapView>(R.id.mMapView).onDestroy()
        //停止前台定位服务：
        mLocationClient?.disableLocInForeground(true)// 关闭前台定位，同时移除通知栏
        //注销传感器
        mSensorManager?.unregisterListener(this)
        super.onDestroy()

    }

    //旋转的x轴位置
    private var lastX = 0f

    //方向
    private var mCurrentDirection = 0f

    private var mCurrentAccracy = 0f
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        val x = sensorEvent?.values?.get(SensorManager.DATA_X) ?: 0f
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentAccracy = sensorEvent?.accuracy?.toFloat() ?: 1f
            mCurrentDirection = x
            // 构造定位图层数据
            myLocationData = MyLocationData.Builder()
                .accuracy(mCurrentAccracy)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection)
                .latitude(BaiduMapUtil.mCurrentLat.value ?: 0.0)
                .longitude(BaiduMapUtil.mCurrentLon)
                .build()
            // 设置定位图层数据
            mBaiduMap!!.setMyLocationData(myLocationData)
        }
        lastX = x
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}