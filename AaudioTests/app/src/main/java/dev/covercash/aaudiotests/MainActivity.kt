package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.audio.oscillator.OscillatorModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.view.NotePickerDialog
import dev.covercash.aaudiotests.view.button.PlayButton
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private val nativeAudio = NativeAudio()

    private fun setupOscillator() {
        val validateFrequency: (Float) -> Float = {
            when {
                it > nativeAudio.frequencyMax -> {
                    Log.w(TAG, "bad frequency provided",
                        IllegalArgumentException("frequency is greater than max")
                    )
                    nativeAudio.frequencyMax
                }
                it < nativeAudio.frequencyMin -> {
                    Log.w(TAG, "bad frequency provided",
                        IllegalArgumentException("frequency is less than min")
                    )
                    nativeAudio.frequencyMin
                }
                else -> it
            }
        }
        val validateStringFrequency: (String) -> Result<Float> = { s: String ->
            when (val v = s.toFloatOrNull()) {
                null -> failure(IllegalArgumentException("unable to parse float from string: $s"))
                else -> success(validateFrequency(v))
            }
        }
        fun setNoteName(note: Note) {
            note_name!!.text = note.toString()
        }
        fun setNoteName(freq: Float) {
            val note = noteFromFrequency(freq)
            setNoteName(note)
        }

        val model = ViewModelProviders.of(this).get(OscillatorModel::class.java)

        model.playing.observe(this, Observer<Boolean> {
            Log.d(TAG, "observed to play boolean: $it")
            tone_button.playing = it
        })

        model.frequency.observe(this, Observer<Float> { freq ->
            Log.d(TAG, "observed new frequency: $freq")
            frequency_slider.setValue(freq)
            setNoteName(freq)
        })

        val setFrequency: (Float) -> Unit = {
            val newFreq = validateFrequency(it)
            model.frequency.value = newFreq
            nativeAudio.frequency = newFreq
        }

        note_name!!.apply {
            setOnClickListener {
                NotePickerDialog(noteFromFrequency(nativeAudio.frequency)) { note ->
                    val freq = note.toFrequency().toFloat()
                    setFrequency(freq)
                }
                    .show(supportFragmentManager, "Note Picker Dialog")
            }
        }

        frequency_slider!!.apply {
            onValueChangedListener = { freq ->
                setFrequency(freq)
            }
            onFieldClick = {
                UnitDialog("frequency") { dialog, s ->
                    validateStringFrequency(s)
                        .onSuccess { f ->
                            setFrequency(f)
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
            onValueChangedListener = { newValue ->
                nativeAudio.level = newValue
            }
        }

    }

    private fun setupToneButton() {
        tone_button!!.apply {
            playing = nativeAudio.playing
            onClick = { _, playing ->
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
