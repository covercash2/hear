package dev.covercash.toolbox

interface Range<D: Number> {
    val min: D
    val max: D
}

data class FloatRange(
    override val min: Float,
    override val max: Float
): Range<Float> {
    private val increment = (max - min)

    fun positionOf(v: Float): Int =
        ((v - min) / increment).toInt()

    fun valueOf(position: Int): Float =
        min + position * increment
}