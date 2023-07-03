package com.sszt.basis.weight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.sszt.basis.R


/**
 *
 * @param time 2021/7/8
 * @param des 展开收起的LinLayout
 * @param author Meng
 *
 * @TODO 注意：在布局中设置 android:animateLayoutChanges="true"
 */
class ExpandableLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {
    private var tvTip: TextView? = null
    private var ivArrow: ImageView? = null
    private var isExpand = false //是否是展开状态，默认是隐藏
    private val defaultItemCount //一开始展示的条目数
            : Int
    private val expandText //待展开显示的文字
            : String?
    private val hideText //待隐藏显示的文字
            : String?
    private val useDefaultBottom //是否使用默认的底部，默认为true使用默认的底部
            : Boolean
    private var hasBottom //是否已经有底部，默认为false，没有
            = false
    private var bottomView: View? = null
    private val fontSize: Float
    private val textColor: Int
    private val arrowResId: Int
    private val mPosition = 0

    /**
     * 渲染完成时初始化默认底部view
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        findViews()
    }

    /**
     * 初始化底部view
     */
    private fun findViews() {
        bottomView = inflate(context, R.layout.expand_layoutl_bottom, null)
        ivArrow = bottomView?.findViewById<View>(R.id.iv_arrow) as ImageView
        tvTip = bottomView?.findViewById<View>(R.id.tv_tip) as TextView
        tvTip!!.paint.textSize = fontSize
        tvTip!!.setTextColor(textColor)
        tvTip!!.text = expandText ?: "展开"
        ivArrow!!.setImageResource(arrowResId)
        bottomView?.setOnClickListener(this)
    }

    fun addItem(view: View) {
        if (!useDefaultBottom) {
            //如果不使用默认底部
            addView(view)
            val childCount = childCount
            if (childCount > defaultItemCount) {
                hide()
            }
            return
        }

        //使用默认底部
        if (!hasBottom) {
            //如果还没有底部
            addView(view)
        } else {
            val childCount = childCount
            addView(view, childCount - 1) //插在底部之前
        }
        refreshUI(view)
    }

    override fun setOrientation(orientation: Int) {
        require(HORIZONTAL != orientation) { "ExpandableTextView only supports Vertical Orientation." }
        super.setOrientation(orientation)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = childCount
        Log.i(TAG, "childCount: $childCount")
        justToAddBottom(childCount)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 判断是否要添加底部
     *
     * @param childCount
     */
    private fun justToAddBottom(childCount: Int) {
        if (childCount > defaultItemCount) {
            if (useDefaultBottom && !hasBottom) {
                //要使用默认底部,并且还没有底部
                addView(bottomView) //添加底部
                hide()
                hasBottom = true
                bottomView!!.visibility = VISIBLE
            }
        }
    }

    /**
     * 刷新UI
     *
     * @param view
     */
    private fun refreshUI(view: View) {
        val childCount = childCount
        if (childCount > defaultItemCount) {
            if (childCount - defaultItemCount == 1) {
                //刚超过默认，判断是否要添加底部
                justToAddBottom(childCount)
            }
            view.visibility = GONE //大于默认数目的先隐藏
            //            AnimationUtils.invisibleAnimator(view);
        }
    }

    /**
     * 展开
     */
    private fun expand() {
        for (i in defaultItemCount until childCount) {
            //从默认显示条目位置以下的都显示出来
            val view = getChildAt(i)
            view.visibility = VISIBLE

//            AnimationUtils.visibleAnimator(view);
        }
    }

    /**
     * 收起
     */
    private fun hide() {
        val endIndex =
            if (useDefaultBottom) childCount - 1 else childCount //如果是使用默认底部，则结束的下标是到底部之前，否则则全部子条目都隐藏
        for (i in defaultItemCount until endIndex) {
            //从默认显示条目位置以下的都隐藏
            val view = getChildAt(i)
            view.visibility = GONE
            //            AnimationUtils.invisibleAnimator(view);
        }
        if (bottomView != null) {
            bottomView!!.visibility = INVISIBLE
            val valueAnimator = ObjectAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 200
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    //                    bottomView.setAlpha(1);
                    bottomView!!.visibility = VISIBLE
                }
            })
            valueAnimator.start()
        }
    }

    // 箭头的动画
    @SuppressLint("ObjectAnimatorBinding")
    private fun doArrowAnim() {
        if (isExpand) {
            // 当前是展开，将执行收起，箭头由上变为下
            ObjectAnimator.ofFloat(ivArrow, "rotation", -180f, 0f).start()
        } else {
            // 当前是收起，将执行展开，箭头由下变为上
            ObjectAnimator.ofFloat(ivArrow, "rotation", 0f, 180f).start()
        }
    }

    override fun onClick(v: View) {
        toggle()
    }

    fun toggle() {
        if (isExpand) {
            hide()
            tvTip!!.text = expandText
        } else {
            expand()
            tvTip!!.text = hideText
        }
        doArrowAnim()
        isExpand = !isExpand

        //回调
        if (mStateListener != null) {
            mStateListener!!.onStateChanged(isExpand)
        }
    }

    private var mStateListener: OnStateChangeListener? = null

    /**
     * 定义状态改变接口
     */
    interface OnStateChangeListener {
        fun onStateChanged(isExpanded: Boolean)
    }

    fun setOnStateChangeListener(mListener: OnStateChangeListener?) {
        mStateListener = mListener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        val endIndex = if (useDefaultBottom) childCount - 1 else childCount //如果是使用默认底部，则结束的下标是到底部之前
        for (i in 0 until endIndex) {
            val view = getChildAt(i)
            view.setOnClickListener { v -> listener.onItemClick(v, i) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    companion object {
        private val TAG = ExpandableLinearLayout::class.java.simpleName
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLinearLayout)
        defaultItemCount = ta.getInt(R.styleable.ExpandableLinearLayout_defaultItemCount, 2)
        expandText = ta.getString(R.styleable.ExpandableLinearLayout_expandText)
        hideText = ta.getString(R.styleable.ExpandableLinearLayout_hideText)
        fontSize = ta.getDimension(
            R.styleable.ExpandableLinearLayout_tipTextSize,
            SizeUtils.sp2px(14f).toFloat()
        )
        textColor = ta.getColor(
            R.styleable.ExpandableLinearLayout_tipTextColor,
            Color.parseColor("#666666")
        )
        arrowResId =
            ta.getResourceId(R.styleable.ExpandableLinearLayout_arrowDownImg, R.drawable.arrow_down)
        useDefaultBottom = ta.getBoolean(R.styleable.ExpandableLinearLayout_useDefaultBottom, true)
        ta.recycle()
        orientation = VERTICAL
    }
}