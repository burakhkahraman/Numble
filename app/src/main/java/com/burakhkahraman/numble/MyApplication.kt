package com.burakhkahraman.numble


import android.app.Application
import android.content.Intent

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startService(Intent(applicationContext, BackgroundMusicService::class.java))
    }

    override fun onTerminate() {
        stopService(Intent(applicationContext, BackgroundMusicService::class.java))
        super.onTerminate()
    }
}