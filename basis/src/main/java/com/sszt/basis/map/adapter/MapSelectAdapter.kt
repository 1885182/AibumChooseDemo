package com.sszt.basis.map.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.R
import com.sszt.basis.map.bean.PoiBean

/**
 *
 * @param time 2021/6/29
 * @param des 普通Adapter
 * @param author Meng
 *
 */
class MapSelectAdapter(data: ArrayList<PoiBean>) :
    BaseQuickAdapter<PoiBean, BaseViewHolder>(
        R.layout.item_map_select_layout, data
    ) {
    override fun convert(holder: BaseViewHolder, item: PoiBean) {

        val distant =
//            if (item.distant < 1) {
//            "当前位置"
//        } else {
            "${item.distant}米"
//        }


        holder.setText(R.id.itemMapSelectName, item.title ?: "")
//        holder.setText(
//            R.id.itemMapSelectAddress, "$distant | ${item.address}"
//        )
  holder.setText(
            R.id.itemMapSelectAddress, "${item.address}"
        )

        holder.getView<ImageView>(R.id.itemMapSelectIV).let {
            if (item.isSelect)
                it.setImageResource(com.sszt.moduleresources.R.mipmap.blue_select)
            else
                it.setImageResource(com.sszt.moduleresources.R.drawable.rc_cdfdfdf_kuang_shape)
        }

    }
}