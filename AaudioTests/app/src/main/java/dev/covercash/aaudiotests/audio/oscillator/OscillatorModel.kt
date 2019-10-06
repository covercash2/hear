package dev.covercash.aaudiotests.audio.oscillator

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.jni.WaveShape

class OscillatorModel : ViewModel() {
    private val nativeAudio = NativeAudio()

    val frequencyMin
        get() = nativeAudio.frequencyMin
    val frequencyMax
        get() = nativeAudio.frequencyMax

    private val playing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val frequency: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
    private val level: MutableLiveData<Float> by lazy {
        // TODO initialize value here?
        MutableLiveData<Float>()
    }
    private val waveShape: MutableLiveData<WaveShape> by lazy {
        MutableLiveData<WaveShape>()
    }

    fun setFrequency(freq: Float) {
        nativeAudio.frequency = freq
        this.frequency.value = freq
    }
    fun getFrequency(): Float {
        // TODO return model value or native value?
        return nativeAudio.frequency
    }
    fun observeFrequency(owner: LifecycleOwner, observer: Observer<Float>) =
        frequency.observe(owner, observer)

    fun setLevel(level: Float) {
        nativeAudio.level = level
        this.level.value = level
    }
    fun getLevel(): Float = nativeAudio.level
    fun observeLevel(owner: LifecycleOwner, observer: Observer<Float>) =
        level.observe(owner, observer)

    fun setPlaying(playing: Boolean) {
        nativeAudio.playing = playing
        this.playing.value = playing
    }
    fun isPlaying(): Boolean = nativeAudio.playing
    fun observePlaying(owner: LifecycleOwner, observer: Observer<Boolean>) =
        playing.observe(owner, observer)

    fun setWaveShape(shape: WaveShape) {
        nativeAudio.setWaveShape(shape)
        this.waveShape.value = shape
    }
    fun getWaveShape(): WaveShape? =
        this.waveShape.value
    fun observeWaveShape(owner: LifecycleOwner, observer: Observer<WaveShape>) =
        waveShape.observe(owner, observer)
}