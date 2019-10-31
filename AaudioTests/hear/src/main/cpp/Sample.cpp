//
// Created by chreeus on 10/9/19.
//

#include "Sample.h"

int32_t Sample::write(const float *sourceData, int32_t numFrames) {

    if (write_index_ + numFrames > kMaxSamples) {
        numFrames = kMaxSamples - write_index_;
    }

    for (int i = 0; i < numFrames; ++i) {
        data_[write_index_++] = sourceData[i];
    }

    return numFrames;
}

int32_t Sample::read(float *targetData, int32_t numFrames) {
    int32_t framesRead = 0;
    while (framesRead < numFrames && read_index_ < write_index_) {
        targetData[framesRead++] = data_[read_index_];
        if (looping_ && read_index_ == write_index_)
            read_index_ = 0;
    }

    return framesRead;
}
