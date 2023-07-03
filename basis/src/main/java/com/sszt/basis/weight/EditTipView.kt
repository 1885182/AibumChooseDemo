package com.sszt.basis.weight

import android.content.Context
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.*
import android.widget.EditText
import com.hjq.toast.ToastUtils
import com.sszt.basis.R
import com.sszt.basis.ext.view.textString
import java.lang.reflect.Field


class EditTipView :  EditText {

    private val TAG = "EditTipView"

    var tipMsg = "输入超出最大长度"
    var etTipFocusable=true

    constructor(context: Context) : super(context, null) {

    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : super(context, attrs, 0) {

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EditTipView, 0, 0)

        tipMsg = typedArray.getString(R.styleable.EditTipView_msg) ?: "输入超出最大长度"
        etTipFocusable=typedArray.getBoolean(R.styleable.EditTipView_etTipFocusable,true)

        if (etTipFocusable){
            //获取焦点
            this.isFocusableInTouchMode = true
            this.isFocusable=true
        }else{
            //获取焦点
            this.isFocusableInTouchMode = false
            this.isFocusable=false
        }

        this.setOnFocusChangeListener { v, hasFocus ->
            //光标移至文章末尾
            if (hasFocus)
                setSelection(textString().length)
        }

    }

    /**
     * 重写onCreateInputConnection 时为了获取用户是否点击输入法还在输入
     */
    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection? {
        Log.e(TAG, "onCreateInputConnection: $outAttrs ")
        if (onCheckIsTextEditor() && isEnabled) {
            outAttrs!!.inputType = inputType

            if (focusSearch(FOCUS_DOWN) != null) {
                outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_FLAG_NAVIGATE_NEXT
            }
            if (focusSearch(FOCUS_UP) != null) {
                outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS
            }

            if (outAttrs.imeOptions and EditorInfo.IME_MASK_ACTION
                == EditorInfo.IME_ACTION_UNSPECIFIED
            ) {
                if (outAttrs.imeOptions and EditorInfo.IME_FLAG_NAVIGATE_NEXT != 0) {
                    // An action has not been set, but the enter key will move to
                    // the next focus, so set the action to that.
                    outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_ACTION_NEXT
                } else {
                    // An action has not been set, and there is no focus to move
                    // to, so let's just supply a "done" action.
                    outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_ACTION_DONE
                }

            }

            val inputListent = EditTipInputConnection(this)
            outAttrs.initialSelStart = selectionStart
            outAttrs.initialSelEnd = selectionEnd
            outAttrs.initialCapsMode = inputListent.getCursorCapsMode(inputType)
            inputListent.setOnIEditTipInput { newStr, oldStr ->
                Log.e(TAG, "onCreateInputConnection: $newStr==$oldStr==${textString()}")
                val maxLengthForTextView = getMaxLengthForTextView(this)
                if (maxLengthForTextView == -1) return@setOnIEditTipInput
                val newLength = newStr.length
                val oldLength = oldStr.length
                if (newLength.plus(oldLength) > maxLengthForTextView) {
                    ToastUtils.show(tipMsg)
                }

            }
            return inputListent
        }
        return null

    }

    /**
     * 获取设置的最大长度
     */
    fun getMaxLengthForTextView(textView: EditText): Int {
        var maxLength = -1
        for (filter in textView.filters) {
            if (filter is LengthFilter) {
                try {
                    val maxLengthField: Field = filter.javaClass.getDeclaredField("mMax")
                    maxLengthField.setAccessible(true)
                    if (maxLengthField.isAccessible()) {
                        maxLength = maxLengthField.getInt(filter)
                    }
                } catch (e: IllegalAccessException) {
                    Log.w(filter.javaClass.name, e)
                } catch (e: IllegalArgumentException) {
                    Log.w(filter.javaClass.name, e)
                } catch (e: NoSuchFieldException) {
                    Log.w(filter.javaClass.name, e)
                }
            }
        }
        return maxLength
    }


}