package dev.covercash.aaudiotests.jni

import android.util.Log
import dev.covercash.aaudiotests.defaultTag

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

    private external fun constFrequencyMin(): Float
    private external fun constFrequencyMax(): Float
    private external fun constLevelMin(): Float
    private external fun constLevelMax(): Float

    val frequencyMin = constFrequencyMin()
    val frequencyMax = constFrequencyMax()
    val levelMin = constLevelMin()
    val levelMax = constLevelMax()

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