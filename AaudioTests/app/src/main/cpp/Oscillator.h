//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_OSCILLATOR_H
#define AAUDIO_TESTS_OSCILLATOR_H

#include <atomic>
#include <stdint.h>

class Oscillator {
public:
    void setFrequency(float freq);
    void setWaveOn(bool isWaveOn);
    void setSampleRate(int32_t sampleRate);
    void render(float *audioData, int32_t numFrames);

private:
    std::atomic<bool> isWaveOn_{false};
    int32_t sampleRate_ = 441000;
    double phase_ = 0.0;
    double phaseIncrement_ = 0.0;
};


#endif //AAUDIO_TESTS_OSCILLATOR_H
