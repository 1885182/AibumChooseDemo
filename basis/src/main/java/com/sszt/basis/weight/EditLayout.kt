package com.sszt.basis.weight

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.sszt.basis.R
import com.sszt.basis.ext.dp

/**
 *
 * @param time 2021/7/5
 * @param des 输入信息
 * @param author Meng
 *
 */
class EditLayout : LinearLayout {

    private val TAG = "EditLayout"

    lateinit var edit_layout: LinearLayout
    lateinit var edit_tv: TextView
    lateinit var edit_et: EditTipView

    //自定义属性、
    var tvColor = Color.BLACK
    var tvName = ""

    var tvSize = -1

    var tvBackgroundColor = Color.TRANSPARENT

    var tvMargin = 0

    var tvMarginTop = 0

    var tvMarginLeft = 0

    var tvMarginRight = 0

    var tvMarginBottom = 0

    var tvPadding = 0

    var tvPaddingTop = 0

    var tvPaddingLeft = 0

    var tvPaddingRight = 0

    var tvPaddingBottom = 0

    var etColor = Color.BLACK

    var etName = ""
    var etHint = ""

    var etSize = 14

    var etInputType = EditorInfo.TYPE_NULL

    var etMaxLines = -1
    var etMaxLength = -1
    var etMsg = ""
    var etDigits = ""
    var etFocusable = true

    var etBackgroundColor = Color.TRANSPARENT

    var etMargin = 0

    var etMarginTop = 0

    var etMarginLeft = 0

    var etMarginRight = 0

    var etMarginBottom = 0

    var etPadding = 0

    var etPaddingTop = 0

    var etPaddingLeft = 0

    var etPaddingRight = 0

    var etPaddingBottom = 0

    constructor(context: Context) : super(context, null) {

    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : super(context, attrs, 0) {
        val inflate = inflate(context, R.layout.edit_layout, this)
        edit_layout = inflate.findViewById(R.id.edit_layout)
        edit_tv = inflate.findViewById(R.id.edit_tv)
        edit_et = inflate.findViewById(R.id.edit_et)

        initType(context, attrs, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {


    }

    private fun initType(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int
    ) {

        //
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EditLayout, defStyleAttr, 0)

        initTV(typedArray)
        initET(typedArray)

        typedArray.recycle()

    }

    /**
     *EditView属性
     */
    private fun initET(typedArray: TypedArray) {

        etName = typedArray.getString(R.styleable.EditLayout_etName) ?: ""
        etHint = typedArray.getString(R.styleable.EditLayout_etHint) ?: ""
        etBackgroundColor =
            typedArray.getColor(R.styleable.EditLayout_etBackgroundColor, etBackgroundColor)
        etColor =
            typedArray.getColor(R.styleable.EditLayout_etColor, etColor)

        etMargin =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etMargin, etMargin).dp()
        etMarginTop =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etMarginTop, etMarginTop).dp()
        etMarginLeft =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etMarginLeft, etMarginLeft)
                .dp()
        etMarginRight =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etMarginRight, etMarginRight)
                .dp()
        etMarginBottom =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_etMarginBottom,
                etMarginBottom
            ).dp()
        etSize =
            typedArray.getDimensionPixelSize(
                R.styleable.EditLayout_etSize,
                edit_et.textSize.toInt()
            )

        etPadding =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etPadding, etPadding).dp()
        etPaddingTop =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etPaddingTop, etPaddingTop)
                .dp()
        etPaddingLeft =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_etPaddingLeft, etPaddingLeft)
                .dp()
        etPaddingRight =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_etPaddingRight,
                etPaddingRight
            ).dp()
        etPaddingBottom =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_etPaddingBottom,
                etPaddingBottom
            ).dp()

        etInputType = typedArray.getInt(R.styleable.EditLayout_etInputType, etInputType)
        etMaxLength = typedArray.getInt(R.styleable.EditLayout_etMaxLength, etMaxLength)
        etMaxLines = typedArray.getInt(R.styleable.EditLayout_etMaxLines, etMaxLines)
        etMsg = typedArray.getString(R.styleable.EditLayout_etMsg) ?: ""
        etDigits = typedArray.getString(R.styleable.EditLayout_etDigits) ?: ""
        etFocusable = typedArray.getBoolean(R.styleable.EditLayout_etFocusable, true)


        edit_et.etTipFocusable = etFocusable
        if (etDigits.isNotEmpty()) {
            edit_et.keyListener = DigitsKeyListener.getInstance(etDigits)
        }
        edit_et.tipMsg = etMsg
        edit_et.setBackgroundColor(etBackgroundColor)
        edit_et.setText(etName)
        edit_et.hint = etHint
        edit_et.setTextColor(etColor)
        edit_et.setTextSize(TypedValue.COMPLEX_UNIT_PX, etSize.toFloat())
        edit_et.inputType = etInputType
        edit_et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(etMaxLength))


        val layoutParams = edit_et.layoutParams as LinearLayout.LayoutParams
        if (etMargin != 0) {
            layoutParams.setMargins(
                etMargin,
                etMargin,
                etMargin,
                etMargin
            )
        } else {
            layoutParams.setMargins(
                etMarginLeft,
                etMarginTop,
                etMarginRight,
                etMarginBottom
            )
        }
        if (etPadding != 0) {
            edit_et.setPadding(
                etPadding,
                etPadding,
                etPadding,
                etPadding
            )
        } else {
            edit_et.setPadding(
                etPaddingLeft,
                etPaddingTop,
                etPaddingRight,
                etPaddingBottom
            )
        }

        edit_et.layoutParams = layoutParams

    }

    /**
     *TextView属性
     */
    private fun initTV(typedArray: TypedArray) {

        tvName = typedArray.getString(R.styleable.EditLayout_tvName) ?: ""
        tvBackgroundColor =
            typedArray.getColor(R.styleable.EditLayout_tvBackgroundColor, tvBackgroundColor)
        etColor =
            typedArray.getColor(R.styleable.EditLayout_etColor, etColor)

        tvMargin =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvMargin, tvMargin).dp()
        tvMarginTop =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvMarginTop, tvMarginTop).dp()
        tvMarginLeft =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvMarginLeft, tvMarginLeft)
                .dp()
        tvMarginRight =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvMarginRight, tvMarginRight)
                .dp()
        tvMarginBottom =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_tvMarginBottom,
                tvMarginBottom
            ).dp()

        tvPadding =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvPadding, tvPadding).dp()
        tvPaddingTop =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvPaddingTop, tvPaddingTop)
                .dp()
        tvPaddingLeft =
            typedArray.getDimensionPixelOffset(R.styleable.EditLayout_tvPaddingLeft, tvPaddingLeft)
                .dp()
        tvPaddingRight =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_tvPaddingRight,
                tvPaddingRight
            ).dp()
        tvPaddingBottom =
            typedArray.getDimensionPixelOffset(
                R.styleable.EditLayout_tvPaddingBottom,
                tvPaddingBottom
            ).dp()
        tvSize =
            typedArray.getDimension(R.styleable.EditLayout_tvSize, tvSize.toFloat()).toInt()

        edit_tv.setBackgroundColor(tvBackgroundColor)
        edit_tv.text = tvName
        edit_tv.setTextColor(tvColor)
        edit_et.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvSize.toFloat())
        Log.e(TAG, "initTV: ${edit_tv.textSize}")

        val layoutParams = edit_tv.layoutParams as LinearLayout.LayoutParams
        if (tvMargin != 0) {
            layoutParams.setMargins(
                tvMargin,
                tvMargin,
                tvMargin,
                tvMargin
            )
        } else {
            layoutParams.setMargins(
                tvMarginLeft,
                tvMarginTop,
                tvMarginRight,
                tvMarginBottom
            )
        }
        if (tvPadding != 0) {
            edit_tv.setPadding(
                tvPadding,
                tvPadding,
                tvPadding,
                tvPadding
            )
        } else {
            edit_tv.setPadding(
                tvPaddingLeft,
                tvPaddingTop,
                tvPaddingRight,
                tvPaddingBottom
            )
        }

        edit_tv.layoutParams = layoutParams

    }


}