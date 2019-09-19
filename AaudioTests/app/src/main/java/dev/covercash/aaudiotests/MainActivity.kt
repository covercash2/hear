package dev.covercash.aaudiotests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import dev.covercash.aaudiotests.jni.NativeAudio
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val nativeAudio = NativeAudio()

    private fun setupFrequencySlider() {
        val frequencySlider = findViewById<SeekBar>(R.id.frequencySelector)

        frequencySlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // nothing?
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val newFreq = seekBar?.progress

                if (newFreq != null) {
                    nativeAudio.setFrequency(newFreq.toFloat())
                }

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setFrequencyText("$progress.0 Hz")
            }
        })
    }

    private fun setFrequencyText(text: String) {
        findViewById<TextView>(R.id.frequencyTextView).apply {
            this.text = text
        }
    }

    private fun setupToneButton() {
        findViewById<ToggleButton>(R.id.toneButton).apply {
            setOnCheckedChangeListener { _, isChecked ->
                nativeAudio.toggleTone(isChecked)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nativeAudio.startEngine()

        setupFrequencySlider()
        setupToneButton()
    }

    override fun onDestroy() {
        nativeAudio.stopEngine()
        super.onDestroy()
    }
}
