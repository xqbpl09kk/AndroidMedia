package me.pl09kk.learn.android.media

import android.app.Application
import android.content.Context
import android.util.Log

class MyApp : Application() {

    companion object{
        const val TAG = "MyApp"
        lateinit var instance : MyApp
    }


    override fun onCreate() {
        super.onCreate()
        Log.e(TAG  , "onCreate")
        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.e(TAG  , "attachBaseContext")
        instance = this
    }
}