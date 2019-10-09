//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_OSCILLATOR_H
#define AAUDIO_TESTS_OSCILLATOR_H

#include <atomic>
#include <stdint.h>
#include <functional>
#include <cmath>

constexpr float FREQUENCY_A = 440.0;
constexpr float FREQUENCY_MIN = 10.0;
constexpr float FREQUENCY_MAX = 20000.0;
constexpr float LEVEL_MIN = 0.0;
constexpr float LEVEL_MAX = 1.0;

constexpr float kPi = static_cast<const float>(M_PI);
constexpr float kTao = kPi * 2;

float calculateSawValue(double phase, float amplitude);
float calculateSineValue(double phase, float amplitude);
float calculateSquareValue(double phase, float amplitude);
float calculateTriangleValue(double phase, float amplitude);

// indexed for clarity
enum WaveShape {
    kSaw = 0,
    kSine = 1,
    kSquare = 2,
    kTriangle = 3,
};

class Oscillator {
public:
    Oscillator(float frequency, float level, WaveShape waveShape);

    void setFrequency(float freq, int32_t sample_rate);
    float getFrequency();
    void setLevel(float freq);
    float getLevel();
    void setWaveOn(bool isWaveOn);
    bool isWaveOn();
    void setWaveShape(WaveShape shape);
    WaveShape getWaveShape();
    void render(float *audioData, int32_t numFrames);

private:
    std::atomic<bool> isWaveOn_{false};

    float frequency_ = 0.0;
    float level_ = 0.0;

    double phase_ = 0.0;
    double phase_increment_ = 0.0;

    WaveShape wave_shape_ = WaveShape(0);
    std::function<float(double,float)> wave_function_ = calculateSineValue;
};


#endif //AAUDIO_TESTS_OSCILLATOR_H
