package dev.covercash.aaudiotests

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.audio.oscillator.OscillatorFragment
import dev.covercash.aaudiotests.audio.oscillator.OscillatorModel
import dev.covercash.aaudiotests.audio.synth.SynthModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.jni.WaveShape
import dev.covercash.aaudiotests.view.NotePickerDialog
import dev.covercash.aaudiotests.view.button.PlayButton
import dev.covercash.aaudiotests.view.unit_slider.UnitDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.oscillator_fragment.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private val nativeAudio = NativeAudio()

    private fun setMainFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction().also { transaction ->
                transaction.replace(
                    R.id.main_fragment_container,
                    fragment
                )
            }.commit()
    }

    private fun setupOscillator() {
        val oscillatorModel = ViewModelProviders
            .of(this, NativeAudioViewModelFactory(nativeAudio))
            .get(OscillatorModel::class.java)
        // add fragment
        setMainFragment(OscillatorFragment(oscillatorModel))
    }

    private fun setupSynth() {
        val synthModel = ViewModelProviders
            .of(this, NativeAudioViewModelFactory(nativeAudio))
            .get(SynthModel::class.java)

        play_button!!.apply {
            playing = synthModel.playing
            onClick = { _, newValue ->
                synthModel.playing = newValue
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nativeAudio.startEngine()
        setupOscillator()
        setupSynth()

        bottom_nav.apply {
            setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_oscillator -> {
                        setupOscillator()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroy() {
        nativeAudio.stopEngine()
        super.onDestroy()
    }
}
