package com.ren.newalbumchoose.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.bean.ImageFolder;

import java.util.List;

/**
 * Created by win10 on 2017/6/19.
 */

public class PopDirListAdapter extends BaseAdapter {
    private Context context;
    private List<ImageFolder> mDatas;

    public PopDirListAdapter(Context context,List<ImageFolder> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(convertView == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_choose_image_list_dir_item,parent,false);
            holder.id_dir_item_images = (ImageView) convertView.findViewById(R.id.id_dir_item_images);
            holder.id_dir_item_names = (TextView) convertView.findViewById(R.id.id_dir_item_names);
            holder.id_dir_item_counts = (TextView) convertView.findViewById(R.id.id_dir_item_counts);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.id_dir_item_names.setText(mDatas.get(position).getName());
        holder.id_dir_item_counts.setText(mDatas.get(position).getPhotoList().size() + "张");
        Glide.with(context).load(mDatas.get(position).getPhotoList().get(0).getPath()).into(holder.id_dir_item_images);
        return convertView;
    }

    class MyViewHolder {
        private ImageView id_dir_item_images;
        private TextView id_dir_item_names,id_dir_item_counts;
    }
}
