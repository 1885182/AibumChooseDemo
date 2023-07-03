package com.ren.newalbumchoose.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.bean.ImageFolder;
import com.ren.newalbumchoose.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTopAdapter extends RecyclerView.Adapter<RecyclerTopAdapter.PicViewHolder> {


    private Context mContext;

    private ArrayList<ImageFolder> mData = new ArrayList();

    private int mImageSize;

    public RecyclerTopAdapter(Activity context) {
        mContext = context;
        mImageSize = Utils.getImageItemWidth(context);
    }

    public void setData(List<ImageFolder> list) {
        mData.clear();
        mData.addAll(list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_choose_image_list_dir_item, parent, false);
        return new PicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PicViewHolder holder, int position) {
        holder.id_dir_item_names.setText(mData.get(position).getName());
        holder.id_dir_item_counts.setText(mData.get(position).getPhotoList().size() + "张");
        if (mData.get(position).getPhotoList().size() > 0)
            Glide.with(mContext).load(mData.get(position).getPhotoList().get(0).getPath()).into(holder.id_dir_item_images);
        holder.itemView.setOnClickListener(v -> iOnClick.click(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();

    }

    private IOnClick iOnClick;

    public void setOnItemClick(IOnClick i) {
        this.iOnClick = i;
    }

    public interface IOnClick {

        void click(int position);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class PicViewHolder extends RecyclerView.ViewHolder {
        private ImageView id_dir_item_images;
        private TextView id_dir_item_names, id_dir_item_counts;

        public PicViewHolder(View convertView) {
            super(convertView);
            id_dir_item_images = (ImageView) convertView.findViewById(R.id.id_dir_item_images);
            id_dir_item_names = (TextView) convertView.findViewById(R.id.id_dir_item_names);
            id_dir_item_counts = (TextView) convertView.findViewById(R.id.id_dir_item_counts);

        }
    }


}
