package com.sszt.basis.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sszt.basis.R
import com.sszt.basis.ext.toast
import java.io.File


/**
 * @author ：MENG
 * @date : 2019/8/3 17:26
 * @package：com.yaoqi.firecontrolgis.Utils
 * @description :
 */
object OpenFileUtils {
    /**
     * 声明各种类型文件的dataType
     */
    private const val DATA_TYPE_APK = "application/vnd.android.package-archive"
    private const val DATA_TYPE_VIDEO = "video/*"
    private const val DATA_TYPE_MUSIC = "music/*"
    private const val DATA_TYPE_AUDIO = "audio/*"
    private const val DATA_TYPE_HTML = "text/html"
    private const val DATA_TYPE_IMAGE = "image/*"
    private const val DATA_TYPE_PPT = "application/vnd.ms-powerpoint"
    private const val DATA_TYPE_EXCEL = "application/vnd.ms-excel"
    private const val DATA_TYPE_WORD = "application/msword"
    private const val DATA_TYPE_CHM = "application/x-chm"
    private const val DATA_TYPE_TXT = "text/plain"
    private const val DATA_TYPE_PDF = "application/pdf"

    /**
     * 未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
     */
    private const val DATA_TYPE_ALL = "*/*"

    /**
     * 打开文件
     *
     * @param mContext
     * @param file /data/user/0/com.sszt.grassrootsgovernance/cache/PRD文档案例.docx/PRD文档案例.docx
     *             /data/user/0/com.sszt.grassrootsgovernance/cache/PRD文档案例.docx
     *
     */
    fun openFile(mContext: Activity, file: File) {
        if (!file.exists()) {
            mContext.toast("文件损坏，打开失败")
            return
        }
        val view =
            mContext.layoutInflater.inflate(R.layout.dialog_file_selected_layout, null, false)

        val bottomSheetDialog = BottomSheetDialog(mContext)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.findViewById<RadioGroup>(R.id.dialogFileSelectRG)
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.dialogFileSelectTBS -> {
                        X5WebUtils.openFile(mContext, file.absolutePath) {
                            val a=it
                            val b=0
                        }
                        bottomSheetDialog.dismiss()
                    }
                    R.id.dialogFileSelectSystem -> {
                        try {
                            // 取得文件扩展名
                            val end =
                                file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase()
                            when (end) {
                                "doc", "docx" -> commonOpenFileWithType(
                                    mContext,
                                    file,
                                    DATA_TYPE_WORD
                                )
                                "xls", "xlsx" -> commonOpenFileWithType(
                                    mContext,
                                    file,
                                    DATA_TYPE_EXCEL
                                )
                                "txt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_TXT)
                                "htm", "html" -> commonOpenFileWithType(
                                    mContext,
                                    file,
                                    DATA_TYPE_HTML
                                )
                                "ppt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PPT)
                                "pdf" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PDF)
                                else -> commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
                            }
                        } catch (e: Exception) {
                            mContext.toast("系统阅读器打开失败，正在通过TBS阅读器打开")
                            X5WebUtils.openFile(mContext, file.absolutePath) {
                            }
                        }
                        bottomSheetDialog.dismiss()
                    }
                    R.id.dialogFileSelectCustom -> {
                        commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
                        bottomSheetDialog.dismiss()
                    }
                }


            }
//        view.findViewById<RadioButton>(R.id.dialogFileSelectTBS).setOnClickListener {
//            X5WebUtils.openFile(mContext, file.absolutePath) {
//            }
//            bottomSheetDialog.dismiss()
//        }
//
//        view.findViewById<RadioButton>(R.id.dialogFileSelectCustom).setOnClickListener {
//            commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
//            bottomSheetDialog.dismiss()
//        }
//
//        view.findViewById<RadioButton>(R.id.dialogFileSelectSystem).setOnClickListener {
//            try {
//                // 取得文件扩展名
//                val end = file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase()
//                when (end) {
//                    "doc", "docx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_WORD)
//                    "xls", "xlsx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_EXCEL)
//                    "txt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_TXT)
//                    "htm", "html" -> commonOpenFileWithType(mContext, file, DATA_TYPE_HTML)
//                    "ppt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PPT)
//                    "pdf" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PDF)
//                    else -> commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
//                }
//            } catch (e: Exception) {
//                mContext.toast("系统阅读器打开失败，正在通过TBS阅读器打开")
//                X5WebUtils.openFile(mContext, file.absolutePath) {
//                }
//            }
//            bottomSheetDialog.dismiss()
//        }


    }


    fun openFileAll(mContext: Activity, file: File) {
        try {
            // 取得文件扩展名
            val end = file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase()
            when (end) {
                "3gp", "mp4" -> openVideoFileIntent(mContext, file)
                "m4a", "mp3", "mid", "xmf", "ogg", "wav" -> openAudioFileIntent(mContext, file)
                "doc", "docx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_WORD)
                "xls", "xlsx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_EXCEL)
                "jpg", "gif", "png", "jpeg", "bmp" -> commonOpenFileWithType(
                    mContext,
                    file,
                    DATA_TYPE_IMAGE
                )
                "txt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_TXT)
                "htm", "html" -> commonOpenFileWithType(mContext, file, DATA_TYPE_HTML)
                "apk" -> commonOpenFileWithType(mContext, file, DATA_TYPE_APK)
                "ppt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PPT)
                "pdf" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PDF)
                "chm" -> commonOpenFileWithType(mContext, file, DATA_TYPE_CHM)
                else -> commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
            }
        } catch (e: Exception) {
            mContext.toast("打开失败")
        }
    }


    /**
     * Android传入type打开文件
     *
     * @param mContext
     * @param file
     * @param type
     */
    private fun commonOpenFileWithType(mContext: Activity, file: File?, type: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        FileProviderUtils.setIntentDataAndType(mContext, intent, type, file, true)
        mContext.startActivityForResult(intent, 2000)
    }


    /**
     * Android打开Video文件
     *
     * @param mContext
     * @param file
     */
    private fun openVideoFileIntent(mContext: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_VIDEO, file, false)
        mContext.startActivity(intent)
    }

    /**
     * Android打开Music文件
     *
     * @param mContext
     * @param file
     */
    private fun openMusicFileIntent(mContext: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_MUSIC, file, false)
        mContext.startActivity(intent)
    }

    /**
     * Android打开Audio文件
     *
     * @param mContext
     * @param file
     */
    private fun openAudioFileIntent(mContext: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_AUDIO, file, false)
        mContext.startActivity(intent)
    }

    //判断文件是否存在
    fun fileIsExists(strFile: String?): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }
}