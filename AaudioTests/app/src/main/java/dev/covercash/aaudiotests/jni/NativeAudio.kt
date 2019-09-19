package dev.covercash.aaudiotests.jni

import android.util.Log

class NativeAudio {

    external fun toggleTone(isOn: Boolean)
    external fun startEngine()
    external fun stopEngine()
    external fun setFrequency(freq: Float)

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