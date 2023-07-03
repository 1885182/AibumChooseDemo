package com.sszt.basis.map

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.search.poi.PoiSearch
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.sszt.basis.R
import com.sszt.basis.map.adapter.MapSelectAdapter
import com.sszt.basis.map.bean.PoiBean
import com.sszt.basis.map.util.BaiduMapUtil
import com.sszt.basis.ext.init
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.util.StatusBar
import com.sszt.resources.IRoute
import java.util.*
import kotlin.collections.ArrayList



/**
 *
 * @param time 2021/6/30
 * @param des 选择位置
 * @param author Meng
 *
 */
@Route(path = IRoute.map_select)
class MapSelectActivity : AppCompatActivity() {

    @JvmField
    @Autowired
    var lat: String? = null

    @JvmField
    @Autowired
    var lng: String? = null

    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var poiList = ArrayList<PoiBean>()
    private var poiListE = MutableLiveData< ArrayList<PoiBean>>()

    private var mPoiSearch: PoiSearch? = null
    private var selectPoi: PoiBean? = null
    private val mMapAdapter by lazy { MapSelectAdapter(arrayListOf()) }

    private val mapSelectMap: MapView
        get() {
            var mapSelectMap = findViewById<MapView>(R.id.mapSelectMap)
            return mapSelectMap
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_select)

        ARouter.getInstance().inject(this)
        val params = getWindow().getAttributes();
        params.height = ScreenUtils.getScreenHeight().minus(BarUtils.getStatusBarHeight())
            .minus(SizeUtils.dp2px(45f)) // 高度设置为屏幕的0.3
        params.width = ScreenUtils.getScreenWidth()
        params.gravity = Gravity.BOTTOM
        params.dimAmount = 0.25f
        getWindow().attributes = params;

        StatusBar.fullScreen(this)



        mBaiduMap = BaiduMapUtil.initMap(mapSelectMap, false, true)

        mBaiduMap?.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(p0: MapStatus?) {

            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {

            }

            override fun onMapStatusChange(p0: MapStatus?) {

            }

            override fun onMapStatusChangeFinish(p0: MapStatus?) {
                p0?.let {
                    mPoiSearch = BaiduMapUtil.poiSearchNearby(
                        p0.target.latitude,
                        it.target.longitude,
                        "建筑$‘住宅$‘银行$’酒店"
                    ) {
                        if (it.isNullOrEmpty()) return@poiSearchNearby
                        poiList.clear()
                        it.forEach {
                            poiList.add(
                                PoiBean(
                                    title = it.name,
                                    distant = it.distance,
                                    address = it.address,
                                    latLng = it.location
                                )
                            )
                        }
                        mMapAdapter.setList(poiList)

                    }
                }

            }
        })

        findViewById<RecyclerView>(R.id.mapSelectRV).init(LinearLayoutManager(this), mMapAdapter)
        mMapAdapter.setOnItemClickListener { _, _, position ->

            mMapAdapter.data.forEach {
                it.isSelect = false
            }
            mMapAdapter.data[position].isSelect = true
            mMapAdapter.notifyDataSetChanged()
            selectPoi = mMapAdapter.data[position]


        }

        findViewById<EditText>(R.id.gaoMapSelectSearch).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mPoiSearch = BaiduMapUtil.poiSearchNearby(
                    BaiduMapUtil.mCurrentLat.value?.toDouble() ?: 0.0,
                    BaiduMapUtil.mCurrentLon,
                    findViewById<EditText>(R.id.gaoMapSelectSearch).textStringTrim(),
                    100000
                ) {
                    if (it.isNullOrEmpty()) return@poiSearchNearby
                    poiList.clear()

                    it.forEach {
                        poiList.add(
                            PoiBean(
                                title = it.name,
                                distant = it.distance,
                                address = it.address,
                                latLng = it.location
                            )
                        )
                    }
                    mMapAdapter.setList(poiList)
                }
            }
        })

        findViewById<TextView>(R.id.gaoMapSelectOk).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("address", selectPoi?.address).putExtra("lat",selectPoi?.latLng?.latitude.toString()).putExtra("lng",selectPoi?.latLng?.longitude.toString()))
            finish()
        }

        if (lat?.isNullOrBlank() == false && lng?.isNullOrBlank() == false) {
            //然后可以移动到定位点,使用animateCamera就有动画效果
            BaiduMapUtil.setMapStatus(lat?.toDouble() ?: 0.0, lng?.toDouble() ?: 0.0, mBaiduMap!!)
        } else {

        }

        findViewById<ImageView>(R.id.gaoMapSelectFinish).setOnClickListener { finish() }


        mPoiSearch = BaiduMapUtil.poiSearchNearby(
            BaiduMapUtil.mCurrentLat.value ?: 0.0,
            BaiduMapUtil.mCurrentLon,
            "住宅$‘建筑$‘银行$’酒店"
        ) {
            if (it.isNullOrEmpty()) return@poiSearchNearby
            poiList.clear()
            it.forEach {
                poiList.add(
                    PoiBean(
                        title = it.name,
                        distant = it.distance,
                        address = it.address,
                        latLng = it.location
                    )
                )
            }
            poiListE.value=poiList

//            mMapAdapter.setList(poiList)
        }

        poiListE.observe(this) {
            mMapAdapter.setList(it)
        }

    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapSelectMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapSelectMap.onPause()
    }


    override fun onDestroy() {
        mLocationClient?.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mapSelectMap.onDestroy()
        //停止前台定位服务：
        mLocationClient?.disableLocInForeground(true)// 关闭前台定位，同时移除通知栏
        mPoiSearch?.destroy()
        super.onDestroy()

    }
}