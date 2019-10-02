package dev.covercash.aaudiotests

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.covercash.aaudiotests.jni.NativeAudio
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeAudioTest {
    @Test
    fun testFrequencyGettersAndSetters() {
        val nativeAudio = NativeAudio()

        val freq0 = nativeAudio.frequency

        Assert.assertEquals(freq0, 440f)

        val freq1 = 1000f
        nativeAudio.frequency = freq1

        Assert.assertEquals(freq1, nativeAudio.frequency)
    }
}

