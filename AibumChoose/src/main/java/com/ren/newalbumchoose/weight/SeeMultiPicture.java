package com.ren.newalbumchoose.weight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.activity.ImageViewerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * //                SeeMultiPicture.load(picPath)//要加载的图片数据，单张或多张
 * //                        .selection(4)//当前选中位置
 * //                        .indicator(true)//是否显示指示器，默认不显示
 * // //                      .imageLoader(new PicassoImageLoader())
 * //                        .theme(R.style.ImageViewerTheme)//设置主题风格，默认：R.style.ImageViewerTheme
 * //                        .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
 * //                        .start( this,view);
 */

/**
 * 查看多图
 */
public final class SeeMultiPicture {

    private ViewerSpec mViewerSpec;

    private ActivityOptionsCompat mOptionsCompat;

    private int placeholderDrawableId;
    private int errorDrawableId;

    private SeeMultiPicture(@NonNull List<?> data) {
        mViewerSpec = ViewerSpec.INSTANCE;
        mViewerSpec.reset();
        mViewerSpec.listData = data;
    }

    private SeeMultiPicture(@NonNull Object data) {
        mViewerSpec = ViewerSpec.INSTANCE;
        mViewerSpec.reset();
        List<Object> listData = new ArrayList<>();
        listData.add(data);
        mViewerSpec.listData = listData;
    }

    /**
     * 加载图片
     *
     * @param data List中的泛型支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@NonNull List<?> data) {
        return new SeeMultiPicture(data);
    }

    /**
     * 加载图片
     *
     * @param path 支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@NonNull String path) {
        return new SeeMultiPicture(path);
    }

    /**
     * 加载图片
     *
     * @param uri 支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@NonNull Uri uri) {
        return new SeeMultiPicture(uri);
    }

    /**
     * 加载图片
     *
     * @param resourceId 支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@DrawableRes int resourceId) {
        return new SeeMultiPicture(resourceId);
    }

    /**
     * 加载图片
     *
     * @param file 支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@NonNull File file) {
        return new SeeMultiPicture(file);
    }

    /**
     * 加载图片
     *
     * @param data 支持{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
     * @return
     */
    public static SeeMultiPicture load(@NonNull Object data) {
        return new SeeMultiPicture(data);
    }

    /**
     * 当前选中位置
     *
     * @param position 默认：0
     * @return
     */
    public SeeMultiPicture selection(int position) {
        mViewerSpec.position = position;
        return this;
    }

    /**
     * 设置占位图
     *
     * @param resourceId
     * @return
     */
    public SeeMultiPicture placeholder(@DrawableRes int resourceId) {
        this.placeholderDrawableId = resourceId;
        this.mViewerSpec.placeholderDrawable = null;
        return this;
    }

    /**
     * 设置占位图
     *
     * @param drawable
     * @return
     */
    public SeeMultiPicture placeholder(@Nullable Drawable drawable) {
        this.mViewerSpec.placeholderDrawable = drawable;
        this.placeholderDrawableId = 0;
        return this;
    }

    /**
     * 设置加载失败时显示的图片
     *
     * @param resourceId
     * @return
     */
    public SeeMultiPicture error(@DrawableRes int resourceId) {
        this.errorDrawableId = resourceId;
        this.mViewerSpec.errorDrawable = null;
        return this;
    }

    /**
     * 设置加载失败时显示的图片
     *
     * @param drawable
     * @return
     */
    public SeeMultiPicture error(@Nullable Drawable drawable) {
        this.mViewerSpec.errorDrawable = drawable;
        this.errorDrawableId = 0;
        return this;
    }

    /**
     * 设置是否显示指示器
     *
     * @param showIndicator 默认：false
     * @return
     */
    public SeeMultiPicture indicator(boolean showIndicator) {
        this.mViewerSpec.isShowIndicator = showIndicator;
        return this;
    }

    /**
     * 设置界面跳转过渡动画
     *
     * @param optionsCompat
     * @return
     */
    public SeeMultiPicture activityOptionsCompat(ActivityOptionsCompat optionsCompat) {
        this.mOptionsCompat = optionsCompat;
        return this;
    }

    /**
     * 设置屏幕方向
     *
     * @param orientation 默认：{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}
     * @return
     */
    public SeeMultiPicture orientation(int orientation) {
        this.mViewerSpec.orientation = orientation;
        return this;
    }

    /**
     * 设置主题风格
     *
     * @param theme 默认：{@link R.style#ImageViewerTheme}
     * @return
     */
    public SeeMultiPicture theme(@StyleRes int theme) {
        this.mViewerSpec.theme = theme;
        return this;
    }

    /**
     * 初始化资源
     *
     * @param context
     */
    private void initResource(Context context) {
        if (mViewerSpec.placeholderDrawable == null && placeholderDrawableId != 0) {
            mViewerSpec.placeholderDrawable = ContextCompat.getDrawable(context, placeholderDrawableId);
        }
        if (mViewerSpec.errorDrawable == null && errorDrawableId != 0) {
            mViewerSpec.errorDrawable = ContextCompat.getDrawable(context, errorDrawableId);
        }
    }

    /**
     * 启动
     *
     * @param activity 当前的{@link Activity}
     */
    public void start(@NonNull Activity activity) {
        start(activity, null);
    }

    /**
     * 启动
     *
     * @param activity      当前的{@link Activity}
     */
    public void start(@NonNull Activity activity, View sharedElement ) {
        if (mViewerSpec.listData.size()<1) {

            return;
        }
        initResource(activity);
        Intent intent = new Intent(activity, ImageViewerActivity.class);

        if (mOptionsCompat == null) {
//            if (sharedElement != null && mViewerSpec.listData.size() == 1) {
//                mOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, ImageViewerActivity.SHARED_ELEMENT);
//            } else {
                mOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.iv_anim_in, R.anim.iv_anim_out);
//            }
        }

        Bundle bundle = null;
        if (mOptionsCompat != null) {
            bundle = mOptionsCompat.toBundle();
        }

        activity.startActivity(intent, bundle);
    }

    /**
     * 启动
     *
     * @param fragment 当前的{@link Fragment}
     */
    public void start(@NonNull Fragment fragment) {
        start(fragment, null);
    }

    /**
     * 启动
     *
     * @param fragment      当前的{@link Fragment}
     * @param sharedElement 过渡动画的共享元素
     */
    public void start(@NonNull Fragment fragment, @Nullable View sharedElement) {
        if (mViewerSpec.listData.size()<1) {

            return;
        }
        initResource(fragment.getContext());
        Intent intent = new Intent(fragment.getContext(), ImageViewerActivity.class);

        if (mOptionsCompat == null) {
            if (sharedElement != null && mViewerSpec.listData.size() == 1) {
                mOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), sharedElement, ImageViewerActivity.SHARED_ELEMENT);
            } else {
                mOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(fragment.getContext(), R.anim.iv_anim_in, R.anim.iv_anim_out);
            }
        }

        Bundle bundle = null;
        if (mOptionsCompat != null) {
            bundle = mOptionsCompat.toBundle();
        }

        fragment.startActivity(intent, bundle);
    }

}
