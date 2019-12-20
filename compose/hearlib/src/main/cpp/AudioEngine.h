//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_AUDIOENGINE_H
#define AAUDIO_TESTS_AUDIOENGINE_H

#include <aaudio/AAudio.h>
#include <android/log.h>
#include <vector>
#include "Oscillator.h"
#include "Filter.h"

class AudioEngine {
public:
    AudioEngine();
    ~AudioEngine();

    bool start();
    void stop();
    void restart();

    bool isMasterPlaying();
    void setMasterPlaying(bool isToneOn);

    const unsigned int createOscillator(float freq, float level, WaveShape shape);
    Oscillator* getOscillator(int id);
    std::vector<Oscillator>& oscillators();

    int32_t sample_rate_ = 0;

    Filter *filter_ = nullptr;

private:
    bool master_playing_ = false;

    Oscillator *oscillator_ = nullptr;
    AAudioStream *stream_ = nullptr;
    std::vector<Oscillator> oscillators_;
};


#endif //AAUDIO_TESTS_AUDIOENGINE_H
