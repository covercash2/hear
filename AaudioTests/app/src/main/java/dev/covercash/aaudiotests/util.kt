package dev.covercash.aaudiotests

const val CONFIG_ERROR = "config error"

fun<T> defaultTag(clazz: Class<T>): String {
    return clazz.simpleName
}

inline fun <T: Any, R> T?.mapNotNull(fn: (T) -> R): R? {
    return this?.let(fn)
}

inline fun <T: Any> T?.mapNull(fn: () -> T?): T? {
    if (this == null) fn()
    return this
}

