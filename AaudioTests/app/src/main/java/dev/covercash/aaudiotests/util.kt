package dev.covercash.aaudiotests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.covercash.aaudiotests.jni.NativeAudio
import kotlin.Result.Companion.failure

const val CONFIG_ERROR = "config error"

const val DECIMAL_FLOAT_FORMAT = "%.1f"

fun <T> defaultTag(clazz: Class<T>): String {
    return clazz.simpleName
}

inline fun <T : Any, R> T?.mapNotNull(fn: (T) -> R): R? {
    return this?.let(fn)
}

inline fun <T : Any> T?.mapNull(fn: () -> T?): T? {
    if (this == null) fn()
    return this
}

abstract class NativeAudioViewModel(val engine: NativeAudio) : ViewModel()
// this factory is to create lifecycle aware models that can be
// passed a nativeAudio instance instead of creating it
class NativeAudioViewModelFactory(private val nativeAudio: NativeAudio) :
    ViewModelProvider.NewInstanceFactory() {
    // TODO no error checking
    // will throw if given a model that doesn't have the right constructor
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.superclass == NativeAudioViewModel::class.java) {
            return modelClass.getDeclaredConstructor(NativeAudio::class.java)
                .let {
                    it.isAccessible = true
                    it.newInstance(nativeAudio)
                }
        }
        throw IllegalArgumentException("native audio models must extend from NativeAudioViewModel")
    }
}

// TODO i'm keeping this here in case i decide to turn on Result returns
//fun <T: ViewModel?> constructNativeAudioViewModel(
//    modelClass: Class<T>,
//    nativeAudio: NativeAudio
//): Result<T> {
//    try {
//        return Result.success(modelClass
//            .getDeclaredConstructor(NativeAudio::class.java)
//            .let {
//                it.isAccessible = true
//                it.newInstance(nativeAudio)
//            }
//        )
//    } catch (e: Exception) {
//        when (e) {
//            is NoSuchMethodException -> return failure(e)
//        }
//    }
//}
