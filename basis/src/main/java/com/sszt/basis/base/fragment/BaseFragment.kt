package com.sszt.basis.base.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.ext.dismissLoadingEx
import com.sszt.basis.weight.LoadingDialog

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　: 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding> : BaseVmDbFragment<VM, DB>() {

    private val loadingDailog by lazy { LoadingDialog() }

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract     /**
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
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    override fun lazyLoadData() {}

    /**
     * 创建LiveData观察者 Fragment执行onViewCreated后触发
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
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {

    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {

        this.requireActivity().dismissLoadingEx("")
    }

    override fun onPause() {
        super.onPause()

    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    override fun lazyLoadTime(): Long {
        return 300
    }
}