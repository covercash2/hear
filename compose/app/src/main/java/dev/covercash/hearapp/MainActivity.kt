package dev.covercash.hearapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.layout.Column
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.NativeAudio

@Model
data class Playing(var value: Boolean) {
    fun toggle() { value = !value }
}

class MainActivity : AppCompatActivity() {
    private val logTag = this.javaClass.simpleName

    val nativeAudio = NativeAudio()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val playing = Playing(nativeAudio.playing)
            MaterialTheme {
                Column {
                    Oscillator(
                        nativeAudio.frequencyValue(), nativeAudio.levelValue(),
                        onFrequencyChanged = {
                            Log.d(this.logTag(), "new frequency: $it")
                            nativeAudio.frequency = it
                        },
                        onLevelChanged = {
                            Log.d(this.logTag(), "new amplitude: $it")
                            nativeAudio.level = it
                        }
                    )

                    FloatingActionButton(
                        if (playing.value) "||"
                        else "|>",
                        onClick = {
                            playing.toggle()
                            nativeAudio.playing = playing.value
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

