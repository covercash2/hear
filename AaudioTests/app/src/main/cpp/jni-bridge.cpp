#include "AudioEngine.h"
#include <android/input.h>
#include <android/log.h>
#include <jni.h>
#include <string>

static auto audioEngine = new AudioEngine();

extern "C" {

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_setFrequencyNative(
        JNIEnv *env,
        jobject obj,
        jfloat freq) {
    audioEngine->getOscillator()->setFrequency(freq);
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_getFrequencyNative(
        JNIEnv *env,
        jobject obj,
        jfloat freq) {
    audioEngine->getOscillator()->getFrequency();
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_toggleToneNative(
        JNIEnv *env,
        jobject obj,
        jboolean isOn) {
    audioEngine->setToneOn(isOn);
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_startEngineNative(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->start();
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_stopEngineNative(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->stop();
}

}

