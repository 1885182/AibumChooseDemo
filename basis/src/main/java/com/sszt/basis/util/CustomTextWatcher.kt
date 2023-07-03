package com.sszt.basis.util

import android.text.Editable
import android.text.TextWatcher

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2022/3/28 15:10<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
interface CustomTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }


}