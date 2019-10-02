//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_OSCILLATOR_H
#define AAUDIO_TESTS_OSCILLATOR_H

#include <atomic>
#include <stdint.h>

#define FREQUENCY_A 440.0

class Oscillator {
public:
    void setFrequency(float freq);
    float getFrequency();
    void setLevel(float freq);
    float getLevel();
    void setWaveOn(bool isWaveOn);
    bool isWaveOn();
    void setSampleRate(int32_t sampleRate);
    void render(float *audioData, int32_t numFrames);

private:
    std::atomic<bool> isWaveOn_{false};
    int32_t sampleRate_ = 441000;

    float frequency_ = FREQUENCY_A;
    float level_ = 0.0;

    double phase_ = 0.0;
    double phaseIncrement_ = 0.0;
};


#endif //AAUDIO_TESTS_OSCILLATOR_H
