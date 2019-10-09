package dev.covercash.aaudiotests.audio

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dev.covercash.aaudiotests.jni.NativeAudio
import dev.covercash.aaudiotests.jni.WaveShape

class AudioEngineModel : ViewModel() {
    private val engine = NativeAudio()

    val frequencyMin
        get() = engine.frequencyMin
    val frequencyMax
        get() = engine.frequencyMax

    init {
        Log.d(this.javaClass.simpleName, "AudioEngineModel initialized")
    }

    private val playingData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val frequencyData: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }
    private val levelData: MutableLiveData<Float> by lazy {
        // TODO initialize value here?
        MutableLiveData<Float>()
    }
    private val waveShapeData: MutableLiveData<WaveShape> by lazy {
        MutableLiveData<WaveShape>()
    }

    var playing: Boolean
        get() = engine.playing
        set(value) {
            engine.playing = value
            playingData.value = value
        }

    var frequency: Float
        get() = engine.frequency
        set(value) {
            engine.frequency = value
            frequencyData.value = value
        }

    var level: Float
        get() = engine.level
        set(value) {
            engine.level = value
            levelData.value = value
        }

    var waveShape: WaveShape
        get() = engine.waveShape
        set(value) {
            engine.waveShape = value
            waveShapeData.value = value
        }

    fun observeFrequency(owner: LifecycleOwner, observer: Observer<Float>) =
        frequencyData.observe(owner, observer)

    fun observeLevel(owner: LifecycleOwner, observer: Observer<Float>) =
        levelData.observe(owner, observer)

    fun observePlaying(owner: LifecycleOwner, observer: Observer<Boolean>) =
        playingData.observe(owner, observer)


    fun observeWaveShape(owner: LifecycleOwner, observer: Observer<WaveShape>) =
        waveShapeData.observe(owner, observer)

    fun startEngine() = engine.startEngine()
    fun stopEngine() = engine.stopEngine()
}