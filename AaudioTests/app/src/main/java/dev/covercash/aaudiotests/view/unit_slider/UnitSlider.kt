package dev.covercash.aaudiotests.view.unit_slider

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import dev.covercash.aaudiotests.R
import kotlinx.android.synthetic.main.unit_slider_view.view.*

const val SLIDER_MIN = 0
const val SLIDER_MAX = 10000

interface Range<D: Number> {
    val min: D
    val max: D
    val increment: D
    fun toSeekBarMaximum(): Int
}

data class FloatRange(
    override val min: Float,
    override val max: Float
): Range<Float> {
    override val increment = (max - min) / (SLIDER_MAX - SLIDER_MIN)

    override fun toSeekBarMaximum(): Int =
        ((max - min) / increment).toInt()
}

abstract class UnitSlider<D: Number>(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val TAG = this.javaClass.simpleName

    protected val name: String
    private val unit: String
    val default: D
    protected val min: D
    protected val max: D

    var onValueChangedListener: (D) -> Unit = {
        Log.d("UnitSlider", "value changed: $it")
    }
    var onFieldClick: () -> Unit = {
        Log.d(TAG, "field clicked")
    }

    val range: Range<D>

    abstract fun setupRange(): Range<D>

    abstract fun progressToData(progress: Int): D
    abstract fun dataToProgress(data: D): Int
    abstract fun dataToString(data: D): String
    abstract fun parseStyleAttributes(typedArray: TypedArray): Triple<D, D, D>

    init {
        val root = inflate(context, R.layout.unit_slider_view, this)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.UnitSlider)

        try {
            name = typedArray.getString(R.styleable.UnitSlider_us_name) ?: ""
            unit = typedArray.getString(R.styleable.UnitSlider_us_unit) ?: ""

            val (default, min, max) = this.parseStyleAttributes(typedArray)
            this.default = default
            this.min = min
            this.max = max

            range = setupRange()

            setTitle(root)
            setUnit(unit)
            setupViews(default, min, max)
        } finally {
            typedArray.recycle()
        }

    }

    protected fun setupViews(default: D, min: D, max: D) {
        setupValueField(dataToString(default))
        setupSlider(dataToProgress(min), dataToProgress(max), dataToProgress(default))
    }

    fun setValue(position: D) {
        unit_field!!.setText(dataToString(position), TextView.BufferType.EDITABLE)
        unit_seek_bar!!.setProgress(dataToProgress(position), true)
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

            Log.d(TAG, "default: $defaultValue, min: $min, max: $max")

            it.min = SLIDER_MIN
            it.max = SLIDER_MAX
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
                        val scaledData = progressToData(progress)
                        setValueText(dataToString(scaledData))
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