package dev.covercash.hearapp

import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.memo
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.foundation.selection.Toggleable
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.tooling.preview.Preview
import dev.covercash.hearlib.*

fun Oscillator.frequencyValue(): Value =
    Value(frequency, frequencyMin, frequencyMax)

fun Oscillator.levelValue(): Value =
    Value(level, levelMin, levelMax)

@Composable
fun Oscillator(
    playing: Playing,
    frequency: Value,
    level: Value,
    onPlayToggled: ((Boolean) -> Unit)? = null,
    onFrequencyChanged: ((Float) -> Unit)? = null,
    onLevelChanged: ((Float) -> Unit)? = null
) {
    Column(Spacing(16.dp)) {
        Row {
            Button(
                if (playing.value) "|>" else "||",
                onClick = {
                    playing.toggle()
                    onPlayToggled?.invoke(playing.value)
                }
            )
            Text(noteFromFrequency(frequency.value).toString())
        }
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
        Playing(false),
        Value(.5f), Value(.7f)
    )
}