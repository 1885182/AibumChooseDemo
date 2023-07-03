package com.sszt.basis.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 根据fragment的高度设置 viewpage的高度
 */
public class AutoHeightViewPager extends ViewPager {

    private int current;
    private int height = 0;
    private int addHeight = 0;
    /**
     * 保存position与对于的View
     */
    private final HashMap<Integer, View> mChildrenViews = new LinkedHashMap();

    private boolean scrollble = true;

    public AutoHeightViewPager(Context context) {
        super(context);
    }

    public AutoHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > current) {
            View child = mChildrenViews.get(current);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight() + addHeight;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * resetHeight() 重置ViewPager高度
     *
     * @param current 当前的第几个Fragment
     */
    public void resetHeight(int current) {
        this.current = current;
        if (mChildrenViews.size() > current) {
            ViewGroup.LayoutParams layoutParams =  getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            setLayoutParams(layoutParams);
        }
    }

    /**
     * 保存position与对于的View
     *
     * @param view     fragment 布局里面的根布局View
     * @param position 当前的第几个Fragment
     */
    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }

    /**
     * 保存position与对于的View
     *
     * @param view      fragment 布局里面的根布局View
     * @param position  当前的第几个Fragment
     * @param addHeight 附加高度
     */
    public void setObjectForPosition(View view, int position, int addHeight) {
        this.addHeight = addHeight;
        mChildrenViews.put(position, view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    /**
     * 设置是否可滑动
     *
     * @param scrollble
     */
    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

}

