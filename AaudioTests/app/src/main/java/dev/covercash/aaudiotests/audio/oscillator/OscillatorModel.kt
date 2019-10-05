package dev.covercash.aaudiotests.audio.oscillator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OscillatorModel : ViewModel() {
    val playing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val frequency: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
    val level: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
}