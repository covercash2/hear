package dev.covercash.aaudiotests.jni

import android.util.Log
import dev.covercash.aaudiotests.defaultTag

class NativeAudio {

    private val classTag = defaultTag(this.javaClass)

    private external fun toggleToneNative(isOn: Boolean)
    private external fun startEngineNative()
    private external fun stopEngineNative()
    private external fun setFrequencyNative(freq: Float)
    private external fun getFrequencyNative(): Float

    var frequency: Float
        get() {
            Log.d(classTag, "get frequency")
            return getFrequencyNative()
        }
        set(value) {
            Log.d(classTag, "setting frequency: $value")
            setFrequencyNative(value)
        }

    fun toggleTone(isOn: Boolean) {
        Log.d(classTag, "toggleTone")

        return toggleToneNative(isOn)
    }

    fun startEngine() {
        Log.d(classTag, "startEngine")

        return startEngineNative()
    }

    fun stopEngine() {
        Log.d(classTag, "stopEngine")

        return stopEngineNative()
    }

    companion object {
        init {
            try {
                System.loadLibrary("native-lib")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("NativeAudio", "unable to load native lib", e)
            }
        }
    }
}