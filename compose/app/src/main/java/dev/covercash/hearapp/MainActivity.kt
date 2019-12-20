package dev.covercash.hearapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.layout.Align
import androidx.ui.layout.Column
import androidx.ui.layout.FlexRow
import androidx.ui.material.*
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.AudioEngine
import dev.covercash.hearlib.NativeAudio
import dev.covercash.hearlib.WaveShape
import kotlin.properties.Delegates

@Model
data class MainModel(
    var playing: Playing,
    var oscillatorN: Int
)

fun AudioEngine.model() = MainModel(
    Playing(playing), 0
)

@Model
data class Playing(var value: Boolean) {
    fun toggle() {
        value = !value
    }
}

class MainActivity : AppCompatActivity() {
    private val logTag = this.javaClass.simpleName

    private val audioEngine = AudioEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainContent(audioEngine)
            }
        }
        audioEngine.start()
    }

    override fun onDestroy() {
        audioEngine.stop()
        super.onDestroy()
    }
}

@Composable
fun MainContent(audioEngine: AudioEngine) {
    val model = audioEngine.model()

    Oscillators(audioEngine, model)

    with(model) {
        Align(Alignment.BottomRight) {
            FloatingActionButton(
                if (playing.value) "||"
                else "|>",
                onClick = {
                    playing.toggle()
                    audioEngine.playing = playing.value
                }
            )
        }
    }
}

@Composable
fun Oscillators(audioEngine: AudioEngine, model: MainModel) {
    Column {
        (0 until model.oscillatorN).forEach {
            with(audioEngine.oscillators[it]) {
                Oscillator(
                    Playing(playing),
                    frequencyValue(), levelValue(),
                    onPlayToggled = { newPlaying ->
                        playing = newPlaying
                    },
                    onFrequencyChanged = { newFreq ->
                        Log.d(this.logTag(), "new frequency: $it")
                        frequency = newFreq
                    },
                    onLevelChanged = { newLevel ->
                        Log.d(this.logTag(), "new amplitude: $it")
                        level = newLevel
                    }
                )
            }
        }

        // add oscillator
        FlexRow {
            this.expanded(.2f) {
                Button("+", style = OutlinedButtonStyle(),
                    onClick = {
                        model.oscillatorN = audioEngine.oscillators
                            .create(440f, .5f, WaveShape.SINE)
                    })
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    Column {
        Oscillator(
            Playing(false),
            Value(.4f), Value(.7f)
        )
    }
}

