package com.sszt.basis.weight

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.sszt.basis.R
import com.sszt.basis.ext.view.gone
import com.sszt.basis.ext.view.visible

/**
 *
 * @param time 2021/7/7
 * @param des 标题栏
 * @param author Meng
 *
 */
class TitleLayout : FrameLayout {

    lateinit var titleBack: ImageView
    lateinit var titleRightImg: ImageView
    lateinit var titleTitle: TextView
    lateinit var titleRightText: TextView
    lateinit var titleRL: RelativeLayout
    lateinit var titleFL: FrameLayout

    constructor(context: Context) : super(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : super(context, attrs, 0) {

        val inflate = inflate(context, R.layout.title_layout, this)

        titleFL = inflate.findViewById(R.id.titleFL)
        titleRL = inflate.findViewById(R.id.titleRL)
        titleBack = inflate.findViewById(R.id.titleBack)
        titleRightImg = inflate.findViewById(R.id.titleRightImg)
        titleTitle = inflate.findViewById(R.id.titleTitle)
        titleRightText = inflate.findViewById(R.id.titleRightText)

        titleFL.setBackgroundColor(ContextCompat.getColor(context,R.color.title_bg))

        //BarUtils.addMarginTopEqualStatusBarHeight(titleRL)

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.TitleLayout, 0, 0)

        val backVisibility =
            typedArray.getBoolean(R.styleable.TitleLayout_title_back_visibility, true)
        val backDrawable = typedArray.getDrawable(R.styleable.TitleLayout_title_back)


        titleBack.apply {
            if (backDrawable != null)
                setImageDrawable(backDrawable)
            if (backVisibility)
                visibility = VISIBLE
            else
                visibility = GONE
        }


        val titleString = typedArray.getString(R.styleable.TitleLayout_title_text)
        val titleTextSize = typedArray.getDimension(R.styleable.TitleLayout_title_text_size, 18f)
        val titleTextColor =
            typedArray.getColor(R.styleable.TitleLayout_title_text_color, Color.BLACK)
        titleTitle.apply {
            text = titleString ?: ""
            setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize)
            setTextColor(titleTextColor)
        }

        val titleRightString = typedArray.getString(R.styleable.TitleLayout_title_right_text)
        val titleRightTextSize =
            typedArray.getDimension(R.styleable.TitleLayout_title_text_right_size, 14f)
        val titleRightTextColor =
            typedArray.getColor(R.styleable.TitleLayout_title_text_right_color, Color.WHITE)
        val titleRightTextVisibility =
            typedArray.getBoolean(R.styleable.TitleLayout_title_text_right_visibility, false)
        val titleRightTextDrawable =
            typedArray.getDrawable(R.styleable.TitleLayout_title_text_right_pic)

        titleRightText.apply {
            titleRightTextDrawable?.let {
                it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
                setCompoundDrawables(null, null, it, null)
            }
            text = titleRightString ?: ""
            setTextSize(TypedValue.COMPLEX_UNIT_SP, titleRightTextSize)
            setTextColor(titleRightTextColor)
            if (titleRightTextVisibility)
                visible()
            else
                gone()

        }

        val rightIvVisibility =
            typedArray.getBoolean(R.styleable.TitleLayout_title_right_iv_visibility, false)
        val rightIvDrawable = typedArray.getDrawable(R.styleable.TitleLayout_title_right_iv)

        titleRightImg.apply {
            if (rightIvDrawable != null)
                setImageDrawable(rightIvDrawable)
            if (rightIvVisibility)
                visible()
            else
                gone()

        }
        typedArray.recycle()

    }

    fun setTitBgColor(color: Int){
        titleFL.setBackgroundColor(color)
    }
   fun setTitBg(drawable: Drawable?){
       titleFL.background = drawable
    }

    /**
     * 返回键监听
     */
    fun setOnBackClick(onClickListener: OnClickListener) {
        titleBack?.setOnClickListener(onClickListener)
    }

    /**
     * 右边监听
     */
    fun setOnRightClick(onClickListener: OnClickListener) {
        titleRightText?.setOnClickListener(onClickListener)
    }

    /**
     * 右边监听
     */
    fun setOnRightImgClick(onClickListener: OnClickListener) {
        titleRightImg?.setOnClickListener(onClickListener)
    }


}