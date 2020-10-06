package me.pl09kk.learn.android.media.book02

import android.media.AudioPlaybackConfiguration
import android.media.MediaPlayer
import me.pl09kk.learn.android.media.MyApp
import me.pl09kk.learn.android.media.R

class MediaPlayerDemo {

    enum class PlaybackState{
        Idle , Initialized , Preparing , Prepared , Started , Stopped , Paused , Completed , End , Error
    }

    private val mediaPlayer = MediaPlayer.create(MyApp.instance , R.raw.forest_in_norway)




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
            mState = PlaybackState.Completed
        }
        mState = PlaybackState.Prepared
    }

    fun togglePlay(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            mState = PlaybackState.Paused
        }else{
            if(mState == PlaybackState.Stopped){
                mediaPlayer.prepare()
            }
            mediaPlayer.start()
            mState = PlaybackState.Started
        }
    }

    fun stop(){
        mediaPlayer.stop()
        mState = PlaybackState.Stopped
    }


    fun release(){
        mediaPlayer.release()
        mState = PlaybackState.End
    }

}