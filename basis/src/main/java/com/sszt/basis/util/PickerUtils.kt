package com.sszt.basis.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.contrarywind.view.WheelView
import com.sszt.basis.R
import com.sszt.basis.ext.util.logw
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @param time 2021/7/9
 * @param des 底部选择器
 * @param author Meng
 *
 */
object PickerUtils {


    val timeType = "yyyy-MM-dd HH:mm"
    val timeTypeNot = "yyyy-MM-dd"

    /**
     * @param date   传入时间
     * @param showType   选择器显示的时间格式  年月日 时分秒 true 为显示 false为不显示 不要大于六个
     * @param iOnGetDate 显示的回调
     */
    fun getTime(
        mContext: Context,
        @Nullable date: String = "",
        showType: BooleanArray,
        timeStyle: String,
        iOnGetDate: (date: String, posision: Int) -> Unit
    ) {
        val cal = Calendar.getInstance()

        if (!TextUtils.isEmpty(date)) {
            cal.time = stringToDate(date, timeStyle)
        } else {
            cal.time = Date()
        }
        val startCalendar = Calendar.getInstance()
        startCalendar.set(
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DATE),
            startCalendar.get(Calendar.HOUR),
            startCalendar.get(Calendar.MINUTE)
        )
        val endDate = Calendar.getInstance()
        endDate.set(
            2050,
            endDate.get(Calendar.YEAR) + 1,
            endDate.get(Calendar.DATE)
        )
        val pvTime = TimePickerBuilder(mContext) { date, view ->
            iOnGetDate.invoke(getTimes(date, timeStyle), 0)
        }
            .setType(showType)
            .setDate(cal)
            .setRangDate(
                startCalendar, endDate
            )
            .isDialog(true)
            .build()


        val mDialog = pvTime!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pvTime.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM)
            }
        }
        pvTime.show()
    }


    /**
     * 没有设置结束时间
     */
    fun getTimeNonEndTime(
        mContext: Context,
        showType: BooleanArray,
        timeStyle: String,
        iOnGetDate: (date: String, posision: Int) -> Unit
    ) {

        val startDate = Calendar.getInstance()
        startDate.set(1900, 0, 23)
        val pvTime = TimePickerBuilder(mContext) { date, view ->
            iOnGetDate.invoke(getTimes(date, timeStyle), 0)
        }.setType(showType)
            .setDate(Calendar.getInstance())
            .isDialog(true)
            .build()


        val mDialog = pvTime!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pvTime.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM)
            }
        }
        pvTime.show()
    }

    /**
     * 结束时间--今天
     */
    fun getTimeEndTimeNow(
        mContext: Context,
        showType: BooleanArray,
        timeStyle: String,
        iOnGetDate: (date: String, posision: Int) -> Unit
    ) {

        val startCalendar = Calendar.getInstance()
        startCalendar.set(1930, 1, 1)
        val nowCalendar = Calendar.getInstance()
        startCalendar.set(2008, 5, 5)
        val endDate = Calendar.getInstance()
        endDate.set(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH),
            endDate.get(Calendar.DATE)
        )
        val pvTime = TimePickerBuilder(mContext) { date, view ->
            iOnGetDate.invoke(getTimes(date, timeStyle), 0)
        }.setType(showType)
            .setDate(nowCalendar)
            .isDialog(true)
            .setRangDate(startCalendar, endDate)
            .setDate(nowCalendar)
            .build()


        val mDialog = pvTime!!.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pvTime.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM)
            }
        }
        pvTime.show()
    }

    /**
     * @param startTime  开始时间
     * @param date   输入框显示的时间格式
     * @param showType   选择器显示的时间格式  年月日 时分秒 true 为显示 false为不显示 不要大于六个
     * @param iOnGetDate 显示的回调
     */
    fun getTime(
        mContext: Context,
        startTime: String,
        date: String,
        showType: BooleanArray,
        iOnGetDate: (date: String, posision: Int) -> Unit
    ) {

        val selectedDate = Calendar.getInstance()

        val startDate = Calendar.getInstance()
        //        startDate.set(2013, 0, 23);

        //        Calendar endDate = Calendar.getInstance();
        //        endDate.set(2019, 11, 28);

        val dateformat = SimpleDateFormat(timeType)//自定义时间格式
        var d: Date? = null//String转Date
        try {
            d = dateformat.parse(startTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        startDate.time = d//设置日历


        //

        val pvTime = TimePickerBuilder(mContext) { date1, view ->
            iOnGetDate.invoke(getTimes(date1, date), 0)
        }.setType(showType)
            .setRangDate(startDate, selectedDate)
            .setDate(selectedDate)
            .isDialog(true)
            .build()


        val mDialog = pvTime!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pvTime!!.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM)
            }
        }
        pvTime!!.show()
    }

    /**
     * 展示数据
     */
    fun getData(
        mContext: Context,
        strings: List<String>,
        iOnGetDate: (date: String, posision: Int) -> Unit
    ) {
        val pickerView = OptionsPickerBuilder(mContext) { op1, op2, op3, v ->
            iOnGetDate.invoke(strings[op1], op1)

        }.isDialog(true).build<String>()

        pickerView.setPicker(strings)

        val mDialog = pickerView!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pickerView.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window


            //修改动画样式
            dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            //改成Bottom,底部显示
            dialogWindow.setGravity(Gravity.BOTTOM)

            val wl = dialogWindow.attributes
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT
            // 设置显示位置
            mDialog.onWindowAttributesChanged(wl)


            if (dialogWindow != null) {

            }
        }
        pickerView.show()
    }

    /**
     * 最多三级联动
     */
    fun showMoreLinkage(
        mContext: Context,
        gson: ArrayList<MoreLinkageBean>,
        iOnGetDate: (dataList: ArrayList<String>) -> Unit
    ) {
        val options1Items = ArrayList<String>()
        val options2Items = ArrayList<ArrayList<String>>()
        val options3Items = ArrayList<ArrayList<ArrayList<String>>>()
        gson.forEach {
            options1Items.add(it.areaName ?: "")
            val items = ArrayList<String>()
            val items2 = ArrayList<ArrayList<String>>()
            if (it.areaList != null) {
                it.areaList.forEach { it1 ->
                    items.add(it1.areaName)
                    val items3 = ArrayList<String>()
                    if (it1.areaList != null) {
                        it1.areaList.forEach { it2 ->
                            if (it2.areaName.isBlank()) {
                                items3.add("")
                            } else {
                                items3.add(it2.areaName)
                            }
                        }
                        items2.add(items3)
                    }
                }
            }

            if (items.isNotEmpty()) {
                options2Items.add(items)
            }
            if (items2.isNotEmpty()) {
                options3Items.add(items2)
            }
        }


        val pickerView = OptionsPickerBuilder(mContext) { op1, op2, op3, v ->

            "$op1 $op2 $op3 ".logw()
            if (options2Items.isNotEmpty() && options3Items.isNotEmpty() && options1Items.isNotEmpty()) {
                iOnGetDate.invoke(
                    arrayListOf(
                        options1Items[op1],
                        options2Items[op1][op2],
                        options3Items[op1][op2][op3]
                    )
                )
            } else if (options2Items.isNotEmpty() && options1Items.isNotEmpty()) {
                iOnGetDate.invoke(arrayListOf(options1Items[op1], options2Items[op1][op2]))
            } else {
                iOnGetDate.invoke(arrayListOf(options1Items[op1]))
            }

        }.setDividerType(WheelView.DividerType.WRAP)
            .setCancelColor(ContextCompat.getColor(mContext, R.color.color_333333))
            .setBgColor(ContextCompat.getColor(mContext, R.color.white))
            .setSubmitColor(ContextCompat.getColor(mContext, com.sszt.moduleresources.R.color.color_main))
            .setDividerColor(ContextCompat.getColor(mContext, R.color.tr))
            .setContentTextSize(16)
            .setTextColorCenter(ContextCompat.getColor(mContext, R.color.color_333333))
            .setLineSpacingMultiplier(3f)
            .setOutSideCancelable(false)
            .setTextColorOut(ContextCompat.getColor(mContext, R.color.color_999999))
            .isDialog(true)
            .build<String>()


        if (options2Items.isNotEmpty() && options3Items.isNotEmpty() && options1Items.isNotEmpty()) {
            pickerView.setPicker(
                options1Items,
                options2Items as List<MutableList<String>>?,
                options3Items as List<MutableList<MutableList<String>>>?
            ) //三级联动
        } else if (options2Items.isNotEmpty() && options1Items.isNotEmpty()) {
            pickerView.setPicker(options1Items, options2Items as List<MutableList<String>>?)//二级联动
        } else {
            pickerView.setPicker(options1Items)//一级联动
        }

        val mDialog = pickerView!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pickerView.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            //修改动画样式
            dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            //改成Bottom,底部显示
            dialogWindow.setGravity(Gravity.BOTTOM)

            val wl = dialogWindow.attributes
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT
            // 设置显示位置
            mDialog.onWindowAttributesChanged(wl)


            if (dialogWindow != null) {

            }
        }
        pickerView.show()
    }

    /**
     * 发案地
     * 最多三级联动
     */
    fun showRailroadGuardArea(
        mContext: Context,
        gson: ArrayList<RailroadGuardAreaListBean>,
        iOnGetDate: (dataList: ArrayList<String>) -> Unit
    ) {
        val options1Items = ArrayList<String>()
        val options2Items = ArrayList<ArrayList<String>>()
        val options3Items = ArrayList<ArrayList<ArrayList<String>>>()
        gson.forEach {
            options1Items.add(it.name ?: "")
            val items = ArrayList<String>()
            val items2 = ArrayList<ArrayList<String>>()
            if (!it.children.isNullOrEmpty()) {
                it.children.forEach { it1 ->
                    items.add(it1.name?:"")
                    val items3 = ArrayList<String>()
                    if (!it1.children.isNullOrEmpty()) {
                        it1.children.forEach { it2 ->
                            if (it2.name?.isBlank()==true) {
                                items3.add("")
                            } else {
                                items3.add(it2.name?:"")
                            }
                        }
                        items2.add(items3)
                    }else{
                        items3.add("")
                        items2.add(items3)
                    }
                }
            }else{
                items.add("")
                items2.add(arrayListOf(""))
            }

            if (items.isNotEmpty()) {
                options2Items.add(items)
            }
            if (items2.isNotEmpty()) {
                options3Items.add(items2)
            }
        }


        val pickerView = OptionsPickerBuilder(mContext) { op1, op2, op3, v ->

            "$op1 $op2 $op3 联动数据".logw()
            if (options2Items.isNotEmpty() && options3Items.isNotEmpty() && options1Items.isNotEmpty()) {
                iOnGetDate.invoke(
                    arrayListOf(
                        options1Items[op1],
                        options2Items[op1][op2],
                        options3Items[op1][op2][op3]
                    )
                )
            } else if (options2Items.isNotEmpty() && options1Items.isNotEmpty()) {
                iOnGetDate.invoke(arrayListOf(options1Items[op1], options2Items[op1][op2]))
            } else {
                iOnGetDate.invoke(arrayListOf(options1Items[op1]))
            }

        }.setDividerType(WheelView.DividerType.WRAP)
            .setCancelColor(ContextCompat.getColor(mContext, R.color.color_333333))
            .setBgColor(ContextCompat.getColor(mContext, R.color.white))
            .setSubmitColor(ContextCompat.getColor(mContext, com.sszt.moduleresources.R.color.color_main))
            .setDividerColor(ContextCompat.getColor(mContext, R.color.tr))
            .setContentTextSize(16)
            .setTextColorCenter(ContextCompat.getColor(mContext, R.color.color_333333))
            .setLineSpacingMultiplier(3f)
            .setOutSideCancelable(false)
            .setTextColorOut(ContextCompat.getColor(mContext, R.color.color_999999))
            .isDialog(true)
            .build<String>()


        if (options2Items.isNotEmpty() && options3Items.isNotEmpty() && options1Items.isNotEmpty()) {
            pickerView.setPicker(
                options1Items,
                options2Items as List<MutableList<String>>?,
                options3Items as List<MutableList<MutableList<String>>>?
            ) //三级联动
        } else if (options2Items.isNotEmpty() && options1Items.isNotEmpty()) {
            pickerView.setPicker(options1Items, options2Items as List<MutableList<String>>?)//二级联动
        } else {
            pickerView.setPicker(options1Items)//一级联动
        }

        val mDialog = pickerView!!.dialog
        if (mDialog != null) {

            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )

            params.leftMargin = 0
            params.rightMargin = 0
            pickerView.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            //修改动画样式
            dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            //改成Bottom,底部显示
            dialogWindow.setGravity(Gravity.BOTTOM)

            val wl = dialogWindow.attributes
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT
            // 设置显示位置
            mDialog.onWindowAttributesChanged(wl)


            if (dialogWindow != null) {

            }
        }
        pickerView.show()
    }
    open class RailroadGuardAreaListBean(
        val children: List<RailroadGuardAreaListBean>?,
        val label: String?,
        val name: String?
    )
    open class MoreLinkageBean(val areaName: String, val areaList: ArrayList<MoreLinkageBean>?)


    private fun getTimes(date: Date, timeType: String): String {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.time)
        val format =
            SimpleDateFormat(if (TextUtils.isEmpty(timeType)) "yyyy.MM.dd" else timeType)
        return format.format(date)
    }

    ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    fun parseTimeString2Date(timeString: String?): Date? {
        if (timeString == null || timeString == "") {
            return null
        }
        var date: Date? = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            date = Date(dateFormat.parse(timeString).time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    fun convertDate2String(date: Date?, pattern: String): String? {
        if (date == null)
            return null
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }

    fun getYear(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(0, 4))
    }

    fun getMonth(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(5, 7))
    }

    fun getDay(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(8, 10))
    }

    fun getHour(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(11, 13))
    }

    fun getMinute(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(14, 16))
    }

    fun getSecond(timeString: String): Int {
        val timeStr =
            convertDate2String(parseTimeString2Date(timeString), "yyyy-MM-dd HH:mm:ss")
        return Integer.parseInt(timeStr!!.substring(17, 19))
    }


}

private fun stringToDate(string: String?, dateType: String) =
    SimpleDateFormat(dateType).parse(string ?: "")