package com.sszt.basis.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.sszt.basis.R;

/**
 * 魅族周视图
 * Created by huanghaibin on 2017/11/29.
 */

public class MeizuWeekView extends WeekView {
    private Paint mTextPaint = new Paint();

    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;
    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();


    public MeizuWeekView(Context context) {
        super(context);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFe1e1e1);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xffed5353);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 4);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);
    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.color_3292ff));
        canvas.drawRect(x, 0, x + mItemWidth, mItemHeight, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        mTextPaint.setColor(calendar.getSchemeColor());
        int cx = x + mItemWidth / 2;
        canvas.drawCircle(x + mItemWidth - 20, 20, mRadio, mSchemeBasicPaint);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(calendar.getScheme(), 0, calendar.getScheme().length(), bounds);
        int widthScheme = bounds.width() + getPaddingLeft() + getPaddingRight();

        canvas.drawText(calendar.getScheme(), cx - widthScheme / 2, mTextBaseLine + mItemHeight / 5, mTextPaint);

    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = 0;

        boolean isInRange = isInRange(calendar);

        //今天日期处理逻辑
        if (calendar.isCurrentDay() && !isSelected) {
            mSelectTextPaint.setColor(Color.WHITE);
            canvas.drawRect(x, 0, x + mItemWidth, mItemHeight, mCurrentDayPaint);
            mSchemeBasicPaint.setColor(calendar.getSchemeColor());
            canvas.drawCircle(x + mItemWidth - 20, 20, mRadio, mSchemeBasicPaint);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            if (calendar != null && calendar.getScheme() != null) {
                mTextPaint.setColor(calendar.getSchemeColor());
                Rect bounds = new Rect();
                mTextPaint.getTextBounds(calendar.getScheme(), 0, calendar.getScheme().length(), bounds);
                int widthScheme = bounds.width() + getPaddingLeft() + getPaddingRight();

                canvas.drawText(calendar.getScheme(), cx - widthScheme / 2, mTextBaseLine + mItemHeight / 5, mTextPaint);
            }
        } else if (isSelected) {
            mSelectTextPaint.setColor(Color.WHITE);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            mSchemeTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_030303));

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);

//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() && isInRange ? mCurDayTextPaint :
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
