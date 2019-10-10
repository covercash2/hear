package dev.covercash.aaudiotests.jni

import android.util.Log
import dev.covercash.aaudiotests.defaultTag

// 'i' represents an index in the corresponding C++ enum
enum class WaveShape(val i: Int) {
    SAW(0),
    SINE(1),
    SQUARE(2),
    TRIANGLE(3),
}

class NativeAudio {

    private val classTag = defaultTag(this.javaClass)

    private external fun isPlayingNative(): Boolean
    private external fun toggleToneNative(isOn: Boolean)
    private external fun startEngineNative()
    private external fun stopEngineNative()
    private external fun setFrequencyNative(freq: Float)
    private external fun getFrequencyNative(): Float
    private external fun setLevelNative(level: Float)
    private external fun getLevelNative(): Float
    private external fun getRcNative(): Float
    private external fun setRcNative(rc: Float)
    private external fun getWaveShapeNative(): Int
    private external fun setWaveShapeNative(i: Int)

    private external fun constFrequencyMin(): Float
    private external fun constFrequencyMax(): Float
    private external fun constLevelMin(): Float
    private external fun constLevelMax(): Float

    val frequencyMin = constFrequencyMin()
    val frequencyMax = constFrequencyMax()
    val levelMin = constLevelMin()
    val levelMax = constLevelMax()
    var rc: Float
        get() = getRcNative()
        set(value) { setRcNative(value) }

    var frequency: Float
        get() {
            return getFrequencyNative()
        }
        set(value) {
            setFrequencyNative(value)
        }

    var level: Float
        get() = getLevelNative()
        set(value) = setLevelNative(value)

    var playing: Boolean
        get() = isPlayingNative()
        set(value) {
            toggleToneNative(value)
        }

    var waveShape: WaveShape
        get() = WaveShape.values()[getWaveShapeNative()]
        set(value) {
            setWaveShapeNative(value.i)
        }

    fun startEngine() {
        return startEngineNative()
    }

    fun stopEngine() {
        return stopEngineNative()
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
            try {
                System.loadLibrary("native-lib")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("NativeAudio", "unable to load native lib", e)
            }
        }
    }
}