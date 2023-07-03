package com.ren.newalbumchoose.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.ren.newalbumchoose.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class ImageViewerAdapter extends Adapter<ImageViewerAdapter.ImageHolder> {

    private List<?> mDatas;

    private OnItemClickListener mOnItemClickListener;

    public ImageViewerAdapter(List<?> list) {
        this.mDatas = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_image_viewer_item, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(mDatas.get(position))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                        holder.photoView.setImageBitmap(resource);
                        holder.photoView.setZoomable(true);
                        holder.photoView.setMinimumScale(1f);
                        holder.photoView.setMediumScale(2f);
                        holder.photoView.setMaximumScale(8f);
                    }

                    @Override
                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        holder.photoView.setImageResource(R.mipmap.pic_def_aibum);
                        holder.photoView.setZoomable(false);
                    }

                    @Override
                    public void onLoadStarted(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.photoView.setImageResource(R.mipmap.pic_def_aibum);
                        holder.photoView.setZoomable(false);
                    }

                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                    }
                });

        holder.photoView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onClick(view, position);
            }
        });
        holder.photoFL.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    static class ImageHolder extends RecyclerView.ViewHolder {

        PhotoView photoView;
        FrameLayout photoFL;

        private ImageHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photoView);
            photoFL = itemView.findViewById(R.id.photoFL);

        }


    }

}
