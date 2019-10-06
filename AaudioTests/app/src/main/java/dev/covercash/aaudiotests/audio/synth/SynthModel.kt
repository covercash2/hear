package dev.covercash.aaudiotests.audio.synth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dev.covercash.aaudiotests.NativeAudioViewModel
import dev.covercash.aaudiotests.jni.NativeAudio

class SynthModel(engine: NativeAudio): NativeAudioViewModel(engine) {
    private val playingData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = engine.playing
        }
    }
    var playing: Boolean
        get() = engine.playing
        set(value) {
            Log.d(javaClass.simpleName, "setting play value: $value")
            engine.playing = value
            playingData.value = value
        }
}