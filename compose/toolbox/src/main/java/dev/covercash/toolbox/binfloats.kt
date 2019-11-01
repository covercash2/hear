package dev.covercash.toolbox

/**
 * Compose uses a binary format for floats
 * to facilitate inline classes.
 * This file is a collection of utils to help
 * with debuggins
*/

/**
 * Packs two Float values into one Long value for use in inline classes.
 */
fun packFloats(val1: Float, val2: Float): Long {
    val v1 = val1.toBits().toLong()
    val v2 = val2.toBits().toLong()
    return v1.shl(32) or (v2 and 0xFFFFFFFF)
}

/**
 * Unpacks the first Float value in [packFloats] from its returned Long.
 */
fun unpackFloat1(value: Long): Float {
    return Float.fromBits(value.shr(32).toInt())
}

/**
 * Unpacks the second Float value in [packFloats] from its returned Long.
 */
fun unpackFloat2(value: Long): Float {
    return Float.fromBits(value.and(0xFFFFFFFF).toInt())
}

fun unpackFloats(value: Long): Pair<Float, Float> {
    return Pair(unpackFloat1(value), unpackFloat2(value))
}

fun Long.unpack(): Pair<Float, Float> = unpackFloats(this)