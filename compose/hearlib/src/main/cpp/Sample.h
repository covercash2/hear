//
// Created by chreeus on 10/9/19.
//

#ifndef AAUDIO_TESTS_SAMPLE_H
#define AAUDIO_TESTS_SAMPLE_H

#include <array>

constexpr int kMaxSamples = 480000;

class Sample {
public:
    int32_t read(float *target, int32_t numFrames);
    int32_t write(const float *sourceData, int32_t numFrames);
private:
    std::atomic<int32_t> write_index_ {0};
    std::atomic<int32_t> read_index_ {0};
    std::atomic<bool> looping_ {false};

    std::array<float, kMaxSamples> data_ {0};
};


#endif //AAUDIO_TESTS_SAMPLE_H
