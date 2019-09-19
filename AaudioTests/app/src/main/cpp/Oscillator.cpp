//
// Created by chreeus on 9/14/19.
//

#include "Oscillator.h"
#include <math.h>

// 2 * pi
#define TAO (3.14159 * 2)
#define AMPLITUDE 0.3
#define FREQUENCY_A 440.0

void Oscillator::setFrequency(float freq) {
    phaseIncrement_ = (TAO * freq) / (double) sampleRate_;
}

void Oscillator::setSampleRate(int32_t sampleRate) {
    sampleRate_ = sampleRate;
    setFrequency(FREQUENCY_A);
}

void Oscillator::setWaveOn(bool isWaveOn) {
    isWaveOn_.store(isWaveOn);
}

void Oscillator::render(float *audioData, int32_t numFrames) {
    if (!isWaveOn_.load()) phase_ = 0;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()) {
            // calculate next sample value
            audioData[i] = (float) (sin(phase_) * AMPLITUDE);

            // increment phase
            phase_ += phaseIncrement_;
            if (phase_ > TAO) phase_ -= TAO;
        } else {
            // silence
            audioData[i] = 0;
        }
    }
}