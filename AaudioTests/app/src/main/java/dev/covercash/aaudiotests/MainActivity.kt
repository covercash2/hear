package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.audio.oscillator.OscillatorModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.view.unit_slider.UnitSlider
import kotlinx.android.synthetic.main.activity_main.*

const val defaultFrequency = 440f // Hz

class MainActivity : AppCompatActivity() {

    private val nativeAudio = NativeAudio()

    private fun setupOscillator() {

        val model = ViewModelProviders.of(this)[OscillatorModel::class.java]
        model.getFrequency().observe(this, Observer<Float> { frequency ->
            frequency_slider.setValue(frequency.times(10f).toInt())
        })

        findViewById<UnitSlider>(R.id.frequency_slider)!!.apply {
            model.frequency.value = nativeAudio.frequency
            onValueChangedListener = { newValue ->
                nativeAudio.frequency = (newValue.toFloat() / 10f)
            }
            dataToString = {
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
