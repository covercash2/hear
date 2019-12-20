//
// Created by chreeus on 9/14/19.
//

#include "Oscillator.h"
#include <cmath>
#include <android/log.h>

float calculateSawValue(double phase, float amplitude) {
    return static_cast<float>(amplitude - (amplitude * phase / kPi));
}

float calculateSineValue(double phase, float amplitude) {
    return static_cast<float>(sin(phase) * amplitude);
}

float calculateSquareValue(double phase, float amplitude) {
    if (phase < kPi) {
        return amplitude;
    }
    else {
        return -amplitude;
    }
}

float calculateTriangleValue(double phase, float amplitude) {
    if (phase < kPi) {
        return static_cast<float>(-amplitude + (2 * amplitude * phase / kPi));
    }
    else {
        return static_cast<float>(3 * amplitude - (2 * amplitude * phase / kPi));
    }
}

float validate(const float v, const float min, const float max) {
    if (v < min) {
        // TODO abstract log to reduce platform dependence
        __android_log_print(
                ANDROID_LOG_ERROR,
                "Oscillator", "bad argument; less than min: %f", v
        );
        return min;
    }
    if (v > max) {
        __android_log_print(
                ANDROID_LOG_ERROR,
                "Oscillator", "bad argument; greater than max: %f", v
        );
        return max;
    }

    return v;
}

Oscillator::Oscillator(float freq, float level, WaveShape shape) {
    frequency_ = freq;
    level_ = level;
    setWaveShape(shape);
}

Oscillator::Oscillator(const Oscillator& other) {
    frequency_ = other.frequency_;
    level_ = other.level_;
    setWaveShape(other.getWaveShape());
}

void Oscillator::setFrequency(const float freq, const int32_t sample_rate) {
    float new_freq = validate(freq, FREQUENCY_MIN, FREQUENCY_MAX);

    frequency_ = new_freq;
    phase_increment_ = (kTao * new_freq) / (double) sample_rate;
}

const WaveShape Oscillator::getWaveShape() const {
    return wave_shape_;
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

void Oscillator::setWaveShape(WaveShape shape) {
    switch (shape) {
        case kSaw:
            wave_function_ = calculateSawValue;
            break;
        case kSine:
            wave_function_ = calculateSineValue;
            break;
        case kSquare:
            wave_function_ = calculateSquareValue;
            break;
        case kTriangle:
            wave_function_ = calculateTriangleValue;
            break;
    }
}

void Oscillator::render(float *audioData, int32_t numFrames) {
    if (!isWaveOn_.load()) phase_ = 0;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()) {
            // calculate next sample value
            audioData[i] += wave_function_(phase_, level_);

            // increment phase
            phase_ += phase_increment_;
            if (phase_ > kTao) phase_ -= kTao;
        } else {
            // silence
//            audioData[i] = 0;
        }
    }
}

 // Return RC low-pass filter output samples, given input samples,
 // time interval dt, and time constant RC
// function lowpass(real[0..n] input, real dt, real RC)
//   var real[0..n] output
//   var real α := dt / (RC + dt)
//   output[0] := α * input[0]
//   for i from 1 to n
//       output[i] := α * input[i] + (1-α) * output[i-1]
//   return output