package com.sszt.basis.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/***
 * <b>标题：</b> 设置吸顶 <br>
 * <b>描述：</b> <br>
 * <b>创建：</b>2022/3/25 9:50<br>
 * <b>更新：</b>时间： 更新人： 更新内容：
 * @param
 * @return
 * @author mengwenyue
 * @version V1.0.0
 */
public class MyCeilingScrollView extends ScrollView {
    private CeilingScrollChangedListener mListener;

    public MyCeilingScrollView(Context context) {
        super(context);
    }

    public MyCeilingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollChangeListener(CeilingScrollChangedListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onCeilingScrollChangedListener(l, t, oldl, oldt);
        }
    }

    public interface CeilingScrollChangedListener {
        void onCeilingScrollChangedListener(int x, int y, int oldX, int oldY);
    }
}
