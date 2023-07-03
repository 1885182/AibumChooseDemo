package com.sszt.basis.ext

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.sszt.basis.R
import com.sszt.basis.adapter.FileAllAdapter
import com.sszt.basis.adapter.PicAdapter
import com.sszt.basis.bean.PicBean
import com.sszt.basis.weight.DialogView

/**
 * <b>标题：</b>  <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2022/8/1 17:17<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */

fun PicAdapter.init(context: Activity, recyclerView: RecyclerView, selctPic: () -> Unit) {
    recyclerView.init(
        object : FlexboxLayoutManager(context) {
            override fun canScrollVertically() = false
        },
        this, true
    )
    val lookUrl = ArrayList<PicBean>()
    lookUrl.add(PicBean(R.mipmap.hui_add, 1))
    this.setList(lookUrl)
    this.addChildClickViewIds(R.id.picDel, R.id.picIv)

    this.setOnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.picDel -> {
                DialogView.showConfirmCancelDialog(content = "确定删除图片？") {
                    this.remove(position)
                    this.notifyDataSetChanged()
                }
            }
            R.id.picIv -> {
                if (this.getItem(position).type == 1) {
                    if (this.data.size >= 7) {
                        context.toast("最多选择6张图片")
                        return@setOnItemChildClickListener
                    }
                    selctPic.invoke()
                } else {
                    lookUrl.clear()
                    this.data.filter { it.type == 0 }.forEach {
                        lookUrl.add(it)
                    }
                    view.lookBigPicture(context, lookUrl, position.minus(1))

                }
            }
        }
    }

}

fun FileAllAdapter.init(
    context: Activity,
    recyclerView: RecyclerView,
    clickBack: (position:Int) -> Unit
) {
    recyclerView.init(LinearLayoutManager(context), this)
    this.addChildClickViewIds(R.id.fileAllDel)
    this.setOnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.fileAllDel -> {
                DialogView.showConfirmCancelDialog(content = "确定删除文件？") {
                    this.remove(position)
                    this.notifyDataSetChanged()
                }
            }
        }
    }
    this.setOnItemClickListener { _, _, position ->
        clickBack.invoke(position)

    }

}