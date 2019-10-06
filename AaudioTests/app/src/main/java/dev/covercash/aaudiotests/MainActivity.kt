package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.audio.oscillator.OscillatorModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.jni.WaveShape
import dev.covercash.aaudiotests.view.NotePickerDialog
import dev.covercash.aaudiotests.view.button.PlayButton
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private fun setupOscillator() {
        val model = ViewModelProviders.of(this).get(OscillatorModel::class.java)

        val validateFrequency: (Float) -> Float = {
            when {
                it > model.frequencyMax -> {
                    Log.w(TAG, "bad frequency provided",
                        IllegalArgumentException("frequency is greater than max")
                    )
                    model.frequencyMax
                }
                it < model.frequencyMin -> {
                    Log.w(TAG, "bad frequency provided",
                        IllegalArgumentException("frequency is less than min")
                    )
                    model.frequencyMin
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

        model.observePlaying(this, Observer<Boolean> {
            Log.d(TAG, "observed to play boolean: $it")
            tone_button.playing = it
        })

        model.observeFrequency(this, Observer { freq ->
            Log.d(TAG, "observed new frequency: $freq")
            frequency_slider.setValue(freq)
            setNoteName(freq)
        })

        val setFrequency: (Float) -> Unit = {
            val newFreq = validateFrequency(it)
            model.setFrequency(newFreq)
        }

        note_name!!.apply {
            setOnClickListener {
                NotePickerDialog(noteFromFrequency(model.getFrequency())) { note ->
                    val freq = note.toFrequency().toFloat()
                    setFrequency(freq)
                }
                    .show(supportFragmentManager, "Note Picker Dialog")
            }
        }

        wave_shape_spinner!!.apply {
            val shapes = WaveShape.values()
            ArrayAdapter(context, android.R.layout.simple_spinner_item, shapes)
                .let {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    adapter = it
                }

            this.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    model.setWaveShape(shapes[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "wave shape spinner: nothing selected")
                }
            }
        }

        frequency_slider!!.apply {
            setValue(model.getFrequency())
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
            setValue(model.getLevel())
            onValueChangedListener = { newLevel ->
                model.setLevel(newLevel)
            }
        }

        tone_button!!.apply {
            playing = model.isPlaying()
            onClick = { _, playing ->
                model.setPlaying(playing)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NativeAudio().startEngine()
        setupOscillator()
    }

    override fun onDestroy() {
        // TODO is this ok?
        NativeAudio().stopEngine()
        super.onDestroy()
    }
}
