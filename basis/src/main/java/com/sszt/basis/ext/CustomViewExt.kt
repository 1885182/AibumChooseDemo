package com.sszt.basis.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sszt.basis.R
import com.sszt.basis.base.appContext
import com.sszt.basis.ext.util.toHtml
import com.sszt.basis.network.ListDataUiState
import com.sszt.basis.util.StatusBar
import com.sszt.basis.weight.TitleLayout


import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView


/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:项目中自定义类的拓展函数
 */

fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoadingEx() {
    this.showCallback(LoadingCallback::class.java)
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    return loadsir
}

//绑定普通的Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}
//绑定流失布局的Recyclerview
fun RecyclerView.initFlexbox(
    bindAdapter: RecyclerView.Adapter<*>
): RecyclerView {
    this.init(
        object : FlexboxLayoutManager(context) {
            override fun canScrollVertically() = false
        },
        bindAdapter, true
    )
    return this
}

////绑定SwipeRecyclerView
//fun SwipeRecyclerView.init(
//    layoutManger: RecyclerView.LayoutManager,
//    bindAdapter: RecyclerView.Adapter<*>,
//    isScroll: Boolean = true
//): SwipeRecyclerView {
//    layoutManager = layoutManger
//    setHasFixedSize(true)
//    adapter = bindAdapter
//    isNestedScrollingEnabled = isScroll
//    return this
//}
//
//fun SwipeRecyclerView.initFooter(loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
//    val footerView = DefineLoadMoreView(appContext)
//    //给尾部设置颜色
////    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
//    //设置尾部点击回调
//    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
//        footerView.onLoading()
//        loadmoreListener.onLoadMore()
//    })
//    this.run {
//        //添加加载更多尾部
//        addFooterView(footerView)
//        setLoadMoreView(footerView)
//        //设置加载更多回调
//        setLoadMoreListener(loadmoreListener)
//    }
//    return footerView
//}

/**
 * 根据滑动获取滑动位置
 */
private var oldScrollPosition = 0
fun RecyclerView.scrollToPosition(
    layoutManger: LinearLayoutManager,
    scrollCallBack: (position: Int) -> Unit
) {
    this.addOnScrollListener(object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val scrollPosition = if (dx > 0) {
                layoutManger.findFirstVisibleItemPosition()
            } else {
                layoutManger.findLastVisibleItemPosition()
            }
            if (oldScrollPosition == scrollPosition) return

            oldScrollPosition = scrollPosition
            scrollCallBack.invoke(oldScrollPosition)

        }
    })
}

/**
 * g根据NestedScrollView 滚动的距离给view设置 带有渐变color
 *@param view 需要设置的color的view
 * @param slidingDistance 设置滚动距离
 * @param color 设置的颜色
 *
 */
fun NestedScrollView.setOnScrollChangeToColor(
    view: View,
    slidingDistance: Float,
    color: Int = ContextCompat.getColor(this.context, R.color.title_bg),
    progress: (currentProgress: Float) -> Unit
) {

    this.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

        view.post {
            var toFloat = scrollY.div(slidingDistance)

            if (toFloat >= 1) {

                toFloat = 1f
            }

            val changeAlpha = StatusBar.changeAlpha(
                color,
                toFloat
            )

            if (view is TitleLayout)
                view.setTitBgColor(changeAlpha)
            else
                view.setBackgroundColor(
                    changeAlpha
                )
            progress.invoke(toFloat)
        }


    }

}

fun RecyclerView.scrollToPositionState(
    layoutManger: LinearLayoutManager,
    scrollCallBack: (position: Int, status: Int) -> Unit
) {
    this.addOnScrollListener(object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val scrollPosition = if (dx > 0) {
                layoutManger.findFirstVisibleItemPosition()
            } else {
                layoutManger.findLastVisibleItemPosition()
            }
            if (oldScrollPosition == scrollPosition) return

            oldScrollPosition = scrollPosition


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == 0)
                scrollCallBack.invoke(oldScrollPosition, newState)
        }
    })
}

fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
//    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}

//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //设置主题颜色
//        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}


/**
 * 初始化有返回键的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.drawable.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
//    setBackgroundColor(SettingUtil.getColor(appContext))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}


//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //等于0，关闭列表动画 否则开启
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}

fun MagicIndicator.init(
    mStringList: List<String> = arrayListOf(),
    isAdjustMode: Boolean = false,
    smode: Int = LinePagerIndicator.MODE_WRAP_CONTENT,
    setLineWidth: Int = 0,
    setLineHeight: Float = 6F,
    lineColor: Int = ContextCompat.getColor(context, R.color.title_bg),
    textNormalColor: Int = ContextCompat.getColor(context, R.color.color_999999),
    textSelectedColor: Int = ContextCompat.getColor(context, R.color.title_bg),
    lineRoundRadius: Float = SizeUtils.dp2px(6f).toFloat(),
    lineVerticalPadding: Int = SizeUtils.dp2px(5f),
    lineHorizontalPadding: Int = SizeUtils.dp2px(6f),
    isBg: Boolean = false,
    action: (index: Int) -> Unit = {}
) {
    val commonNavigator = CommonNavigator(appContext)
    commonNavigator.isAdjustMode = isAdjustMode
    commonNavigator.adapter = object : CommonNavigatorAdapter() {

        override fun getCount(): Int {
            return mStringList.size
        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return SimplePagerTitleView(appContext).apply {
                //设置文本
                text = mStringList[index].toHtml()
                //字体大小
                textSize = 14f
                //未选中颜色
                normalColor = textNormalColor
                //选中颜色
                selectedColor = textSelectedColor
                //点击事件
                setOnClickListener {
                    action.invoke(index)
                    text = mStringList[index].toHtml()
                }
            }
        }

        override fun getIndicator(context: Context): IPagerIndicator {

            if (isBg) {
                val indicator = WrapPagerIndicator(context)
                indicator.fillColor = lineColor
                indicator.roundRadius = lineRoundRadius
                indicator.verticalPadding = lineVerticalPadding
                indicator.horizontalPadding = lineHorizontalPadding
                return indicator
            }



            return LinePagerIndicator(context).apply {
                mode = smode
                //线条的宽高度
                lineHeight = SizeUtils.dp2px(setLineHeight).toFloat()
//                lineWidth = SizeUtils.dp2px(30f).toFloat()
//                if (smode == LinePagerIndicator.MODE_MATCH_EDGE) {
//                    xOffset = SizeUtils.dp2px(10f).toFloat()
//                } else
                if (smode == LinePagerIndicator.MODE_EXACTLY) {
                    lineWidth = setLineWidth.toFloat()
                }
//
                //线条的圆角
                roundRadius = lineRoundRadius
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //线条的颜色
                setColors(lineColor)
            }


        }

        override fun getTitleWeight(context: Context?, index: Int): Float {
            return 3.0f
        }
    }
    this.navigator = commonNavigator
}

fun MagicIndicator.bindViewPager2(
    appContext: Context,
    viewPager: ViewPager2,
    mStringList: List<String> = arrayListOf(),
    isAdjustMode: Boolean = false,
    smode: Int = LinePagerIndicator.MODE_WRAP_CONTENT,
    setLineWidth: Int = 0,
    setLineHeight: Float = 3F,
    action: (index: Int) -> Unit = {}
) {

    if (mStringList.isNotEmpty())
        viewPager.offscreenPageLimit = mStringList.size
    val commonNavigator = CommonNavigator(appContext)
    commonNavigator.isAdjustMode = isAdjustMode
    commonNavigator.adapter = object : CommonNavigatorAdapter() {

        override fun getCount(): Int {
            return mStringList.size
        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return SimplePagerTitleView(appContext).apply {
                //设置文本
                text = mStringList[index].toHtml()
                //字体大小
                textSize = 14f
                //未选中颜色
                normalColor = ContextCompat.getColor(context, R.color.color_999999)
                //选中颜色
                selectedColor = ContextCompat.getColor(context, R.color.title_bg)
                //点击事件
                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = smode
                //线条的宽高度
                lineHeight = SizeUtils.dp2px(setLineHeight).toFloat()
//                lineWidth = SizeUtils.dp2px(30f).toFloat()
                if (smode == LinePagerIndicator.MODE_MATCH_EDGE) {
                    xOffset = SizeUtils.dp2px(10f).toFloat()
                } else if (smode == LinePagerIndicator.MODE_EXACTLY) {
                    lineWidth = setLineWidth.toFloat()
                }
//
                //线条的圆角
                roundRadius = SizeUtils.dp2px(6f).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //线条的颜色
                setColors(ContextCompat.getColor(context, R.color.title_bg))
            }


        }

        override fun getTitleWeight(context: Context?, index: Int): Float {
            return 3.0f
        }
    }
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }
    })
}

fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}

fun ViewPager2.init(
    fragment: FragmentManager,
    activity: Lifecycle,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment, activity) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
//

/**
 * 加载列表数据
 */
fun <T> FragmentActivity.loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    smartRefreshLayout: SmartRefreshLayout
) {

    if (data.hasMore) {
        smartRefreshLayout.resetNoMoreData()
    } else {
        smartRefreshLayout.finishLoadMoreWithNoMoreData()
    }

    if (data.isSuccess) {
        this.dismissLoadingEx("")
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {
//                loadService.showEmpty()
                baseQuickAdapter.setList(arrayListOf())
                baseQuickAdapter.setEmptyView(R.layout.item_none_data_layout)
                baseQuickAdapter.notifyDataSetChanged()

                smartRefreshLayout.finishRefresh()
            }
            //是第一页
            data.isRefresh -> {

                baseQuickAdapter.setList(data.listData)
//                loadService.showSuccess()
                smartRefreshLayout.finishRefresh()
            }
            //不是第一页
            else -> {

                baseQuickAdapter.addData(data.listData)
//                loadService.showSuccess()
                smartRefreshLayout.finishLoadMore()
            }
        }
    } else {
        this.dismissLoadingEx(data.errMessage)
        toast(data.errMessage)
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
//            loadService.showError(data.errMessage)
            smartRefreshLayout.finishRefresh()
        } else {
//            recyclerView.loadMoreError(0, data.errMessage)
            smartRefreshLayout.finishLoadMore()
        }
    }
}

/**
 * 加载列表数据
 */
fun <T> FragmentActivity.loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>
) {


    if (data.isSuccess) {
        this.dismissLoadingEx("")
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {
//                loadService.showEmpty()
                baseQuickAdapter.setList(arrayListOf())
                baseQuickAdapter.setEmptyView(R.layout.item_none_data_layout)
                baseQuickAdapter.notifyDataSetChanged()


            }
            //是第一页
            data.isRefresh -> {

                baseQuickAdapter.setList(data.listData)
//                loadService.showSuccess()

            }
            //不是第一页
            else -> {

                baseQuickAdapter.addData(data.listData)
//                loadService.showSuccess()

            }
        }
    } else {
        this.dismissLoadingEx(data.errMessage)
        toast(data.errMessage)
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
//            loadService.showError(data.errMessage)

        } else {
//            recyclerView.loadMoreError(0, data.errMessage)

        }
    }
}


/**
 * 加载列表数据
 */
fun <T> Fragment.loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
//    loadService: LoadService<*>

) {

    this.requireActivity().dismissLoadingEx("")
    if (data.isSuccess) {
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {
//                loadService.showEmpty()

                baseQuickAdapter.setList(arrayListOf())
                baseQuickAdapter.setEmptyView(R.layout.item_none_data_layout)
                baseQuickAdapter.notifyDataSetChanged()
            }
            //是第一页
            data.isRefresh -> {

                baseQuickAdapter.setList(data.listData)
//                loadService.showSuccess()
            }
            //不是第一页
            else -> {
                baseQuickAdapter.addData(data.listData)
//                loadService.showSuccess()
            }
        }
    } else {
        toast(data.errMessage)
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
//            loadService.showError(data.errMessage)
        } else {
//            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}


/**
 * 加载列表数据
 */
fun <T> Fragment.loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    smartRefreshLayout: SmartRefreshLayout
) {

    this.requireActivity().dismissLoadingEx("")
    if (data.hasMore) {
        smartRefreshLayout.resetNoMoreData()
    } else {
        smartRefreshLayout.finishLoadMoreWithNoMoreData()
    }
    if (data.isSuccess) {
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {

                baseQuickAdapter.setList(arrayListOf())
                baseQuickAdapter.setEmptyView(R.layout.item_none_data_layout)
                baseQuickAdapter.notifyDataSetChanged()

                smartRefreshLayout.finishRefresh()
//                loadService?.showEmpty()

            }
            //是第一页
            data.isRefresh -> {

                baseQuickAdapter.setList(data.listData)
//                loadService?.showSuccess()
                smartRefreshLayout.finishRefresh()
            }
            else -> { //不是第一页

                baseQuickAdapter.addData(data.listData)
//                loadService?.showSuccess()
                smartRefreshLayout.finishLoadMore()
            }
        }
    } else {
        toast(data.errMessage)
        //失败
        if (data.isRefresh) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMoreWithNoMoreData()
        }
    }
}


fun SmartRefreshLayout.loadAndRefresh(load: () -> Unit, refresh: () -> Unit) {
    this.setOnLoadMoreListener {
        load.invoke()
    }
    this.setOnRefreshListener {
        refresh.invoke()
    }
}
