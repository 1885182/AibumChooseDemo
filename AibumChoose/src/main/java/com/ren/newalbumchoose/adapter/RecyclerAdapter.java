package com.ren.newalbumchoose.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.bean.ImageBean;
import com.ren.newalbumchoose.utils.OtherUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter {

    public static int PIC = 0;
    public static int VIDEO = 1;
    public static int CAMERA = 2;

    private Context mContext;

    private ArrayList<ImageBean> mData = new ArrayList();


    public RecyclerAdapter(Activity context) {
        mContext = context;
//        mImageSize = Utils.getImageItemWidth(context);
    }

    public void setData(List<ImageBean> list) {
        mData.clear();
        mData.addAll(list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = null;

        if (viewType == PIC) {
            v = LayoutInflater.from(mContext).inflate(R.layout.activity_choose_image_grid_item, parent, false);
             return new PicViewHolder(v);
        } else if (viewType == VIDEO) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_choose_video_grid_item,  parent, false));
        } else if (viewType == CAMERA) {
            return new CameraViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_camera_item,  parent, false));
        }

        return null;
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
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> {
            iOnClick.click(position);
        });
        if (mData.get(position).getType() == PIC) {
            exPicHolder((PicViewHolder) holder, position);
        } else if (mData.get(position).getType() == VIDEO) {
            exVideoHolder((VideoViewHolder) holder, position);
        } else if (mData.get(position).getType() == CAMERA) {
            exCameraHolder((CameraViewHolder) holder, position);
        }

    }

    private void exPicHolder(PicViewHolder holder, int position) {

        if (mData.get(position).isChecked()) {

            holder.choose_item_select.setImageResource(R.mipmap.choose_image_yes);
            holder.choose_item_image.setColorFilter(Color.parseColor("#77000000"));
        } else {

            holder.choose_item_select.setImageResource(R.mipmap.choose_image_no);
            holder.choose_item_image.setColorFilter(null);
        }
        Glide.with(mContext).load(mData.get(position).getPath()).into(holder.choose_item_image);

    }


    private void exVideoHolder(VideoViewHolder holder, int position) {

        holder.choose_item_video_mb.setText(OtherUtils.getSize(mData.get(position).getPath()));

        if (mData.get(position).isChecked()) {
            holder.choose_item_video_select.setImageResource(R.mipmap.choose_image_yes);
            holder.choose_item_video.setColorFilter(Color.parseColor("#77000000"));
        } else {

            holder.choose_item_video_select.setImageResource(R.mipmap.choose_image_no);
            holder.choose_item_video.setColorFilter(null);
        }
        Glide.with(mContext).load(mData.get(position).getPath()).into(holder.choose_item_video);


//        holder.choose_item_video_square.addView(iv);

    }

    private void exCameraHolder(CameraViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView choose_item_image;
        ImageView choose_item_select;
        ConstraintLayout rl_root_grid_item;

        public PicViewHolder(View itemView) {
            super(itemView);
            choose_item_image = itemView.findViewById(R.id.choose_item_image);
            choose_item_select = itemView.findViewById(R.id.choose_item_select);
            rl_root_grid_item = itemView.findViewById(R.id.rl_root_grid_item);

        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView choose_item_video, choose_item_videoPlayer;
        ImageView choose_item_video_select;
        TextView choose_item_video_mb;
//        SquareLayout choose_item_video_square;

        public VideoViewHolder(View itemView) {
            super(itemView);
            choose_item_video = itemView.findViewById(R.id.choose_item_video);
            choose_item_videoPlayer = itemView.findViewById(R.id.choose_item_videoPlayer);
            choose_item_video_select = itemView.findViewById(R.id.choose_item_video_select);
            choose_item_video_mb = itemView.findViewById(R.id.choose_item_video_mb);
//            choose_item_video_square = itemView.findViewById(R.id.choose_item_video_square);


        }
    }

    public static class CameraViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout camera;

        public CameraViewHolder(View itemView) {
            super(itemView);
            camera = itemView.findViewById(R.id.camera);
        }
    }

}
