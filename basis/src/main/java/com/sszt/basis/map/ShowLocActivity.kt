package com.sszt.basis.map

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.sszt.basis.R
import com.sszt.basis.map.util.BaiduMapUtil
import com.sszt.basis.util.StatusBar
import com.sszt.basis.weight.TitleLayout
import com.sszt.resources.IRoute


@Route(path = IRoute.map_show_loc)
class ShowLocActivity : AppCompatActivity() {

    @JvmField
    @Autowired
    var lat = ""

    @JvmField
    @Autowired
    var lng = ""

    private var mBaiduMap: BaiduMap? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_show_loc)
        ARouter.getInstance().inject(this)


        //StatusBar.fullScreen(this)

        findViewById<TitleLayout>(R.id.showLocTl).setOnClickListener { finish() }



        mBaiduMap = BaiduMapUtil.initMap(findViewById(R.id.mapShowLoc), false, false)
        //mBaiduMap?.let { BaiduMapUtil.setMapStatus(lat.toDouble(),lng.toDouble(), it) }




        BaiduMapUtil.addMarketAndCenter(
            LatLng(lat.toDouble(),lng.toDouble()),
            R.drawable.loc_red,
            mBaiduMap!!
        )

    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        findViewById<MapView>(R.id.mapShowLoc).onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        findViewById<MapView>(R.id.mapShowLoc).onPause()
    }


    override fun onDestroy() {
        findViewById<MapView>(R.id.mapShowLoc).onDestroy()
        super.onDestroy()

    }
}