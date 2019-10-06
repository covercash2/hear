package dev.covercash.aaudiotests.view.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayButton : FloatingActionButton {
    private val TAG = this.javaClass.simpleName

    var playing: Boolean
        get() = isActivated
        set(value) {
            isActivated = value
        }

    var onClick: (View, Boolean) -> Unit = { _, _ ->  }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        setOnClickListener {
            playing = !playing
            onClick(it, playing)
        }
    }
}