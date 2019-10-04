package dev.covercash.aaudiotests

import org.junit.Assert
import org.junit.Test

class NoteUnitTest {
    @Test
    fun testNoteToHalfStep() {
        val c0 = Note(Name.C, Modifier.Natural, 0)
        val expected0 = 0
        Assert.assertEquals(expected0, c0.toHalfSteps())

        val e0 = Note(Name.E, Modifier.Natural, 0)
        val expected1 = 4
        Assert.assertEquals(expected1, e0.toHalfSteps())

        val a4 = Note(Name.A, Modifier.Natural, 4)
        val expected2 = 57
        Assert.assertEquals(expected2, a4.toHalfSteps())

        val ab4 = Note(Name.A, Modifier.Flat, 4)
        val expected3 = 56
        Assert.assertEquals(expected3, ab4.toHalfSteps())
    }

    @Test
    fun testFrequencyToNote() {
        val freq0 = 440f
        val note = noteFromFrequency(freq0)
        Assert.assertEquals(Note(Name.A, Modifier.Natural, 4), note)
    }
}