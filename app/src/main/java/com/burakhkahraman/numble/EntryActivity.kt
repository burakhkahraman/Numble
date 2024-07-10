package com.burakhkahraman.numble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        startService(Intent(this, BackgroundMusicService::class.java))

        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val howToPlayButton = findViewById<Button>(R.id.howToPlayButton)
        howToPlayButton.setOnClickListener {
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
