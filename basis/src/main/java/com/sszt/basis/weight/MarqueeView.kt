package com.sszt.basis.weight

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.AnimRes
import androidx.lifecycle.*
import com.blankj.utilcode.util.SizeUtils
import com.sszt.basis.R
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @param time 2021/7/6
 * @param des 滚动的跑马灯
 * @param author Meng
 *
 */
class MarqueeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewFlipper(context, attrs) {
    private val TAG = "MarqueeView"

    //时间间隔
    private var interval = 3000
    private var lineSpacing = 1f

    //    最大显示行数
    private var maxLins = 1
    private var hasSetAnimDuration = false
    private var animDuration = 1000

    //    设置文本大小
    private var textSize = 14

    //    设置文本颜色
    private var textColor = -0x1

    //
    private var singleLine = false
    private var gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
    private var hasSetDirection = false

    //    方向
    private var direction = DIRECTION_BOTTOM_TO_TOP

    @AnimRes
    private var inAnimResId: Int = R.anim.anim_bottom_in

    @AnimRes
    private var outAnimResId: Int = R.anim.anim_top_out
    private var position = 0
    private var notices = ArrayList<String>()
    private var onItemClickListener: OnItemClickListener? = null

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0)
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval)
        maxLins = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvMaxLins, maxLins)
        lineSpacing = typedArray.getFloat(R.styleable.MarqueeViewStyle_mvLineSpacing, lineSpacing)
        hasSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration)
        animDuration =
            typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration)
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false)
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize =
                typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize.toFloat())
                    .toInt()
            textSize = SizeUtils.px2sp(textSize.toFloat())
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor)
        val gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, GRAVITY_LEFT)
        when (gravityType) {
            GRAVITY_LEFT -> gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            GRAVITY_CENTER -> gravity = Gravity.CENTER
            GRAVITY_RIGHT -> gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            else -> {
            }
        }
        hasSetDirection = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvDirection)
        direction = typedArray.getInt(R.styleable.MarqueeViewStyle_mvDirection, direction)
        if (hasSetDirection) {
            when (direction) {
                DIRECTION_BOTTOM_TO_TOP -> {
                    inAnimResId = R.anim.anim_bottom_in
                    outAnimResId = R.anim.anim_top_out
                }
                DIRECTION_TOP_TO_BOTTOM -> {
                    inAnimResId = R.anim.anim_top_in
                    outAnimResId = R.anim.anim_bottom_out
                }
                DIRECTION_RIGHT_TO_LEFT -> {
                    inAnimResId = R.anim.anim_right_in
                    outAnimResId = R.anim.anim_left_out
                }
                DIRECTION_LEFT_TO_RIGHT -> {
                    inAnimResId = R.anim.anim_left_in
                    outAnimResId = R.anim.anim_right_out
                }
                else -> {
                }
            }
        } else {
            inAnimResId = R.anim.anim_bottom_in
            outAnimResId = R.anim.anim_top_out
        }
        typedArray.recycle()
        flipInterval = interval
    }
    /**
     * 根据字符串，启动翻页公告
     *
     * @param notice       字符串
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    /**
     * 根据字符串，启动翻页公告
     *
     * @param notice 字符串
     */
    @JvmOverloads
    fun startWithText(
        notice: String,
        @AnimRes inAnimResId: Int = this.inAnimResId,
        @AnimRes outAnimResID: Int = outAnimResId
    ) {
        if (TextUtils.isEmpty(notice)) return
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                startWithFixedWidth(notice, inAnimResId, outAnimResID)
            }
        })
    }

    /**
     * 根据字符串和宽度，启动翻页公告
     *
     * @param notice 字符串
     */
    private fun startWithFixedWidth(
        notice: String,
        @AnimRes inAnimResId: Int,
        @AnimRes outAnimResID: Int
    ) {
        val noticeLength = notice.length
        val width: Int = SizeUtils.px2dp(width.toFloat())
        if (width == 0) {
            throw RuntimeException("Please set the width of MarqueeView !")
        }
        val limit = width / textSize
        val list = ArrayList<String>()
        if (noticeLength <= limit) {
            list.add(notice)
        } else {
            val size = noticeLength / limit + if (noticeLength % limit != 0) 1 else 0
            for (i in 0 until size) {
                val startIndex = i * limit
                val endIndex =
                    if ((i + 1) * limit >= noticeLength) noticeLength else (i + 1) * limit
                list.add(notice.substring(startIndex, endIndex))
            }
        }
        if (notices == null) notices = ArrayList()
        notices!!.clear()
        notices!!.addAll(list)
        postStart(inAnimResId, outAnimResID)
    }
    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param notices      字符串列表
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param notices 字符串列表
     */
    @JvmOverloads
    fun startWithList(
        notices: ArrayList<String>,
        @AnimRes inAnimResId: Int = this.inAnimResId,
        @AnimRes outAnimResID: Int = outAnimResId
    ) {
        if (notices.isNullOrEmpty()) return
        setNotices(notices)
        postStart(inAnimResId, outAnimResID)
    }

    private fun postStart(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        post { start(inAnimResId, outAnimResID) }
    }

    private var isAnimStart = false
    private fun start(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        removeAllViews()
        clearAnimation()
        position = 0
        addView(createTextView(notices!![position]))
        if (notices.size > 1) {
            setInAndOutAnimation(inAnimResId, outAnimResID)
            startFlipping()
        }
        if (inAnimation != null) {
            inAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isAnimStart) {
                        animation.cancel()
                    }
                    isAnimStart = true
                }

                override fun onAnimationEnd(animation: Animation) {
                    position++
                    if (position >= notices!!.size) {
                        position = 0
                    }
                    val view: View = createTextView(notices!![position])
                    if (view.parent == null) {
                        addView(view)
                    }
                    isAnimStart = false
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun createTextView(text: CharSequence): TextView {
        var textView = getChildAt((displayedChild + 1) % 3)
        if (textView is TextView) {

        } else {
            textView = TextView(context)
        }

        textView = TextView(context)
        textView.gravity = gravity
        textView.setTextColor(textColor)
        textView.textSize = textSize.toFloat()
        textView.isSingleLine = singleLine
        textView.maxLines = maxLins


        textView.setOnClickListener { v: View? ->
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(getPosition(), v as TextView?)
            }
        }

//参数add：增加的间距数值，对应android:lineSpacingExtra参数。
//参数mult：增加的间距倍数，对应android:lineSpacingMultiplier参数。
        textView.setLineSpacing(lineSpacing, 1f)
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.text = text
        textView.tag = position

        return textView
    }

    fun getPosition(): Int {
        return currentView.tag as Int
    }

    fun getNotices(): List<CharSequence>? {
        return notices
    }

    fun setNotices(notices: ArrayList<String>) {
        this.notices = notices
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, textView: TextView?)
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    private fun setInAndOutAnimation(@AnimRes inAnimResId: Int, @AnimRes outAnimResID: Int) {
        val inAnim = AnimationUtils.loadAnimation(
            context, inAnimResId
        )
        if (hasSetAnimDuration) inAnim.duration = animDuration.toLong()
        inAnimation = inAnim
        val outAnim = AnimationUtils.loadAnimation(
            context, outAnimResID
        )
        if (hasSetAnimDuration) outAnim.duration = animDuration.toLong()
        outAnimation = outAnim
    }


    companion object {
        private const val GRAVITY_LEFT = 0
        private const val GRAVITY_CENTER = 1
        private const val GRAVITY_RIGHT = 2
        private const val DIRECTION_BOTTOM_TO_TOP = 0
        private const val DIRECTION_TOP_TO_BOTTOM = 1
        private const val DIRECTION_RIGHT_TO_LEFT = 2
        private const val DIRECTION_LEFT_TO_RIGHT = 3
    }

    init {
        init(context, attrs, 0)
    }
}