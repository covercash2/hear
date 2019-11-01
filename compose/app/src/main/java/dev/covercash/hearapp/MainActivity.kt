package dev.covercash.hearapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.*
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.NativeAudio

class MainActivity : AppCompatActivity() {
    private val logTag = this.javaClass.simpleName

    val nativeAudio = NativeAudio()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column {
                    Oscillator(
                        nativeAudio.frequencyValue(), nativeAudio.levelValue(),
                        onFrequencyChanged = {
                            Log.d(this.logTag(), "new frequency: $it")
                            nativeAudio.frequency = it
                        },
                        onAmplitudeChanged = {
                            Log.d(this.logTag(), "new amplitude: $it")
                            nativeAudio.level = it
                        }
                    )
                }
            }
        }

        nativeAudio.startEngine()
    }

    override fun onDestroy() {
        nativeAudio.stopEngine()
        super.onDestroy()
    }
}

@Preview
@Composable
fun DefaultPreview() {
    Column {
        Oscillator(
            Value(.4f), Value(.7f)
        )
    }
}

