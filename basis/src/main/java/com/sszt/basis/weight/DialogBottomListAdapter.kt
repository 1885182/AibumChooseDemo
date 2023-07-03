package com.sszt.basis.weight

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.R

/**
 *
 * @param time 2021/7/14
 * @param des 底部列表弹窗
 * @param author Meng
 *
 */
class DialogBottomListAdapter(data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.dialog_bottom_list_item_layout, data
    ) {
    override fun convert(holder: BaseViewHolder, item: String) {

        holder.setText(R.id.dialogBottomListItemTitle,item?:"")

    }
}