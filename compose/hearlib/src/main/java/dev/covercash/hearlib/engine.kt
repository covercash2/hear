package dev.covercash.hearlib

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AudioEngine {
    private val nativeAudio = NativeAudio()

    var playing: Boolean
        get() = nativeAudio.isMasterPlaying()
        set(value) {
            nativeAudio.setMasterPlaying(value)
        }

    val oscillators = Oscillators(nativeAudio)

    fun start() =
        nativeAudio.startEngineNative()
    fun stop() = nativeAudio.stopEngineNative()
}

/**
 * a wrapper to treat native layer oscillators
 * as a collection.
 */
class Oscillators(
    private val nativeAudio: NativeAudio
) {
    // returns the total number of oscillators
    fun create(freq: Float, level: Float, shape: WaveShape): Int =
        nativeAudio.createOscillator(freq, level, shape.i)

    operator fun get(id: Int): Oscillator {
        return Oscillator(id, nativeAudio)
    }
}

class Oscillator(
    private val id: Int,
    private val nativeAudio: NativeAudio
) {
    var playing: Boolean
        get() = nativeAudio.isOscillatorPlaying(id)
        set(value) {
            nativeAudio.setOscillatorPlaying(id, value)
        }
    var frequency: Float
        get() =
            nativeAudio.getOscillatorFrequency(id)
        set(value) {
            nativeAudio.setOscillatorFrequency(id, value)
        }
    var level: Float
        get() = nativeAudio.getOscillatorLevel(id)
        set(value) {
            nativeAudio.setOscillatorLevel(id, value)
        }
    var waveShape: WaveShape by WaveShapeDelegate(id, nativeAudio)

    val frequencyMin: Float
        get() = nativeAudio.frequencyMin
    val frequencyMax: Float
        get() = nativeAudio.frequencyMax

    val levelMin: Float
        get() = nativeAudio.levelMin
    val levelMax: Float
        get() = nativeAudio.levelMax
}

typealias OscillatorId = Int

class OscillatorDelegate<T>(
    private val id: OscillatorId,
    private val engine: AudioEngine
) : ReadOnlyProperty<T, Oscillator> {
    override fun getValue(thisRef: T, property: KProperty<*>): Oscillator {
        return engine.oscillators[id]
    }
}

class WaveShapeDelegate<T>(
    private val oscillatorIndex: Int,
    private val nativeAudio: NativeAudio
) : ReadWriteProperty<T, WaveShape> {
    override fun getValue(thisRef: T, property: KProperty<*>): WaveShape {
        return WaveShape.values()[
                nativeAudio.getOscillatorWaveShape(oscillatorIndex)
        ]
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: WaveShape) {
        nativeAudio.setOscillatorWaveShape(oscillatorIndex, value.i)
    }
}
