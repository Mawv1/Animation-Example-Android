package com.example.customviewexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val barsWaveView = findViewById<BarsWaveView>(R.id.bars_wave_view)
        val playStopButton = findViewById<AnimatedPlayStopButton>(R.id.play_stop_button)

        playStopButton.setOnClickListener {
            if (barsWaveView.isRunning) {
                barsWaveView.stopAnimation()
                playStopButton.setPlaying(false)
            } else {
                barsWaveView.startAnimation()
                playStopButton.setPlaying(true)
            }
        }
//        playStopButton.setPlaying(true)
    }
}