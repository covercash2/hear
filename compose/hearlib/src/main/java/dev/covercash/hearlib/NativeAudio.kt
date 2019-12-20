package dev.covercash.hearlib

import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NativeAudio {
    external fun isMasterPlaying(): Boolean
    external fun setMasterPlaying(isOn: Boolean)
    external fun startEngineNative()
    external fun stopEngineNative()

    external fun constFrequencyMin(): Float
    external fun constFrequencyMax(): Float
    external fun constLevelMin(): Float
    external fun constLevelMax(): Float

    // Oscillator

    // returns the total number of oscillators
    external fun createOscillator(
        freq: Float, level: Float, waveShape: Int
    ): Int

    external fun isOscillatorPlaying(id: Int): Boolean
    external fun setOscillatorPlaying(id: Int, b: Boolean)

    external fun getOscillatorFrequency(id: Int): Float
    external fun setOscillatorFrequency(id: Int, value: Float)

    external fun getOscillatorLevel(id: Int): Float
    external fun setOscillatorLevel(id: Int, level: Float)

    external fun getOscillatorWaveShape(id: Int): Int
    external fun setOscillatorWaveShape(id: Int, i: Int)
    // end Oscillator

    external fun getRcNative(): Float
    external fun setRcNative(rc: Float)

    val frequencyMin = constFrequencyMin()
    val frequencyMax = constFrequencyMax()
    val levelMin = constLevelMin()
    val levelMax = constLevelMax()

    var rc: Float
        get() = getRcNative()
        set(value) {
            setRcNative(value)
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