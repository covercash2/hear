package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.view.unit_slider.UnitSlider

const val defaultFrequency = 440f // Hz

class MainActivity : AppCompatActivity() {

    private val nativeAudio = NativeAudio()

    private fun setupOscillator() {
        findViewById<UnitSlider>(R.id.frequency_slider)!!.apply {
            data.onValueChangedListener = { newValue ->
                Log.d("MainActivity", "value changed: $newValue")
                nativeAudio.setFrequency(newValue.toFloat() / 10f)
            }
            data.dataToString = {
                it.toFloat().div(10f).toString()
            }
        }
    }

    private fun setupToneButton() {
        findViewById<ToggleButton>(R.id.toneButton)!!.apply {
            setOnCheckedChangeListener { _, isChecked ->
                Log.d("MainActivity", "checked: $isChecked")
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
