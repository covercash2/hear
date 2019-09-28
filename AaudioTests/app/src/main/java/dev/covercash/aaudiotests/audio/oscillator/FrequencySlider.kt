package dev.covercash.aaudiotests.audio.oscillator

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.res.getFloatOrThrow
import dev.covercash.aaudiotests.R
import dev.covercash.aaudiotests.view.unit_slider.FloatRange
import dev.covercash.aaudiotests.view.unit_slider.Range
import dev.covercash.aaudiotests.view.unit_slider.UnitSlider

class FrequencySlider(ctx: Context, attrs: AttributeSet) : UnitSlider<Float>(ctx, attrs) {
    private val TAG = this.javaClass.simpleName

    override val range: Range<Float>

    init {
        range = FloatRange(min, max)
        setupViews(default, min, max)
    }

    override fun progressToData(progress: Int): Float =
        range.min + progress * range.increment

    override fun dataToProgress(data: Float): Int =
        ((data - range.min) / range.increment).toInt()

    override fun dataToString(data: Float): String =
        "%.1f".format(data)

    override fun parseStyleAttributes(typedArray: TypedArray): Triple<Float, Float, Float> {
        val data = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_default_value)
        val min = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_min)
        val max = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_max)

        return Triple(data, min, max)
    }
}