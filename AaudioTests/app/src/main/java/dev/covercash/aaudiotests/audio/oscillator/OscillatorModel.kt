package dev.covercash.aaudiotests.audio.oscillator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OscillatorModel : ViewModel() {
    val frequency: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
    val playing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}