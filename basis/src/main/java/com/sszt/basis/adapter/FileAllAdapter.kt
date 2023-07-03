package com.sszt.basis.adapter


import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.sszt.basis.R
import com.sszt.basis.bean.PicBean
import com.sszt.basis.ext.load
import com.sszt.basis.ext.view.gone
import com.sszt.basis.ext.view.visible

/**
 * <b>标题：</b>  选择图片 <br>
 * <b>描述：</b> 选择图片 <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/11/12 9:31<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
class FileAllAdapter(data: ArrayList<PicBean>,val delIsShown: Boolean = true) :
    BaseQuickAdapter<PicBean, BaseViewHolder>(
        R.layout.item_file_all_layout, data
    ) {
    override fun convert(holder: BaseViewHolder, item: PicBean) {
        holder.setText(R.id.fileAllName,item.fileName?:"")
        holder.getView<ImageView>(R.id.fileAllDel).let {
            if (delIsShown){
                it.visible()
            }else{
                it.gone()
            }
        }

    }
}