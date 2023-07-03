package com.sszt.basis.weight

 import com.chad.library.adapter.base.BaseQuickAdapter
 import com.chad.library.adapter.base.viewholder.BaseViewHolder
 import com.sszt.basis.R


class DialogBottomDataAdapter(data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_dialog_bottom_data_layout, data
    ) {
    override fun convert(holder: BaseViewHolder, item: String) {

        holder.setText(R.id.itemDialogDataTv,item)

    }
}