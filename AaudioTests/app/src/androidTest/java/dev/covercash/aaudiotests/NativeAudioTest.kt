package dev.covercash.aaudiotests

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.covercash.aaudiotests.jni.NativeAudio
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeAudioTest {
    private val nativeAudio = NativeAudio()

    @Test
    fun testFrequencyGetterAndSetter() {
        val min = nativeAudio.frequencyMin
        val max = nativeAudio.frequencyMax

        // test default value
        val freq0 = nativeAudio.frequency
        // sanity check setter
        nativeAudio.frequency = freq0
        Assert.assertEquals(freq0, nativeAudio.frequency)

        // sanity check known good value
        val freq1 = 440f
        nativeAudio.frequency = freq1
        Assert.assertEquals(freq1, nativeAudio.frequency)

        // values less than min should
        // clamp to min
        val badFreq0 = 0f
        nativeAudio.frequency = badFreq0
        Assert.assertEquals(min, nativeAudio.frequency)

        // values greater than max should
        // clamp to max
        val badFreq1 = 100_000f
        nativeAudio.frequency = badFreq1
        Assert.assertEquals(max, nativeAudio.frequency)

        // check bounds
        nativeAudio.frequency = min
        Assert.assertEquals(min, nativeAudio.frequency)

        nativeAudio.frequency = max
        Assert.assertEquals(max, nativeAudio.frequency)

    }

    @Test
    fun testAmplitudeGetterAndSetter() {
        val min = nativeAudio.levelMin
        val max = nativeAudio.levelMax

        // test default value
        val level0 = nativeAudio.level
        // sanity check setter
        nativeAudio.level = level0
        Assert.assertEquals(level0, nativeAudio.level)

        // sanity test good value
        val level1 = .3f
        nativeAudio.level = level1
        Assert.assertEquals(level1, nativeAudio.level)

        // values greater than the max should be
        // clamped to max
        val badLevel1 = 2.0f
        nativeAudio.level = badLevel1
        Assert.assertEquals(max, nativeAudio.level)

        // values less than the minimum should be
        // clamped to min
        val badLevel0 = -1.0f
        nativeAudio.level = badLevel0
        Assert.assertEquals(min, nativeAudio.level)

        // test bounds
        nativeAudio.level = max
        Assert.assertEquals(max, nativeAudio.level)

        nativeAudio.level = min
        Assert.assertEquals(min, nativeAudio.level)
    }

    @Test
    fun testPlayToggle() {
        // sanity checks
        val v0 = nativeAudio.playing
        Assert.assertEquals(v0, nativeAudio.playing)
        nativeAudio.playing = v0
        Assert.assertEquals(v0, nativeAudio.playing)

        // exhaustive tests
        val v1 = !v0
        nativeAudio.playing = !nativeAudio.playing
        Assert.assertEquals(v1, nativeAudio.playing)

        val v2 = false
        nativeAudio.playing = false
        Assert.assertEquals(v2, nativeAudio.playing)

        val v3 = true
        nativeAudio.playing = v3
        Assert.assertEquals(v3, nativeAudio.playing)
    }
}

