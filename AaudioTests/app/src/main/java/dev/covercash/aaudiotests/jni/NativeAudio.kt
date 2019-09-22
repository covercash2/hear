package dev.covercash.aaudiotests.jni

import android.util.Log
import dev.covercash.aaudiotests.defaultTag

class NativeAudio {

    private val classTag = defaultTag(this.javaClass)

    private external fun toggleToneNative(isOn: Boolean)
    private external fun startEngineNative()
    private external fun stopEngineNative()
    private external fun setFrequencyNative(freq: Float)

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

    fun setFrequency(freq: Float) {
        Log.d(classTag, "setFrequency($freq)")

        return setFrequencyNative(freq)
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