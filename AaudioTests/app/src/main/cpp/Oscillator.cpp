//
// Created by chreeus on 9/14/19.
//

#include "Oscillator.h"
#include <math.h>

// 2 * pi
#define TAO (3.14159 * 2)
#define FREQUENCY_A 440.0

void Oscillator::setFrequency(float freq) {
    frequency_ = freq;
    phaseIncrement_ = (TAO * freq) / (double) sampleRate_;
}

float Oscillator::getFrequency() {
    return frequency_;
}

void Oscillator::setLevel(float level) {
    level_ = level;
}

float Oscillator::getLevel() {
    return level_;
}

void Oscillator::setSampleRate(int32_t sampleRate) {
    sampleRate_ = sampleRate;
    setFrequency(FREQUENCY_A);
}

void Oscillator::setWaveOn(bool isWaveOn) {
    isWaveOn_.store(isWaveOn);
}

bool Oscillator::isWaveOn() {
    return isWaveOn_.load(std::memory_order_relaxed);
}

void Oscillator::render(float *audioData, int32_t numFrames) {
    if (!isWaveOn_.load()) phase_ = 0;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()) {
            // calculate next sample value
            audioData[i] = (float) (sin(phase_) * level_);

            // increment phase
            phase_ += phaseIncrement_;
            if (phase_ > TAO) phase_ -= TAO;
        } else {
            // silence
            audioData[i] = 0;
        }
    }
}