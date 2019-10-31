//
// Created by chreeus on 10/9/19.
//

#ifndef AAUDIO_TESTS_FILTER_H
#define AAUDIO_TESTS_FILTER_H


class Filter {
public:
    float rc_ = 0.0;

    void apply(float* audioData, int32_t numFrames, float dt);
};


#endif //AAUDIO_TESTS_FILTER_H
