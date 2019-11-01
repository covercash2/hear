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
    Column(Spacing(16.dp)) {
        LabeledSlider(frequency, "frequency", "Hz", onFrequencyChanged)
//        LabeledSlider(amplitude.percent, "amplitude", "dB", onAmplitudeChanged)
    }
}

@Composable
fun LabeledSlider(
    value: Value,
    label: String,
    units: String,
    onChange: ((percent: Float) -> Unit)?
) {
    FlexRow {
        this.inflexible {
            Text(label)
            Text("%.2f".format(value.value))
            Text(units)
        }
        this.expanded(5f) {
            TestSlider(
                name = label,
                state = value,
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