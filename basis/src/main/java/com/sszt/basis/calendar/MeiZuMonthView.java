package com.sszt.basis.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.sszt.basis.R;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class MeiZuMonthView extends MonthView {
    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();
    //画分割线
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;

    public MeiZuMonthView(Context context) {
        super(context);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFe1e1e1);

        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(dipToPx(context, 0.5f));
        mRectPaint.setColor(0xffdfdfdf);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 3);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);


//        //兼容硬件加速无效的代码
//        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
//        //4.0以上硬件加速会导致无效
//        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.color_3292ff));
        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mSelectedPaint);
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        mTextPaint.setColor(calendar.getSchemeColor());
        int cx = x + mItemWidth / 2;
        canvas.drawCircle(x + mItemWidth - 20, y + 20, mRadio, mSchemeBasicPaint);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(calendar.getScheme(), 0, calendar.getScheme().length(), bounds);
        int widthScheme = bounds.width() + getPaddingLeft() + getPaddingRight();

        canvas.drawText(calendar.getScheme(), cx - widthScheme / 2, mTextBaseLine + y + mItemHeight / 5, mTextPaint);

    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    //防止重复画线
    private int cy = 0;

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
//        int top = y - mItemHeight / 5;
        int top = y;
        if (cy != y) {
            canvas.drawLine(x, y, getWidth(), y, mRectPaint);
        }
        cy = y;
        boolean isInRange = isInRange(calendar);
//今天日期处理逻辑
        if (calendar.isCurrentDay() && !isSelected) {
            mSelectTextPaint.setColor(Color.WHITE);
            canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mCurrentDayPaint);
            mSchemeBasicPaint.setColor(calendar.getSchemeColor());
            canvas.drawCircle(x + mItemWidth - 20, y + 20, mRadio, mSchemeBasicPaint);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            if (calendar != null && calendar.getScheme() != null) {
                mTextPaint.setColor(calendar.getSchemeColor());
                Rect bounds = new Rect();
                mTextPaint.getTextBounds(calendar.getScheme(), 0, calendar.getScheme().length(), bounds);
                int widthScheme = bounds.width() + getPaddingLeft() + getPaddingRight();

                canvas.drawText(calendar.getScheme(), cx - widthScheme / 2, mTextBaseLine + y + mItemHeight / 5, mTextPaint);

            }
        } else if (isSelected) {
            mSelectTextPaint.setColor(Color.WHITE);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
        } else if (hasScheme) {
            mSchemeTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_030303));

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }


    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
