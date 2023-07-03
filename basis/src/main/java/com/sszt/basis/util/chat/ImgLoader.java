package com.sszt.basis.util.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sszt.basis.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by cxf on 2017/8/9.
 */

public class ImgLoader {

    private static final boolean SKIP_MEMORY_CACHE = false;

    private static BlurTransformation sBlurTransformation;

    static {
        sBlurTransformation = new BlurTransformation(40);
    }


    public static void display(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }
        if (url != null && !url.equals("") && url.contains("/upload/")) {
            Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).placeholder(R.drawable.ic_chat_file_default).error(R.drawable.ic_chat_file_default).into(imageView);
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).placeholder(R.drawable.ic_chat_file_default).error(R.drawable.ic_chat_file_default).into(imageView);
    }

    public static void displayNull(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayWithError(Context context, String url, ImageView imageView, int errorRes) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).error(errorRes).into(imageView);
    }

    public static void displayAvatar(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }
        displayWithError(context, url, imageView, R.drawable.ic_chat_file_default);
    }

    public static void display(Context context, File file, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(file).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void display(Context context, int res, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(res).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }


    public static void clear(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);

    }

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();

    }

    /**
     * 显示视频封面缩略图
     */

    public static void displayVideoThumb(Context context, String videoPath, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(Uri.fromFile(new File(videoPath))).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayVideoThumbRemote(Context context, String videoPath, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(Uri.parse(videoPath)).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayDrawable(Context context, String url, final DrawableCallback callback) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (callback != null) {
                    callback.onLoadSuccess(resource);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (callback != null) {
                    callback.onLoadFailed();
                }
            }
        });
    }


    public static void display(Context context, String url, ImageView imageView, int placeholderRes) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).placeholder(placeholderRes).into(imageView);
    }

    /**
     * 显示模糊的毛玻璃图片
     */
    public static void displayBlur(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(url)
                .skipMemoryCache(SKIP_MEMORY_CACHE)
                .apply(RequestOptions.bitmapTransform(sBlurTransformation))
                .into(imageView);
    }


    public interface DrawableCallback {
        void onLoadSuccess(Drawable drawable);

        void onLoadFailed();
    }


}
