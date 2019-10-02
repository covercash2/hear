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

    var frequency: Float
        get() {
            Log.d(classTag, "get frequency")
            return getFrequencyNative()
        }
        set(value) {
            Log.d(classTag, "setting frequency: $value")
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

    fun toggleTone(isOn: Boolean) {
        return toggleToneNative(isOn)
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
//            try {
//                System.loadLibrary("native-lib")
//            } catch (e: UnsatisfiedLinkError) {
//                Log.e("NativeAudio", "unable to load native lib", e)
//            }
        }
    }
}