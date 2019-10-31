//
// Created by chreeus on 10/9/19.
//

#include <cstdint>
#include "Filter.h"

void Filter::apply(float *data, int32_t numFrames, float dt) {
    // a non-function low pass filter
    float a = dt / (rc_ + dt);
    data[0] = a * data[0];
    for (int i = 1; i < numFrames; ++i) {
        data[i] = data[i-1] + a * (data[i] - data[i-1]);
    }
}