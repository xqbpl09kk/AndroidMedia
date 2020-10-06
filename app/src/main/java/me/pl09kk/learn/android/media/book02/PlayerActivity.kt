package me.pl09kk.learn.android.media.book02

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player.*
import me.pl09kk.learn.android.media.R

class PlayerActivity : AppCompatActivity() {
    private val TAG = "PlayerActivity"

    private val uiHandler = Handler(Looper.getMainLooper())
    private val seekBarUpdater = object : Runnable {
        override fun run() {
            runOnUiThread {
                durationText.text = playService?.getDuration()
                playedText.text = playService?.getCurrent()
                seekBar.progress = playService?.getProgress()?:0
            }
            uiHandler.postDelayed(this , 200)
        }
    }

    private var playService :MediaPlayerService ?= null

    private val serviceCon = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            playService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playService = (service as? MediaPlayerService.MyBinder)?.getService()
            if(playService?.isStarted() == true){
                uiHandler.post(seekBarUpdater)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val bound = bindService(Intent(this , MediaPlayerService::class.java) ,serviceCon , BIND_AUTO_CREATE)
        Log.e(TAG , "bind result $bound")
        playButton.setOnClickListener {
            val ret = playService?.togglePlay()
            if(ret == true){
                uiHandler.post(seekBarUpdater)
            }else{
                uiHandler.removeCallbacks(seekBarUpdater)
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var userProgress = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser)
                    userProgress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                uiHandler.removeCallbacks(seekBarUpdater)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playService?.seekTo(userProgress)
                if(playService?.isStarted() == true){
                    uiHandler.post(seekBarUpdater)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(playService?.isStarted() == true){
            uiHandler.post(seekBarUpdater)
        }
    }

    override fun onStop() {
        super.onStop()
        uiHandler.removeCallbacks(seekBarUpdater)
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceCon)
    }
}