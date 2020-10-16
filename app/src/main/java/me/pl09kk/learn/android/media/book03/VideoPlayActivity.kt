package me.pl09kk.learn.android.media.book03

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_play.*
import me.pl09kk.learn.android.media.R


class VideoPlayActivity  : AppCompatActivity(){

    private val uiHandler = Handler(Looper.getMainLooper())

    private var lastProgress = -1

    private val progressRunnable = object : Runnable {
        override fun run() {
            uiHandler.removeCallbacks(this)
            runOnUiThread {
                text1.text = formatSecond(videoView.currentPosition / 1000)
                text2.text = formatSecond(videoView.duration /1000 )
                seekBar.progress = videoView.currentPosition / 1000
            }
            uiHandler.postDelayed(this , 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sea_sky)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener {
            val width = getWindowWidth()
            val height = it.videoHeight * width/ it.videoWidth
            val layoutParams = videoView.layoutParams
            layoutParams.height = height
            layoutParams.width = width
            videoView.layoutParams = layoutParams

            videoView.seekTo(0)
            seekBar.max = videoView.duration / 1000
        }


        videoView.setOnCompletionListener {
            videoView.resume()
            uiHandler.removeCallbacks(progressRunnable)
            seekBar.progress = 0
        }

        playBtn.setOnClickListener {
            if(videoView.isPlaying){
                videoView.pause()
                uiHandler.removeCallbacks(progressRunnable)
                playBtn.text = "Play"
            }else{
                videoView.start()
                progressRunnable.run()
                playBtn.text = "Pause"
            }
        }

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    val videoProgress = videoView.duration * progress / (seekBar?.max?:1000)
                    videoView.seekTo(videoProgress)
                    text1.text = formatSecond(progress)
                    text2.text = formatSecond(seekBar?.max ?:1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                videoView.pause()
                uiHandler.removeCallbacks(progressRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.start()
                progressRunnable.run()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(lastProgress > 0){
            videoView.seekTo(lastProgress)
            videoView.start()
            lastProgress = -1
        }
    }


    override fun onStop() {
        super.onStop()
        if(videoView.isPlaying){
            videoView.pause()
            lastProgress = videoView.currentPosition
        }
        uiHandler.removeCallbacks(progressRunnable)
    }



    override fun onDestroy() {
        super.onDestroy()
        videoView.suspend()
    }

    private fun getWindowWidth( ) : Int{
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    private fun formatSecond(second : Int) : String{
        return String.format("%02d : %02d" , second / 60 , second % 60)
    }



}