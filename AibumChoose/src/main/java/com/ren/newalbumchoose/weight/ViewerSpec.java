package com.ren.newalbumchoose.weight;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.ren.newalbumchoose.R;

import java.util.List;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum ViewerSpec {

    INSTANCE;

    public int position;

    public List<?> listData;


    public boolean isShowIndicator;

    public @Nullable
    Drawable placeholderDrawable;

    @Nullable
    public Drawable errorDrawable;

    @StyleRes
    public int theme = R.style.ImageViewerTheme;

    public int orientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;

    void reset() {
        position = 0;
        listData = null;
        isShowIndicator = false;
        placeholderDrawable = null;
        errorDrawable = null;
        theme = R.style.ImageViewerTheme;
        orientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
    }

}
