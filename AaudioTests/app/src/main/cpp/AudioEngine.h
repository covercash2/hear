//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_AUDIOENGINE_H
#define AAUDIO_TESTS_AUDIOENGINE_H

#include <aaudio/AAudio.h>
#include "Oscillator.h"
#include "Filter.h"

class AudioEngine {
public:
    AudioEngine(Oscillator *oscillator);
    ~AudioEngine();

    bool start();
    void stop();
    void restart();

    Oscillator* getOscillator();

    void setToneOn(bool isToneOn);

    int32_t sample_rate_ = 0;

    Filter *filter_ = nullptr;

private:
    Oscillator *oscillator_ = nullptr;
    AAudioStream *stream_ = nullptr;
};


#endif //AAUDIO_TESTS_AUDIOENGINE_H
