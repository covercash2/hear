#include "AudioEngine.h"
#include <android/input.h>
#include <android/log.h>
#include <jni.h>
#include <string>

static auto audioEngine = new AudioEngine();

extern "C" {

JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_constFrequencyMin(
        JNIEnv *env,
        jobject obj) {
    return FREQUENCY_MIN;
}
JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_constFrequencyMax(
        JNIEnv *env,
        jobject obj) {
    return FREQUENCY_MAX;
}
JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_constLevelMin(
        JNIEnv *env,
        jobject obj) {
    return LEVEL_MIN;
}
JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_constLevelMax(
        JNIEnv *env,
        jobject obj) {
    return LEVEL_MAX;
}

JNIEXPORT int JNICALL
Java_dev_covercash_hearlib_NativeAudio_createOscillator(
        JNIEnv *env,
        jobject obj,
        jfloat freq,
        jfloat level,
        jint waveShape) {
    return audioEngine->createOscillator(freq, level, WaveShape(waveShape));
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setOscillatorLevel(
        JNIEnv *env,
        jobject obj,
        jint id,
        jfloat level) {
    audioEngine->getOscillator(id)->setLevel(level);
}

JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_getOscillatorLevel(
        JNIEnv *env,
        jobject obj,
        jint id) {
    return audioEngine->getOscillator(id)->getLevel();
}

JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_getOscillatorFrequency(
        JNIEnv *env,
        jobject obj,
        jint id) {
    return audioEngine->getOscillator(id)->getFrequency();
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setOscillatorFrequency(
        JNIEnv *env,
        jobject obj,
        jint id,
        jfloat freq) {
    audioEngine->getOscillator(id)->setFrequency(freq, audioEngine->sample_rate_);
}

JNIEXPORT jboolean JNICALL
Java_dev_covercash_hearlib_NativeAudio_isMasterPlaying(
        JNIEnv *env,
        jobject obj) {
    return static_cast<jboolean>(audioEngine->isMasterPlaying());
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setMasterPlaying(
        JNIEnv *env,
        jobject obj,
        jboolean isOn) {
    audioEngine->setMasterPlaying(isOn);
}

JNIEXPORT float JNICALL
Java_dev_covercash_hearlib_NativeAudio_getRcNative(
        JNIEnv *env,
        jobject obj) {
    return audioEngine->filter_->rc_;
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setRcNative(
        JNIEnv *env,
        jobject obj,
        jfloat rc) {
    audioEngine->filter_->rc_ = rc;
}

JNIEXPORT int JNICALL
Java_dev_covercash_hearlib_NativeAudio_getOscillatorWaveShape(
        JNIEnv *env,
        jobject obj,
        jint id) {
    WaveShape shape = audioEngine->getOscillator(id)->getWaveShape();
    return static_cast<int>(shape);
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setOscillatorWaveShape(
        JNIEnv *env,
        jobject obj,
        jint id,
        jint waveIndex) {
    audioEngine->getOscillator(id)->setWaveShape(WaveShape(waveIndex));
}

JNIEXPORT jboolean JNICALL
Java_dev_covercash_hearlib_NativeAudio_isOscillatorPlaying(
        JNIEnv *env,
        jobject obj,
        jint id) {
    return static_cast<jboolean>(audioEngine->getOscillator(id)->isWaveOn());
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_setOscillatorPlaying(
        JNIEnv *env,
        jobject obj,
        jint id,
        jboolean b) {
    audioEngine->getOscillator(id)->setWaveOn(b);
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_startEngineNative(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->start();
}

JNIEXPORT void JNICALL
Java_dev_covercash_hearlib_NativeAudio_stopEngineNative(
        JNIEnv *env,
        jobject /* this */) {
    audioEngine->stop();
}

}

