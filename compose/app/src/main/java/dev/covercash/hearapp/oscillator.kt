package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.layout.*
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.NativeAudio

@Model
class OscillatorState(
    var frequency: Float,
    var amplitude: Float
)

class Value(
    val value: Float,
    private val min: Float = 0f,
    private val max: Float = 1f
) {
    val percent: Float
        get() =
            value.minus(min).div(max.minus(min))
}

fun NativeAudio.frequencyValue(): Value =
    Value(frequency, frequencyMin, frequencyMax)

fun NativeAudio.levelValue(): Value =
    Value(level, levelMin, levelMax)

@Composable
fun Oscillator(
    frequency: Value,
    level: Value,
    onFrequencyChanged: ((Float) -> Unit)? = null,
    onAmplitudeChanged: ((Float) -> Unit)? = null
) {
    Column(Spacing(8.dp)) {
        LabeledSlider(frequency.percent, "frequency", "Hz", onFrequencyChanged)
//        LabeledSlider(amplitude.percent, "amplitude", "dB", onAmplitudeChanged)
    }
}

@Composable
fun LabeledSlider(
    percent: Float,
    label: String,
    units: String,
    onChange: ((percent: Float) -> Unit)?
) {
    FlexRow {
        this.flexible(.2f) {
            Text(label)
            Text("%.2f".format(percent))
            Text(units)
        }
        this.expanded(.2f) {
            TestSlider(
                name = label,
                state = SliderState(percent),
                onChange = onChange
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    Oscillator(
        Value(.5f), Value(.7f)
    )
}