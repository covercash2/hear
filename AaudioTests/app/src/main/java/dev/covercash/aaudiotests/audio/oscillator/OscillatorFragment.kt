package dev.covercash.aaudiotests.audio.oscillator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.covercash.aaudiotests.R
import dev.covercash.aaudiotests.jni.NativeAudio

class OscillatorFragment(val audioEngine: NativeAudio) : Fragment() {
    private fun setupFrequencySlider(parent: View) {
        val frequencySlider = parent.findViewById<SeekBar>(R.id.frequencySelector)

        frequencySlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // nothing?
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val newFreq = seekBar?.progress

                if (newFreq != null) {
                    audioEngine.frequency = newFreq.toFloat()
                }

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setFrequencyText(parent, "$progress")
            }
        })
    }

    private fun setFrequencyText(parent: View, text: String) {
        parent.findViewById<TextView>(R.id.frequency_text_view).apply {
            this.text = text
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.oscillator_fragment, container, false)

        setupFrequencySlider(view)

        return view
    }
}