package dev.covercash.aaudiotests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.audio.AudioEngineModel
import dev.covercash.aaudiotests.audio.oscillator.OscillatorFragment
import dev.covercash.aaudiotests.jni.NativeAudio
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private lateinit var model: AudioEngineModel

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
        // add fragment
        setMainFragment(OscillatorFragment())
    }

    private fun setupSynth(model: AudioEngineModel) {

        model.observePlaying(this, Observer { newValue ->
            play_button.playing = newValue
        })

        play_button!!.apply {
            playing = model.playing
            onClick = { _, newValue ->
                model.playing = newValue
            }
        }
    }

    private fun setupFilter() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders
            .of(this)[AudioEngineModel::class.java]

        model.startEngine()

        setupOscillator()
        setupSynth(model)

        bottom_nav.apply {
            setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_oscillator -> {
                        setupOscillator()
                        true
                    }
                    R.id.action_filter -> {
                        setupFilter()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroy() {
        model.stopEngine()
        super.onDestroy()
    }
}
