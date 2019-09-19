#include "AudioEngine.h"
#include <android/input.h>
#include <android/log.h>
#include <jni.h>
#include <string>

static AudioEngine *audioEngine = new AudioEngine();

extern "C" {

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_setFrequency(
        JNIEnv *env,
        jobject obj,
        jfloat freq) {
    audioEngine->setFrequency(freq);
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_toggleTone(
        JNIEnv *env,
        jobject obj,
        jboolean isOn) {
    audioEngine->setToneOn(isOn);
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_startEngine(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->start();
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_stopEngine(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->stop();
}

}

