package com.sszt.basis.map.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.*
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherResult
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.R
import com.sszt.basis.callback.livedata.UnPeekLiveData
import com.sszt.basis.ext.util.loge
import java.util.*


/**
 *
 * @param time 2021/6/23
 * @param des BaiduMapUtil.kt 地图工具
 * @param author Meng
 *
 */
object BaiduMapUtil {

    //城市编码
    var adCode = UnPeekLiveData<String>()

    //当前城市
    var mCity = ""

    //当前县区
    var mDistrict = ""

    var addrStr = UnPeekLiveData<String>()
    var address = UnPeekLiveData<String>()

    //当前维度
    var mCurrentLat = MutableLiveData<Double>()

    //当前精度
    var mCurrentLon = 0.0

    private val TAG = "BaiduMapUtil"

    private var mBaiduMap: BaiduMap? = null

    @SuppressLint("StaticFieldLeak")
    private var mLocationClient: LocationClient? = null

    //防止初始化第一次定位获取经纬度异常
    var isChangeState = false

    /**
     * 初始化地图
     * @param mMapView 地图控件
     * @return  BaiduMap
     */
    fun initMap(
        mMapView: MapView,
        isTrafficEnabled: Boolean = true,
        isMyLocationEnabled: Boolean = true
    ): BaiduMap? {
        mBaiduMap = mMapView.map
        mBaiduMap?.let {
            //普通地图 ,mBaiduMap是地图控制器对象
            it.mapType = BaiduMap.MAP_TYPE_NORMAL
            //开启交通图
            it.isTrafficEnabled = isTrafficEnabled
            //开启地图的定位图层
            it.isMyLocationEnabled = isMyLocationEnabled

            //
            val mLocationConfiguration = MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true,
                null
            )
            //开启定位
            it.setMyLocationConfiguration(mLocationConfiguration)

            setMapStatus(mCurrentLat.value ?: 0.0, mCurrentLon, it)
        }

        return mBaiduMap

    }

    /**
     * 构造地图数据
     */
    class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (location?.city == null) {
                return
            }
            ObservableMap.updateValue(location)
            adCode.postValue(location.adCode)

            mCity = location.city
            mDistrict = location.district
            addrStr.postValue(location.addrStr)

            address.postValue("${location.province ?: ""}${location.city ?: ""}${location.district ?: ""}${location.locationDescribe ?: ""}")

            "百度定位回调 》》》》 ${location.address}》》${location.address.country}--${location.address.province}--${location.address.city}--${location.address.district}--${location.address.street}--${location.address.town}--${location.address.address} --${location.locTypeDescription}--${location.buildingName}--${location.direction}--${location.disToRealLocation}--${location.locationWhere}--${location.poiList}--${location}".loge()

            SPUtils.getInstance().put("mAddress", location.address.province+location.address.city+location.address.district+location.address.town+location.address.street)

            if (location.latitude > 0.0 && location.longitude > 0.0) {
                mCurrentLon = location.longitude
                mCurrentLat.postValue(location.latitude)
                SPUtils.getInstance().put("mCurrentLon", location.longitude.toString())
                SPUtils.getInstance().put("mCurrentLat", location.latitude.toString())
            }



        }

    }

    /**
     * 设置缩放等级和中心位置
     * @param zoom 缩放等级
     */
    fun setMapStatus(lat: Double, lon: Double, baiduMap: BaiduMap, zoom: Float = 16f) {
        if (lat <= 0.0) return
        isChangeState = true
        //中心位置
        Log.e("测试",lat.toString()+"--setMapStatus-"+lon)
        val mMapStatus: MapStatus = MapStatus.Builder()
            .target(LatLng(lat, lon))
            .zoom(zoom)
            .build()
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        val mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
        //改变地图状态
        baiduMap.animateMapStatus(mMapStatusUpdate)

    }

    /**
     * 设置缩放等级和中心位置
     * @param zoom 缩放等级
     */
    fun setMapCenter(baiduMap: BaiduMap, lat: LatLng) {
        if (lat.latitude <= 0.0) return
        isChangeState = true
        //中心位置
        val mMapStatus: MapStatus = MapStatus.Builder()
            .target(lat)
            .build()
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        val mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
        //改变地图状态
        baiduMap.animateMapStatus(mMapStatusUpdate)

    }

    /**
     * 初始化定位
     * @param context 上线文
     * @param myLocationListener 定位监听 （返回定位信息数据）location.addrStr //获取详细地址信息  location.country //获取国家 location.adCode //获取adcode location.town //获取乡镇信息
     *                                    location.province //获取省份 location.city //获取城市  location.district //获取区县location.street //获取街道信息
     *                                    设置是否需要地址信息，默认不需要 option.setIsNeedAddress(true)
     * @return LocationClient 定位实例
     **/
    fun initLocationClient(
        context: Context
    ): LocationClient? {

        if (mLocationClient != null) {
            return mLocationClient
        }

        LocationClient.setAgreePrivacy(true)
        //定位初始化
        mLocationClient = LocationClient(context)

        mLocationClient?.let {

            //通过LocationClientOption设置LocationClient相关参数
            val option = LocationClientOption()
            // 可选，默认false，设置是否开启Gps定位
            option.isOpenGps = true
            option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            option.isLocationNotify = true
            // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            option.setCoorType("bd09ll")
            // 设置发起连续定位请求的间隔
            option.setScanSpan(5000)
            option.setNeedNewVersionRgc(true)
            // 可选，设置是否需要地址信息，默认不需要
            option.setIsNeedAddress(true)
            // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIsNeedLocationPoiList(false)
            option.setIsNeedAltitude(true)
            // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationDescribe(true)
            option.setWifiCacheTimeOut(1000)
            // 可选，设置是否需要设备方向结果
            option.setNeedDeviceDirect(true)
            //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
            option.setOpenAutoNotifyMode()

            //设置locationClientOption
            it.locOption = option
            //注册LocationListener监听器
            it.registerLocationListener(MyLocationListener())
            //开启地图定位图层
            it.start()


        }
        return mLocationClient
    }

    /**
     * 顶部通知同于后台定位
     */
    fun <T> showNotificationBar(
        context: Context,
        clazz: Class<T>
    ) {
        //大于等于Android 8.0 小于等于Android 10.0 才显示  在Android 11 上会报startService 异常错误
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
        //Android 8.0 开发须知
            NotificationBarUtil.sendNotification(
                context,
                title = "适配android 8限制后台定位功能",
                content = "正在后台定位",
                smallIcon = R.mipmap.icon_mark1,
                "location_notification",
                "location",
                mLocationClient!!,
                clazz
            )
    }

    /**
     * 添加Maker坐标点
     * @param point market 经纬度
     * @param res market 图片
     * @param mBaiduMap BaiduMap
     */
    fun addMarket(point: LatLng, res: Int, mBaiduMap: BaiduMap) {

        //构建Marker图标
        val bitmap = BitmapDescriptorFactory
            .fromResource(res)

        //构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions()
            .position(point)
            .icon(bitmap)
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option)
    }

    /**
     * 添加Maker坐标点并到中心
     * @param point market 经纬度
     * @param res market 图片
     * @param mBaiduMap BaiduMap
     */
    fun addMarketAndCenter(point: LatLng, res: Int, mBaiduMap: BaiduMap) {

        Log.e("测试","addMarketAndCenter"+point.latitude+"---"+point.longitude)
        //中心位置
        val mMapStatus: MapStatus = MapStatus.Builder()
            .target(point)
            .zoom(14f)
            .build()
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        val mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
        //改变地图状态
        mBaiduMap.animateMapStatus(mMapStatusUpdate)

        //构建Marker图标
        val bitmap = BitmapDescriptorFactory
            .fromResource(res)

        //构建MarkerOption，用于在地图上添加Marker
        val option: OverlayOptions = MarkerOptions()
            .position(point)
            .icon(bitmap)
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option)


    }

    var mPoiSearch: PoiSearch? = null
    fun poiSearchInCity(
        city: String,
        keyWord: String,
        callBack: (List<PoiInfo>) -> Unit
    ): PoiSearch {
        if (mPoiSearch == null)
            mPoiSearch = PoiSearch.newInstance()
        else
            mPoiSearch?.let {

                it.searchInCity(
                    PoiCitySearchOption()
                        .city(city) //必填
                        .cityLimit(true)
                        .keyword(keyWord) //必填
                        .pageNum(0)
                        .scope(2)

                        .pageCapacity(30)

                )

                it.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
                    override fun onGetPoiResult(p0: PoiResult?) {

                        p0?.allPoi?.size?.toString()?.loge()
                        callBack.invoke(p0?.allPoi ?: arrayListOf())
                    }

                    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                        p0?.address?.loge()
                    }

                    override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
                        p0?.poiDetailInfoList?.size?.toString()?.loge()
                    }

                    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
                        p0?.arrayPoiInfo?.size?.toString()?.loge()

                    }
                })

            }

        return mPoiSearch!!
    }

    /**
     * POI周边检索 (根据经纬度查询POI)
     * @param keyWord  支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
     * @param radius 搜索半径
     *
     */
    fun poiSearchNearby(
        lat: Double,
        lon: Double,
        keyWord: String,
        radius: Int = 1000,
        pageNum: Int = 0,
        pageCapacity: Int = 30,
        callBack: (List<PoiInfo>) -> Unit
    ): PoiSearch {
        if (mPoiSearch == null)
            mPoiSearch = PoiSearch.newInstance()
        else
            mPoiSearch?.let {
                it.searchNearby(
                    PoiNearbySearchOption().location(LatLng(lat, lon))
                        .keyword(keyWord)
                        .radius(radius)
                        .pageNum(pageNum)
                        .pageCapacity(pageCapacity)
                        .radiusLimit(true)
                        .scope(2)
                        .tag("公司,住宅")

                )

                it.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
                    override fun onGetPoiResult(p0: PoiResult?) {
                        Log.e(TAG, "onGetPoiResult: ${p0?.allPoi?.size}")
                        callBack.invoke(p0?.allPoi ?: arrayListOf())

                    }

                    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                        p0?.address?.loge()
                    }

                    override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
                        p0?.poiDetailInfoList?.size?.toString()?.loge()
                    }

                    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
                        p0?.arrayPoiInfo?.size?.toString()?.loge()
                    }
                })

            }

        return mPoiSearch!!

    }

    fun getWeather(districtID: String, callBack: (WeatherResult) -> Unit) {
        //天安门区域ID
        val weatherSearchOption = WeatherSearchOption()
            .weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_REAL_TIME)
            .districtID(districtID)
        val mWeatherSearch = WeatherSearch.newInstance()
        mWeatherSearch.setWeatherSearchResultListener {
            Log.e("天气天气天气天气","方法内")
            if (it != null)
                callBack.invoke(it)
        }

        mWeatherSearch.request(weatherSearchOption)
    }


}