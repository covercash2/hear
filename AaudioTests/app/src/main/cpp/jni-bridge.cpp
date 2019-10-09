#include "AudioEngine.h"
#include <android/input.h>
#include <android/log.h>
#include <jni.h>
#include <string>

static auto audioEngine = new AudioEngine(new Oscillator(FREQUENCY_A, 0.0, WaveShape::kSine));

extern "C" {

JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_constFrequencyMin(
        JNIEnv *env,
        jobject obj) {
    return FREQUENCY_MIN;
}
JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_constFrequencyMax(
        JNIEnv *env,
        jobject obj) {
    return FREQUENCY_MAX;
}
JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_constLevelMin(
        JNIEnv *env,
        jobject obj) {
    return LEVEL_MIN;
}
JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_constLevelMax(
        JNIEnv *env,
        jobject obj) {
    return LEVEL_MAX;
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_setLevelNative(
        JNIEnv *env,
        jobject obj,
        jfloat level) {
    audioEngine->getOscillator()->setLevel(level);
}

JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_getLevelNative(
        JNIEnv *env,
        jobject obj) {
    return audioEngine->getOscillator()->getLevel();
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_setFrequencyNative(
        JNIEnv *env,
        jobject obj,
        jfloat freq) {
    audioEngine->getOscillator()->setFrequency(freq, audioEngine->sample_rate_);
}

JNIEXPORT float JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_getFrequencyNative(
        JNIEnv *env,
        jobject obj,
        jfloat freq) {
    return audioEngine->getOscillator()->getFrequency();
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_toggleToneNative(
        JNIEnv *env,
        jobject obj,
        jboolean isOn) {
    audioEngine->setToneOn(isOn);
}

JNIEXPORT int JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_getWaveShapeNative(
        JNIEnv *env,
        jobject obj) {
    WaveShape shape = audioEngine->getOscillator()->getWaveShape();
    return static_cast<int>(shape);
}

JNIEXPORT void JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_setWaveShapeNative(
        JNIEnv *env,
        jobject obj,
        jint waveIndex) {
    audioEngine->getOscillator()->setWaveShape(WaveShape(waveIndex));
}

JNIEXPORT bool JNICALL
Java_dev_covercash_aaudiotests_jni_NativeAudio_isPlayingNative(
        JNIEnv *env,
        jobject obj) {
    return audioEngine->getOscillator()->isWaveOn();
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

