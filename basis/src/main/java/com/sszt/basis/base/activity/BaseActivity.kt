package com.sszt.basis.base.activity

import android.os.Bundle
import android.view.MotionEvent
import androidx.viewbinding.ViewBinding
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.ext.dismissLoadingEx
import com.sszt.basis.weight.LoadingDialog

/**
 * 时间　: 2019/12/21
 * 作者　: hegaojian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : BaseVmDbActivity<VM, DB>() {

    private val loadingDailog by lazy { LoadingDialog() }
    val TAG = "sszt_log"


    abstract
    /**
     * <b>标题：</b> 获取布局 <br>
     * <b>描述：</b> <br>
     * <b>创建：</b>2021/11/5 19:09<br>
     * <b>更新：</b>时间： 更新人： 更新内容：
     * @param
     * @return
     * @author mengwenyue
     * @version V1.0.0
     */
    override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    /** <b>标题：</b> 接收网络请求后的数据 <br>     * <b>描述：</b> <br>
     * <b>创建：</b>2021/11/5 19:09<br>
     * <b>更新：</b>时间： 更新人： 更新内容：
     * @param
     * @return
     * @author mengwenyue
     * @version V1.0.0
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
//        Loading.showLoading(this)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {

        this.dismissLoadingEx("")

    }

    private val MIN_DELAY_TIME = 500 // 两次点击间隔不能少于500ms

    private var lastClickTime: Long = 0

    /**
     *设置是否启用重复点击
     */
    var setOnceClick = false

    open fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!setOnceClick) return super.dispatchTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 判断连续点击事件时间差
            if (isFastClick()) {
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}