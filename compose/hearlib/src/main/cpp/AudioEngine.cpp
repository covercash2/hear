//
// Created by chreeus on 9/14/19.
//

#include "AudioEngine.h"
#include <android/log.h>
#include <thread>
#include <mutex>

#include "util.h"

constexpr int32_t kBufferSizeInBursts = 2;

aaudio_data_callback_result_t
dataCallback(
        AAudioStream *stream,
        void *userData,
        void *audioData,
        int32_t numFrames
) {

    auto engine = static_cast<AudioEngine *>(userData);
    auto data = static_cast<float *>(audioData);

    if (engine->isMasterPlaying()) {
        for (auto oscillator : engine->oscillators()) {
            oscillator.render(data, numFrames);
        }
    }

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

AudioEngine::AudioEngine() {
    filter_ = new Filter();
    filter_->rc_ = 1.0;
}

AudioEngine::~AudioEngine() {
    delete oscillator_;
    delete filter_;
}

// returns the index of the created oscillator
unsigned const int AudioEngine::createOscillator(float freq, float level, WaveShape shape) {
    Oscillator new_oscillator(freq, level, shape);
    oscillators_.emplace_back(new_oscillator);
    return oscillators_.size();
}

Oscillator* AudioEngine::getOscillator(int id) {
    return &oscillators_[id];
}

std::vector<Oscillator>& AudioEngine::oscillators() {
    return oscillators_;
}

bool AudioEngine::start() {
    StreamBuilder streamBuilder = makeStreamBuilder();

    AAudioStreamBuilder_setFormat(streamBuilder.get(), AAUDIO_FORMAT_PCM_FLOAT);
    AAudioStreamBuilder_setChannelCount(streamBuilder.get(), 1);
    AAudioStreamBuilder_setPerformanceMode(streamBuilder.get(),
                                           AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setDataCallback(streamBuilder.get(), ::dataCallback, this);
    AAudioStreamBuilder_setErrorCallback(streamBuilder.get(), ::errorCallback, this);

    // open the stream
    aaudio_result_t result = AAudioStreamBuilder_openStream(streamBuilder.get(), &stream_);
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

bool AudioEngine::isMasterPlaying() {
    return master_playing_;
}

void AudioEngine::setMasterPlaying(bool isToneOn) {
    master_playing_ = isToneOn;
}
