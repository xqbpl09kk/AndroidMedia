package me.pl09kk.learn.android.media.book02

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import me.pl09kk.learn.android.media.MyApp
import me.pl09kk.learn.android.media.R

class MediaPlayerService : Service() {

    private enum class PlaybackState {
        Idle, Initialized, Preparing, Prepared, Started, Stopped, Paused, Completed, End, Error
    }

    inner class MyBinder : Binder() {
        fun getService(): MediaPlayerService {
            return this@MediaPlayerService
        }
    }

    private val binder = MyBinder()

    private val mediaPlayer = MediaPlayer.create(MyApp.instance, R.raw.forest_in_norway)

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY_COMPATIBILITY
    }

    private var mState = PlaybackState.Idle

    init {
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            mState = PlaybackState.Error
            false
        }
        mediaPlayer.setOnPreparedListener {
            mState = PlaybackState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            stopSelf()
            mState = PlaybackState.Completed
        }
        mState = PlaybackState.Prepared
    }


    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    fun stop() {
        mediaPlayer.stop()
        mState = PlaybackState.Stopped
    }

    fun togglePlay(): Boolean {
        return if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mState = PlaybackState.Paused
            false
        } else {
            if (mState == PlaybackState.Stopped) {
                mediaPlayer.prepare()
            }
            mediaPlayer.start()
            mState = PlaybackState.Started
            true
        }
    }

    fun isStarted(): Boolean {
        return mState == PlaybackState.Started
    }

    fun getProgress(): Int {
        if (mState == PlaybackState.Prepared
            || mState == PlaybackState.Paused
            || mState == PlaybackState.Started
        ) {
            return mediaPlayer.currentPosition * 1000 / mediaPlayer.duration
        } else {
            return 0
        }
    }

    fun getCurrent(): String {
        return if (avaliable()) {
            val current = mediaPlayer.currentPosition / 1000
            "${String.format("%02d", current / 60)}:${String.format("%02d", current % 60)}"
        } else {
            "00:00"
        }
    }


    fun getDuration(): String {
        return if (avaliable()) {
            val duration = mediaPlayer.duration / 1000
            "${String.format("%02d", duration / 60)}:${String.format("%02d", duration % 60)}"
        } else {
            "00:00"
        }
    }

    fun seekTo(position: Int) {
        if (avaliable()) {
            mediaPlayer.seekTo(position * mediaPlayer.duration / 1000)
        }
    }


    fun release() {
        mediaPlayer.release()
        mState = PlaybackState.End
    }


    private fun avaliable(): Boolean {
        return mState == PlaybackState.Prepared
                || mState == PlaybackState.Paused
                || mState == PlaybackState.Started
    }

}