package com.burakhkahraman.numble

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class BackgroundMusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
