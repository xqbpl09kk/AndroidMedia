package me.pl09kk.learn.android.media

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import me.pl09kk.learn.android.media.book02.MediaPlayerService

class MyApp : Application() {

    companion object{
        const val TAG = "MyApp"
        lateinit var instance : MyApp
    }


    override fun onCreate() {
        super.onCreate()
        Log.e(TAG  , "onCreate")
        startPlayService()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.e(TAG  , "attachBaseContext")
        instance = this
    }

    private fun startPlayService(){
        startService(Intent(this , MediaPlayerService::class.java))
    }
}