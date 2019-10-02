package dev.covercash.aaudiotests.view.unit_slider

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.core.content.res.getFloatOrThrow
import dev.covercash.aaudiotests.R

class FloatSlider(ctx: Context, attrs: AttributeSet) : UnitSlider<Float>(ctx, attrs) {

    override fun setupRange(): Range<Float> = FloatRange(min, max)

    override fun dataToProgress(data: Float): Int =
        ((data - range.min) / range.increment).toInt()

    override fun progressToData(progress: Int): Float =
        range.min + progress * range.increment

    override fun dataToString(data: Float): String =
        "%.1f".format(data)

    override fun parseStyleAttributes(typedArray: TypedArray): Triple<Float, Float, Float> {
        val data = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_default_value)
        val min = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_min)
        val max = typedArray.getFloatOrThrow(R.styleable.UnitSlider_us_max)

        return Triple(data, min, max)
    }
}