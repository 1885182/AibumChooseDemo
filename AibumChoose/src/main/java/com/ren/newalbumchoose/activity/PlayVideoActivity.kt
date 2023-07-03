package com.ren.newalbumchoose.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.ren.newalbumchoose.R
import com.ren.newalbumchoose.video.UniversalMediaController
import com.ren.newalbumchoose.video.UniversalVideoView

/**
 * 播放视频
 */
class PlayVideoActivity : AppCompatActivity(), UniversalVideoView.VideoViewCallback {

    companion object {
        const val URL_VIDEO = "video"
    }

    private var mSeekPosition = 0
    private var cachedHeight = 0
    private var isFullscreen = false
    private val SEEK_POSITION_KEY = "SEEK_POSITION_KEY"

    //    private var VIDEO_URL = "http://minio.bjsszt.cn/jczl/91F7A433EDB241258070653535D2BB3A.mp4"
    private var VIDEO_URL = ""

    private val TAG = "PlayVideoActivity"

    private var mVideoView: UniversalVideoView? = null
    private var mMediaController: UniversalMediaController? = null
    private var mVideoLayout: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_play_video)

        mVideoLayout = findViewById(R.id.mVideoLayout)
        mVideoView = findViewById(R.id.mVideoView)
        mMediaController = findViewById(R.id.mMediaController)
        mVideoView?.setMediaController(mMediaController!!)

        mVideoView?.setVideoViewCallback(this)

        VIDEO_URL = intent.getStringExtra(URL_VIDEO)
            ?: ""
        setVideoAreaSize()
    }


    override fun onStop() {
        super.onStop()
        finish()
    }

    /**
     * 置视频区域大小
     */
    private fun setVideoAreaSize() {
        if (VIDEO_URL.isNullOrEmpty()) {
            Toast.makeText(this, "视频地址解析失败", Toast.LENGTH_LONG).show()
        }

        mVideoLayout?.post(Runnable {
            val width: Int = mVideoLayout?.getWidth() ?: 0
            cachedHeight = (width * 405f / 720f).toInt()
            val videoLayoutParams: ViewGroup.LayoutParams = mVideoLayout?.getLayoutParams()!!
            videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            videoLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            mVideoLayout?.layoutParams = videoLayoutParams
            mVideoView?.setVideoPath(VIDEO_URL)
            mVideoView?.requestFocus()
            if (mSeekPosition > 0) {
                mVideoView?.seekTo(mSeekPosition)
            }
            mVideoView?.start()
            mMediaController?.setTitle("")
        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SEEK_POSITION_KEY, mSeekPosition)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        super.onRestoreInstanceState(outState)
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY)

    }


    override fun onScaleChange(isFullscreen: Boolean) {
        this.isFullscreen = isFullscreen
        if (isFullscreen) {
            val layoutParams: ViewGroup.LayoutParams = mVideoLayout?.getLayoutParams()!!
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            mVideoLayout?.setLayoutParams(layoutParams)
        } else {
            val layoutParams: ViewGroup.LayoutParams = mVideoLayout?.getLayoutParams()!!
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = cachedHeight
            mVideoLayout?.setLayoutParams(layoutParams)
        }
        switchTitleBar(!isFullscreen)
    }

    private fun switchTitleBar(show: Boolean) {
        val supportActionBar: ActionBar? = supportActionBar
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show()
            } else {
                supportActionBar.hide()
            }
        }
    }

    override fun onPause(mediaPlayer: MediaPlayer?) {
     }

    override fun onStart(mediaPlayer: MediaPlayer?) {
     }

    override fun onBufferingStart(mediaPlayer: MediaPlayer?) {

    }

    override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {

    }

    override fun onBackPressed() {
        if (isFullscreen) {
            mVideoView?.setFullscreen(false)
        } else {
            super.onBackPressed()
        }
    }


}