//
// Created by chreeus on 9/14/19.
//

#include "AudioEngine.h"
#include <android/log.h>
#include <thread>
#include <mutex>

constexpr int32_t kBufferSizeInBursts = 2;

aaudio_data_callback_result_t
dataCallback(
        AAudioStream *stream,
        void *userData,
        void *audioData,
        int32_t numFrames) {

    auto engine = static_cast<AudioEngine *>(userData);
    auto data = static_cast<float *>(audioData);

    engine->getOscillator()->render(data, numFrames);

    // TODO implement filters
//    int32_t sample_rate = AAudioStream_getSampleRate(stream);
//    int32_t samples_per_frame = AAudioStream_getSamplesPerFrame(stream);
//    int32_t sample_num = samples_per_frame * numFrames;
//    float dt = (float)sample_num / (float)sample_rate;
//    float rc = engine->filter_->rc_;
//
//    engine->filter_->apply(data, numFrames, dt);

    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

void errorCallback(
        AAudioStream *stream,
        void *userData,
        aaudio_result_t error) {
    if (error == AAUDIO_ERROR_DISCONNECTED) {
        std::function<void(void)> restartFunction =
                std::bind(&AudioEngine::restart, static_cast<AudioEngine *>(userData));

        new std::thread(restartFunction);
    }
}

AudioEngine::AudioEngine(Oscillator *oscillator) {
    oscillator_ = oscillator;
    filter_ = new Filter();
    filter_->rc = 1.0;
}
AudioEngine::~AudioEngine() {
    delete oscillator_;
    delete filter_;
}

bool AudioEngine::start() {
    AAudioStreamBuilder *streamBuilder;
    AAudio_createStreamBuilder(&streamBuilder);
    AAudioStreamBuilder_setFormat(streamBuilder, AAUDIO_FORMAT_PCM_FLOAT);
    AAudioStreamBuilder_setChannelCount(streamBuilder, 1);
    AAudioStreamBuilder_setPerformanceMode(streamBuilder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setDataCallback(streamBuilder, ::dataCallback, this);
    AAudioStreamBuilder_setErrorCallback(streamBuilder, ::errorCallback, this);

    // open the stream
    aaudio_result_t result = AAudioStreamBuilder_openStream(streamBuilder, &stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(
                ANDROID_LOG_ERROR,
                "AudioEngine", "Error opening stream %s",
                AAudio_convertResultToText(result));
        return false;
    }

    sample_rate_ = AAudioStream_getSampleRate(stream_);

    AAudioStream_setBufferSizeInFrames(
            stream_,
            AAudioStream_getFramesPerBurst(stream_) * kBufferSizeInBursts);

    // start the stream
    result = AAudioStream_requestStart(stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(
                ANDROID_LOG_ERROR,
                "AudioEngine", "error starting stream %s",
                AAudio_convertResultToText(result));
        return false;
    }

    AAudioStreamBuilder_delete(streamBuilder);
    return true;
}

void AudioEngine::restart() {
    static std::mutex restartingLock;
    if (restartingLock.try_lock()) {
        stop();
        start();
        restartingLock.unlock();
    }
}

void AudioEngine::stop() {
    if (stream_ != nullptr) {
        AAudioStream_requestStop(stream_);
        AAudioStream_close(stream_);
    }
}

void AudioEngine::setToneOn(bool isToneOn) {
    oscillator_->setWaveOn(isToneOn);
}

Oscillator *AudioEngine::getOscillator() {
    return oscillator_;
}
