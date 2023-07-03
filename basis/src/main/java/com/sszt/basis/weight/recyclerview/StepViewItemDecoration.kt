package com.sszt.basis.weight.recyclerview

import android.content.Context
import android.graphics.*
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.sszt.basis.ext.util.loge

/**
 *
 * @param time 2021/7/7
 * @param des 通过RV分割线实现的竖向时间轴
 * @param author Meng
 *
 * @param selectIndex 到哪一步了
 * @param selectColor 经过点的颜色
 * @param unSelectColor 未经过点和线条颜色
 * @param isShowLastLine 是否显示最后一条线
 * @param isStroke 圈是实心还是空心 TODO 默认空心
 * @param itemLeft 赋值ItemView的左偏移长度
 * @param bitMapRes  指示器设置成图片
 * */
class StepViewItemDecoration(
    mContext: Context,
    val selectIndex: Int = 0,
    val selectColor: Int = Color.parseColor("#4EC2F5"),
    val unSelectColor: Int = Color.parseColor("#D7D7D7"),
    val isShowLastLine: Boolean = false,
    val isStroke: Boolean = true,
    val itemLeft: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        16f,
        mContext.resources.displayMetrics
    ), // 赋值ItemView的顶部偏移长度
    val itemTop: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        6f,
        mContext.resources.displayMetrics
    ),
    // 赋值轴点圆的半径为10
    val bigCircleRadius: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        5f,
        mContext.resources.displayMetrics
    ),
    val bitMapRes: Bitmap? = null
) : RecyclerView.ItemDecoration() {
    //到达时间画笔
    var mPaintGreen = Paint()

    //未到达时间的画笔
    var mPaintGray = Paint()

    // 赋值ItemView的左偏移长度
//    var itemLeft = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        16f,
//        mContext.resources.displayMetrics
//    )


    init {
        mPaintGreen.color = selectColor

        mPaintGreen.style = if (isStroke) Paint.Style.STROKE else Paint.Style.FILL
        mPaintGreen.strokeWidth = SizeUtils.dp2px(1f).toFloat()

        mPaintGray.color = unSelectColor
        mPaintGray.style = if (isStroke) Paint.Style.STROKE else Paint.Style.FILL
        mPaintGray.strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            mContext.resources.displayMetrics
        )
    }

    //同样是绘制内容，但与onDraw（）的区别是：绘制在图层的最上层
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

    }

    //在子视图上设置绘制范围，并绘制内容
    //绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            // 轴点 = 圆 = 圆心(x,y)
            val centerX =
                if (bitMapRes != null) {
                    bitMapRes.width
                } else {
                    child.left / 2
                }

            val centerY = child.top + itemTop
            //画圈
//            if (i <= selectIndex) {
            //画到达内圈圆
//                mPaintGreen.alpha = 128


            "宽度：》》》》---- ${bitMapRes?.width?.toFloat()}".loge()

            if (bitMapRes != null) {
                canvas.drawBitmap(
                    bitMapRes,
                    bitMapRes.width.div(2).toFloat(),
                    child.top.toFloat(),
                    mPaintGreen
                )
            } else {
                canvas.drawCircle(
                    centerX.toFloat(),
                    centerY,
                    (bigCircleRadius).toFloat(),
                    mPaintGreen
                )

            }

            //画到达外圈圆
//                mPaintGreen.alpha = 255
//                canvas.drawCircle(centerX.toFloat(), centerY, bigCircleRadius, mPaintGreen)
//            } else {
//                canvas.drawCircle(centerX.toFloat(), centerY, bigCircleRadius, mPaintGray)
//            }


            if (isShowLastLine) {

                val pointCenter = Point(centerX, (centerY + (bigCircleRadius)).toInt())


                //画线
                when (i) {
                    0 -> {
                        val pointBottom = Point(centerX, child.bottom)
                        canvas.drawLine(
                            pointCenter.x.toFloat(),
                            pointCenter.y.toFloat(),
                            pointBottom.x.toFloat(),
                            pointBottom.y.toFloat(),
                            mPaintGray
                        )
                        //最后一个Itme不画线
                    }
                    childCount - 1 -> {
                        val pointTop = Point(centerX, (child.top - itemTop).toInt())
                        canvas.drawLine(
                            pointTop.x.toFloat(),
                            pointTop.y.toFloat(),
                            pointCenter.x.toFloat(),
                            pointCenter.y.toFloat(),
                            mPaintGray
                        )
                    }
                    else -> {
                        val pointTop = Point(centerX, (child.top - itemTop).toInt())
                        val pointCenter1 = Point(centerX, (centerY - bigCircleRadius).toInt())
                        val pointCenter2 = Point(centerX, (centerY + bigCircleRadius).toInt())
                        val pointBottom = Point(centerX, child.bottom)

                        canvas.drawLine(
                            pointTop.x.toFloat(),
                            pointTop.y.toFloat(),
                            pointCenter1.x.toFloat(),
                            pointCenter1.y.toFloat(),
                            mPaintGray
                        )
                        canvas.drawLine(
                            pointBottom.x.toFloat(),
                            pointBottom.y.toFloat(),
                            pointCenter2.x.toFloat(),
                            pointCenter2.y.toFloat(),
                            mPaintGray
                        )
                    }
                }
            } else {
                val pointCenter = Point(centerX, (centerY + (bigCircleRadius)).toInt())

                val pointCenter1 = Point(centerX, (centerY - bigCircleRadius).toInt())

                val pointCenter2 = Point(centerX, (centerY + bigCircleRadius).toInt())
                //画线
                when (i) {
                    0 -> {
                        val pointBottom = Point(centerX, child.bottom)
                        canvas.drawLine(
                            pointCenter.x.toFloat(),
                            pointCenter.y.toFloat(),
                            pointBottom.x.toFloat(),
                            pointBottom.y.toFloat(),
                            mPaintGray
                        )
                        //最后一个Itme不画线
                    }
                    else -> {
                        val pointTop = Point(centerX, (child.top - itemTop).toInt())
                        val pointBottom = Point(centerX, child.bottom)

                        canvas.drawLine(
                            pointTop.x.toFloat(),
                            pointTop.y.toFloat(),
                            pointCenter1.x.toFloat(),
                            pointCenter1.y.toFloat(),
                            mPaintGray
                        )
                        canvas.drawLine(
                            pointBottom.x.toFloat(),
                            pointBottom.y.toFloat(),
                            pointCenter2.x.toFloat(),
                            pointCenter2.y.toFloat(),
                            mPaintGray
                        )
                    }
                }
            }


        }
    }

    //设置ItemView的内嵌偏移长度（inset）
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(itemLeft.toInt(), itemTop.toInt(), 0, 0)
    }
}