package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.ui.core.*
import androidx.ui.layout.*
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.NativeAudio

fun NativeAudio.frequencyValue(): Value =
    Value(frequency, frequencyMin, frequencyMax)

fun NativeAudio.levelValue(): Value =
    Value(level, levelMin, levelMax)

@Composable
fun Oscillator(
    frequency: Value,
    level: Value,
    onFrequencyChanged: ((Float) -> Unit)? = null,
    onLevelChanged: ((Float) -> Unit)? = null
) {
    Column(Spacing(16.dp)) {
        LabeledSlider(frequency, "frequency", "Hz", onFrequencyChanged)
        LabeledSlider(level, "level", "dB", onLevelChanged)
    }
}

@Composable
fun LabeledSlider(
    value: Value,
    label: String,
    units: String,
    onChange: ((percent: Float) -> Unit)?
) {
    Column {
        FlexRow {
            this.expanded(2f) {
                Text(label)
                Text("%9.1f".format(value.value))
                Text(units)
            }
        }
        Slider(
            state = value,
            onChange = onChange
        )
    }
}

@Preview
@Composable
fun preview() {
    Oscillator(
        Value(.5f), Value(.7f)
    )
}