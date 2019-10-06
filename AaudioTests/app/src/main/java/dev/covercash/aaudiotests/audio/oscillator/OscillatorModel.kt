package dev.covercash.aaudiotests.audio.oscillator

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dev.covercash.aaudiotests.NativeAudioViewModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.jni.WaveShape

class OscillatorModel(engine: NativeAudio) : NativeAudioViewModel(engine) {
    val frequencyMin
        get() = engine.frequencyMin
    val frequencyMax
        get() = engine.frequencyMax

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
        engine.frequency = freq
        this.frequency.value = freq
    }
    fun getFrequency(): Float {
        // TODO return model value or native value?
        return engine.frequency
    }
    fun observeFrequency(owner: LifecycleOwner, observer: Observer<Float>) =
        frequency.observe(owner, observer)

    fun setLevel(level: Float) {
        engine.level = level
        this.level.value = level
    }
    fun getLevel(): Float = engine.level
    fun observeLevel(owner: LifecycleOwner, observer: Observer<Float>) =
        level.observe(owner, observer)

    fun setPlaying(playing: Boolean) {
        engine.playing = playing
        this.playing.value = playing
    }
    fun isPlaying(): Boolean = engine.playing
    fun observePlaying(owner: LifecycleOwner, observer: Observer<Boolean>) =
        playing.observe(owner, observer)

    fun setWaveShape(shape: WaveShape) {
        engine.setWaveShape(shape)
        this.waveShape.value = shape
    }
    fun getWaveShape(): WaveShape? =
        this.waveShape.value
    fun observeWaveShape(owner: LifecycleOwner, observer: Observer<WaveShape>) =
        waveShape.observe(owner, observer)
}