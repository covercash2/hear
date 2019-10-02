package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.view.button.PlayButton
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

const val FREQUENCY_MIN = 10.0f
const val FREQUENCY_MAX = 20_000f

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private val nativeAudio = NativeAudio()

    private fun setupOscillator() {
        val validateFrequency: (Float) -> Result<Float> = {
            when {
                it > FREQUENCY_MAX || it < FREQUENCY_MIN ->
                    failure(
                        IndexOutOfBoundsException(
                            "value is out of range: $it\n\tfrequency between $FREQUENCY_MIN and $FREQUENCY_MAX required"
                        )
                    )
                else -> success(it)
            }
        }
        val validateStringFrequency: (String) -> Result<Float> = { s: String ->
            when (val v = s.toFloatOrNull()) {
                null -> failure(IllegalArgumentException("unable to parse float from string: $s"))
                else -> validateFrequency(v)
            }
        }

        frequency_slider!!.apply {
            setValue(nativeAudio.frequency)
            onValueChangedListener = { newValue ->
                Log.d(TAG, "value changed: $newValue")
                validateFrequency(newValue)
                    .onFailure {
                        Log.e(TAG, "unable to validate frequency", it)
                    }
                    .onSuccess {
                        nativeAudio.frequency = newValue
                    }
            }
            onFieldClick = {
                UnitDialog("frequency") { dialog, s ->
                    validateStringFrequency(s)
                        .onSuccess { f ->
                            nativeAudio.frequency = f
                            setValue(f)
                            dialog.dismiss()
                        }
                        .onFailure {
                            Log.d(TAG, "unable to validate dialog input")
                        }
                }
                    .show(supportFragmentManager, "Frequency Dialog")
            }
        }

        level_slider!!.apply {
            this.setValue(nativeAudio.level)
            onValueChangedListener = { newValue ->
                nativeAudio.level = newValue
            }
        }

    }

    private fun setupToneButton() {
        tone_button!!.apply {
            playing = nativeAudio.playing
            onClick =  { _, playing ->
                nativeAudio.playing = playing
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
