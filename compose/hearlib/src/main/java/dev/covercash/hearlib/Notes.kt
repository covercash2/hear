package dev.covercash.hearlib

import java.lang.IndexOutOfBoundsException
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.roundToInt

val A4 = Note(
    Name.A,
    Modifier.Natural,
    4
)

// constants taken from https://pages.mtu.edu/~suits/NoteFreqCalcs.html
const val freq0 = 16.352597831287414 // C0

fun generateAllNotes(): Sequence<Note> =
    generateSequence(
        Note(
            Name.C,
            Modifier.Natural,
            0
        )
    ) {
        it.next()
    }.takeWhile {
        it.octave < 9
    }

fun frequencyFromHalfSteps(halfSteps: Double): Double =
    2.0.pow(halfSteps / 12.0).times(freq0)

fun halfStepsFromFrequency(freq: Double): Double =
    12.times(log(freq / freq0, 2.0))

fun noteFromHalfSteps(halfSteps: Int): Note {
    val octave: Int = halfSteps.div(12)
    val n = halfSteps.rem(12)

    val (name, modifier) = when (n) {
        0 -> Pair(
            Name.C,
            Modifier.Natural
        )
        1 -> Pair(
            Name.C,
            Modifier.Sharp
        )
        2 -> Pair(
            Name.D,
            Modifier.Natural
        )
        3 -> Pair(
            Name.E,
            Modifier.Flat
        )
        4 -> Pair(
            Name.E,
            Modifier.Natural
        )
        5 -> Pair(
            Name.F,
            Modifier.Natural
        )
        6 -> Pair(
            Name.F,
            Modifier.Sharp
        )
        7 -> Pair(
            Name.G,
            Modifier.Natural
        )
        8 -> Pair(
            Name.G,
            Modifier.Sharp
        )
        9 -> Pair(
            Name.A,
            Modifier.Natural
        )
        10 -> Pair(
            Name.B,
            Modifier.Flat
        )
        11 -> Pair(
            Name.B,
            Modifier.Natural
        )
        else -> throw IndexOutOfBoundsException("unexpected value outside of (0..11)")
    }

    return Note(name, modifier, octave)
}

fun noteFromFrequency(freq: Float): Note =
    noteFromHalfSteps(
        halfStepsFromFrequency(
            freq.toDouble()
        ).roundToInt()
    )

data class Note(val value: Name, val modifier: Modifier, val octave: Int) {

    fun next(): Note =
        when (value) {
            Name.A -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.B,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.B,
                    Modifier.Flat,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.A,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.B -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.C,
                    Modifier.Sharp,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.C,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.B,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.C -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.D,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.C,
                    Modifier.Sharp,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.C,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.D -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.E,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.E,
                    Modifier.Flat,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.D,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.E -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.F,
                    Modifier.Sharp,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.F,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.E,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.F -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.G,
                    Modifier.Natural,
                    this.octave
                )
                Modifier.Natural -> Note(
                    Name.F,
                    Modifier.Sharp,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.F,
                    Modifier.Natural,
                    this.octave
                )
            }
            Name.G -> when (modifier) {
                Modifier.Sharp -> Note(
                    Name.A,
                    Modifier.Natural,
                    octave + 1
                )
                Modifier.Natural -> Note(
                    Name.G,
                    Modifier.Sharp,
                    this.octave
                )
                Modifier.Flat -> Note(
                    Name.G,
                    Modifier.Natural,
                    this.octave
                )
            }
        }

    fun toHalfSteps(): Int =
        octave.times(12)
            .plus(value.toHalfSteps())
            .plus(modifier.toHalfSteps())

    fun toFrequency(): Double =
        frequencyFromHalfSteps(this.toHalfSteps().toDouble())

    override fun toString(): String = value.toString() + modifier.toString() + octave
}

enum class Name {
    A, B, C, D, E, F, G;

    fun toHalfSteps(): Int = when (this) {
        C -> 0
        D -> 2
        E -> 4
        F -> 5
        G -> 7
        A -> 9
        B -> 11
    }

    override fun toString(): String = when (this) {
        A -> "A"
        B -> "B"
        C -> "C"
        D -> "D"
        E -> "E"
        F -> "F"
        G -> "G"
    }
}

enum class Modifier {
    Flat, Natural, Sharp;

    fun toHalfSteps(): Int = when (this) {
        Sharp -> 1
        Natural -> 0
        Flat -> -1
    }

    override fun toString(): String = when (this) {
        Sharp -> "#"
        Natural -> ""
        Flat -> "b"
    }
}
