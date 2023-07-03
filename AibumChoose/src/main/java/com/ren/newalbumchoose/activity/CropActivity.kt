package com.ren.newalbumchoose.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ren.newalbumchoose.R
import com.ren.newalbumchoose.utils.CustomCrop
import com.ren.newalbumchoose.weight.CropImageView
import java.io.File
import java.util.*

/**
 *
 * @param time 2021/8/5
 * @param des 裁剪图片Activity
 * @param author Meng
 *
 */
class CropActivity : AppCompatActivity(), CropImageView.OnBitmapSaveCompleteListener {

    lateinit var mCropImageView: CropImageView

    //原始图片路径
    private var imagePath = ""

    //裁剪的宽度
    private var cropWidth = 500

    //裁剪的高度
    private var cropHeight = 500
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 隐藏顶部的状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_crop)

        imagePath = intent.getStringExtra(CustomCrop.CROP_PATH) ?: ""
        cropWidth = intent.getIntExtra(CustomCrop.CROP_WIDTH, 500)
        cropHeight = intent.getIntExtra(CustomCrop.CROP_HEIGHT, 500)

        if (imagePath.isNullOrBlank()) {
            Toast.makeText(this, "文件获取失败", Toast.LENGTH_SHORT).show()
            return
        }

        mCropImageView = findViewById(R.id.cropIv)
        mCropImageView.setOnBitmapSaveCompleteListener(this)

        mCropImageView.focusStyle =
            CropImageView.Style.RECTANGLE
        mCropImageView.focusWidth = cropWidth
        mCropImageView.focusHeight = cropHeight

        //缩放图片
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val displayMetrics = resources.displayMetrics
        options.inSampleSize =
            calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels)
        options.inJustDecodeBounds = false
        mBitmap = BitmapFactory.decodeFile(imagePath, options)
        mCropImageView.setImageBitmap(mBitmap)

        findViewById<Button>(R.id.cropSave).setOnClickListener {
            mCropImageView.saveBitmapToFile(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                cropHeight,
                cropHeight,
                true
            )
        }
        findViewById<Button>(R.id.cropCancel).setOnClickListener {
            finish()
        }
    }

    override fun onBitmapSaveSuccess(file: File?) {
        Toast.makeText(this, "裁剪成功", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK, Intent().putExtra("resultPath", file?.absolutePath))
        finish()
    }

    override fun onBitmapSaveError(file: File?) {
        Toast.makeText(this, "裁剪失败", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            inSampleSize = if (width > height) {
                width / reqWidth
            } else {
                height / reqHeight
            }
        }
        return inSampleSize
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != mBitmap && !mBitmap!!.isRecycled) {
            mBitmap!!.recycle()
            mBitmap = null
        }
    }
}