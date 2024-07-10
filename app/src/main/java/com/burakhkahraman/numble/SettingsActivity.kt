package com.burakhkahraman.numble


import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.burakhkahraman.numble.R
import com.burakhkahraman.numble.utils.LocaleHelper

class SettingsActivity : AppCompatActivity() {

    private lateinit var volumeSeekBar: SeekBar
    private lateinit var volumeDownButton: ImageButton
    private lateinit var volumeUpButton: ImageButton
    private lateinit var turkishFlagButton: ImageButton
    private lateinit var englishFlagButton: ImageButton
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        volumeDownButton = findViewById(R.id.volumeDownButton)
        volumeUpButton = findViewById(R.id.volumeUpButton)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        setupVolumeControl()

    }

    private fun setupVolumeControl() {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeekBar.max = maxVolume
        volumeSeekBar.progress = currentVolume

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        volumeDownButton.setOnClickListener {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (volume > 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume - 1, 0)
                volumeSeekBar.progress = volume - 1
            }
        }

        volumeUpButton.setOnClickListener {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (volume < maxVolume) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume + 1, 0)
                volumeSeekBar.progress = volume + 1
            }
        }
    }


}