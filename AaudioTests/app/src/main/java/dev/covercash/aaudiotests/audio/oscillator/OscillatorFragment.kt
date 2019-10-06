package dev.covercash.aaudiotests.audio.oscillator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dev.covercash.aaudiotests.Note
import dev.covercash.aaudiotests.R
import dev.covercash.aaudiotests.jni.WaveShape
import dev.covercash.aaudiotests.noteFromFrequency
import dev.covercash.aaudiotests.view.NotePickerDialog
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.oscillator_fragment.*
import kotlinx.android.synthetic.main.oscillator_fragment.view.*

class OscillatorFragment(val model: OscillatorModel) : Fragment() {
    private val TAG = this.javaClass.simpleName

    private fun setUpViews(view: View) {
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
                null -> Result.failure(IllegalArgumentException("unable to parse float from string: $s"))
                else -> Result.success(validateFrequency(v))
            }
        }
        fun setNoteName(note: Note) {
            note_name!!.text = note.toString()
        }
        fun setNoteName(freq: Float) {
            val note = noteFromFrequency(freq)
            setNoteName(note)
        }

        val fragManager = fragmentManager!!

        model.observeFrequency(this, Observer { freq ->
            Log.d(TAG, "observed new frequency: $freq")
            frequency_slider.setValue(freq)
            setNoteName(freq)
        })

        model.observeLevel(this, Observer { level ->
            level_slider.setValue(level)
        })

        model.observeWaveShape(this, Observer { shape ->
            wave_shape_spinner.setSelection(shape.i)
        })

        val setFrequency: (Float) -> Unit = {
            val newFreq = validateFrequency(it)
            model.setFrequency(newFreq)
        }

        view.wave_shape_spinner!!.apply {
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

        view.frequency_slider!!.apply {
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
                    .show(fragManager, "Frequency Dialog")
            }
        }

        view.level_slider!!.apply {
            setValue(model.getLevel())
            onValueChangedListener = { newLevel ->
                model.setLevel(newLevel)
            }
        }

        view.note_name!!.apply {
            text = noteFromFrequency(model.getFrequency()).toString()
            setOnClickListener {
                NotePickerDialog(noteFromFrequency(model.getFrequency())) { note ->
                    val freq = note.toFrequency().toFloat()
                    setFrequency(freq)
                }
                    .show(fragManager, "Note Picker Dialog")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.oscillator_fragment, container, false)

        setUpViews(view)

        return view
    }
}