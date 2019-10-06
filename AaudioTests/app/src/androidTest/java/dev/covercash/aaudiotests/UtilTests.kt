package dev.covercash.aaudiotests

import androidx.lifecycle.ViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.covercash.aaudiotests.audio.oscillator.OscillatorModel
import dev.covercash.aaudiotests.jni.NativeAudio
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilTests {

    class TestFailedException(msg: String): Throwable(msg)

    val nativeAudio = NativeAudio()

    @Test
    fun testNativeAudioViewModelFactory() {
        val factory = NativeAudioViewModelFactory(nativeAudio)
        val model: OscillatorModel = factory.create(OscillatorModel::class.java)

        try {
            // create bad model
            factory.create(ViewModel::class.java)
            throw TestFailedException("factory should throw when trying to build a bad model")
        } catch (e: IllegalArgumentException) {
            // pass
        }
    }
}