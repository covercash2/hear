package dev.covercash.aaudiotests.view.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayButton : FloatingActionButton {
    private val TAG = this.javaClass.simpleName

    enum class State {
        PLAYING, STOPPED
    }

    private var state = State.STOPPED

    var onClick: (View, State) -> Unit = { _, _ -> }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        setOnClickListener {
            toggleState()
            onClick(it, state)
        }
    }

    private fun toggleState() {
        state = when (state) {
            State.STOPPED -> {
                setPlaying()
                State.PLAYING
            }
            State.PLAYING -> {
                setStopped()
                State.STOPPED
            }
        }
    }

    private fun setPlaying() {
        isActivated = true
    }

    private fun setStopped() {
        isActivated = false
    }

}