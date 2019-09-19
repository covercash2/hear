package dev.covercash.aaudiotests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.oscillator.OscillatorFragment
import dev.covercash.aaudiotests.view.UnitSlider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val nativeAudio = NativeAudio()

    private fun setupOscillator() {
        findViewById<UnitSlider>(R.id.frequency_slider)!!.apply {
            onValueChangedListener = { newValue ->
                nativeAudio.setFrequency(newValue)
            }
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

        setupOscillator()
        setupToneButton()
    }

    override fun onDestroy() {
        nativeAudio.stopEngine()
        super.onDestroy()
    }
}
