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
import androidx.core.content.res.getIntOrThrow
import dev.covercash.aaudiotests.R
import kotlinx.android.synthetic.main.unit_slider_view.view.*

class UnitSlider(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val name: String
    private val unit: String

    var onValueChangedListener: (Int) -> Unit = {
        Log.d("UnitSlider", "value changed: $it")
    }
    var dataToString: ((Int) -> String)? = null

    init {
        val root = inflate(context, R.layout.unit_slider_view, this)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.UnitSlider)

        try {
            name = typedArray.getString(R.styleable.UnitSlider_us_name) ?: ""
            unit = typedArray.getString(R.styleable.UnitSlider_us_unit) ?: ""
            val data = typedArray.getIntOrThrow(R.styleable.UnitSlider_us_default_value)
            val min = typedArray.getIntOrThrow(R.styleable.UnitSlider_us_min)
            val max = typedArray.getIntOrThrow(R.styleable.UnitSlider_us_max)

            setTitle(root)
            setUnit(unit)
            setupValueField(root, dataString(data))
            setupSlider(root, data, min, max)
        } finally {
            typedArray.recycle()
        }

    }

    fun setValue(position: Int) {
        setSliderPosition(position)
        unit_field.setText(dataString(position), TextView.BufferType.EDITABLE)
    }

    private fun dataString(data: Int): String {
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

    private fun setSliderPosition(position: Int) {
        findViewById<SeekBar>(R.id.unit_seek_bar)!!.apply {
            Log.d("UnitSlider", "setting progress value $position")
            setProgress(position, true)
        }
    }

    private fun setValueText(text: String) {
        findViewById<TextView>(R.id.unit_field)!!.apply {
            this.text = text
        }
    }

    private fun setupSlider(root: View, defaultValue: Int, min: Int, max: Int) {
        root.findViewById<SeekBar>(R.id.unit_seek_bar)!!.apply {

            this.min = min
            this.max = max

            this.setProgress(defaultValue, false)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
                    if (fromUser) {
                        setValueText(dataString(progress))
                    }
                    onValueChangedListener(progress)
                }
            })
        }

    }

    private fun setupValueField(root: View, defaultValue: String) {
        root.findViewById<TextView>(R.id.unit_field)!!.apply {
            this.text = defaultValue
            this.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // nothing?
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // nothing?
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("UnitSlider", "onTextChanged: $s")
                    s?.let {
                        it.filter(Char::isDigit)
                            .toString()
                            .toInt().let { intValue ->
                                setSliderPosition(intValue)
                            }
                    }
                }
            })
        }
    }

}