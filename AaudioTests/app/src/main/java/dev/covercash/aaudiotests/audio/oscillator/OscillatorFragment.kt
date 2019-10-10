package dev.covercash.aaudiotests.audio.oscillator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.DECIMAL_FLOAT_FORMAT
import dev.covercash.aaudiotests.Note
import dev.covercash.aaudiotests.R
import dev.covercash.aaudiotests.audio.AudioEngineModel
import dev.covercash.aaudiotests.jni.WaveShape
import dev.covercash.aaudiotests.noteFromFrequency
import dev.covercash.aaudiotests.view.NotePickerDialog
import dev.covercash.aaudiotests.view.unit_slider.FloatRange
import dev.covercash.aaudiotests.view.unit_slider.SLIDER_MAX
import dev.covercash.aaudiotests.view.unit_slider.SLIDER_MIN
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.oscillator_fragment.*
import kotlinx.android.synthetic.main.oscillator_fragment.view.*

class OscillatorFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    @SuppressLint("SetTextI18n")
    private fun setUpViews(view: View) {
        val model = activity?.run {
            ViewModelProviders
                .of(this)
                .get(AudioEngineModel::class.java)
        } ?: throw IllegalStateException("could not get activity for fragment")

        val validateFrequency: (Float) -> Float = {
            when {
                it > model.frequencyMax -> {
                    Log.w(
                        TAG, "bad frequency provided",
                        IllegalArgumentException("frequency is greater than max")
                    )
                    model.frequencyMax
                }
                it < model.frequencyMin -> {
                    Log.w(
                        TAG, "bad frequency provided",
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

        val frequencyRange = FloatRange(model.frequencyMin, model.frequencyMax)
        val levelRange = FloatRange(0f, 1f)

        model.observeFrequency(this, Observer { freq ->
            Log.d(TAG, "observed new frequency: $freq")
            frequency_seek_bar.progress = frequencyRange.positionOf(freq)
            frequency_value_text.text = DECIMAL_FLOAT_FORMAT.format(freq)
            setNoteName(freq)
        })

        model.observeLevel(this, Observer { level ->
            level_seek_bar.progress = levelRange.positionOf(level)
            level_value_text.text = DECIMAL_FLOAT_FORMAT.format(level)
        })

        model.observeWaveShape(this, Observer { shape ->
            wave_shape_spinner.setSelection(shape.i)
        })

        val setFrequency: (Float) -> Unit = {
            val newFreq = validateFrequency(it)
            model.frequency = newFreq
        }

        view.wave_shape_spinner!!.apply {
            val shapes = WaveShape.values()
            ArrayAdapter(context, android.R.layout.simple_spinner_item, shapes)
                .let {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    adapter = it
                }

            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    model.waveShape = shapes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "wave shape spinner: nothing selected")
                }
            }
        }

        view.frequency_seek_bar!!.apply {
            progress = frequencyRange.positionOf(model.frequency)

            min = SLIDER_MIN
            max = SLIDER_MAX

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) { /* nothing? */ }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { /* nothing? */ }

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val scaledData = frequencyRange.valueOf(progress)
                        view.frequency_value_text.text = DECIMAL_FLOAT_FORMAT.format(scaledData)
                        model.frequency = scaledData
                    }
                }
            })
        }

        view.frequency_value_text.setOnClickListener {
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

        view.level_seek_bar!!.apply {

            min = SLIDER_MIN
            max = SLIDER_MAX

            progress = levelRange.positionOf(model.level)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) { /* nothing? */ }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { /* nothing? */ }

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val scaledData = levelRange.valueOf(progress)
                        view.level_value_text.text = DECIMAL_FLOAT_FORMAT.format(scaledData)
                        model.level = scaledData
                    }
                }
            })
        }

        view.level_value_text!!.text = DECIMAL_FLOAT_FORMAT.format(model.level)

        view.note_name!!.apply {
            text = noteFromFrequency(model.frequency).toString()
            setOnClickListener {
                NotePickerDialog(noteFromFrequency(model.frequency)) { note ->
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