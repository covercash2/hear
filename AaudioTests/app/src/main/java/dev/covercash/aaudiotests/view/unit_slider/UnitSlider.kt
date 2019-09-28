package dev.covercash.aaudiotests.view.unit_slider

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.getFloatOrThrow
import androidx.core.content.res.getIntOrThrow
import dev.covercash.aaudiotests.R
import kotlinx.android.synthetic.main.unit_slider_view.view.*

class UnitSlider(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val TAG = this.javaClass.simpleName

    private val name: String
    private val unit: String

    var onValueChangedListener: (Float) -> Unit = {
        Log.d("UnitSlider", "value changed: $it")
    }
    var onFieldClick: () -> Unit = {
        Log.d(TAG, "field clicked")
    }
    var dataToString: ((Float) -> String)? = null
    var scaleData: (Int) -> Float = {
        Log.d(TAG, "scale data linear")
        it.toFloat()
    }

    var dataToInt: (Float) -> Int = { it.toInt() }

    init {
        val root = inflate(context, R.layout.unit_slider_view, this)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.UnitSlider)

        try {
            name = typedArray.getString(R.styleable.UnitSlider_us_name) ?: ""
            unit = typedArray.getString(R.styleable.UnitSlider_us_unit) ?: ""
            val data = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_default_value)
            val min = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_min)
            val max = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_max)

            setTitle(root)
            setUnit(unit)
            setupValueField(dataString(data))
            setupSlider(dataToInt(min), dataToInt(max), dataToInt(data))
        } finally {
            typedArray.recycle()
        }

    }

    fun setValue(position: Float) {
        unit_field.setText(dataString(position), TextView.BufferType.EDITABLE)
    }

    private fun dataString(data: Float): String {
        return if (dataToString == null) {
            data.toString()
        } else {
            dataToString!!(data)
        }
    }

    private fun setTitle(root: View) {
        root.findViewById<TextView>(R.id.unit_title)!!.apply {
            text = name
        }
    }

    private fun setUnit(text: String) {
        findViewById<TextView>(R.id.unit_label)!!.apply {
            this.text = text
        }
    }

    private fun setValueText(text: String) {
        unit_field!!.setText(text, TextView.BufferType.EDITABLE)
    }

    private fun setupSlider(min: Int, max: Int, defaultValue: Int) {
        unit_seek_bar!!.let {

            it.min = min * 10
            it.max = max * 10
            it.setProgress(defaultValue, false)

            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // nothing?
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // nothing?
                }

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.d(TAG, "progress: $progress")
                    if (fromUser) {
                        val scaledData = scaleData(progress)
                        setValueText(dataString(scaledData))
                        onValueChangedListener(scaledData)
                    }
                }
            })
        }

    }

    private fun setupValueField(defaultValue: String) {
        unit_field!!.apply {
            text = defaultValue
            setOnClickListener {
                onFieldClick()
            }
        }
    }

}