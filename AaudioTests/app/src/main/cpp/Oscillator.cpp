//
// Created by chreeus on 9/14/19.
//

#include "Oscillator.h"
#include <cmath>

constexpr float TAO = static_cast<const float>(M_PI * 2);

float validate(const float v, const float min, const float max) {
    if (v < min) {
        return min;
    }
    if (v > max) {
        return max;
    }
    return v;
}

void Oscillator::setFrequency(const float freq) {
    float newFreq = validate(freq, FREQUENCY_MIN, FREQUENCY_MAX);

    frequency_ = newFreq;
    phaseIncrement_ = (TAO * newFreq) / (double) sampleRate_;
}

float Oscillator::getFrequency() {
    return frequency_;
}

void Oscillator::setLevel(float level) {
    float newLevel = validate(level, LEVEL_MIN, LEVEL_MAX);
    level_ = newLevel;
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
    // FIXME i have no idea what memory order to use.
    // it doesn't really matter since
    // it should only be called serially from the
    // android ui thread
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