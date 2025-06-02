package com.example.customviewexample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val barsWaveView = findViewById<BarsWaveView>(R.id.bars_wave_view)
        val playStopButton = findViewById<AnimatedPlayStopButton>(R.id.play_stop_button)
        val nextScreenButton = findViewById<Button>(R.id.next_screen_button)

        nextScreenButton.setOnClickListener {
            val intent = Intent(this, SecondScreen::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        playStopButton.setOnClickListener {
            if (barsWaveView.isRunning) {
                barsWaveView.stopAnimation()
                playStopButton.setPlaying(true)
            } else {
                barsWaveView.startAnimation()
                playStopButton.setPlaying(false)
            }
        }

        barsWaveView.stopAnimation()
        playStopButton.setPlaying(true)
    }
}