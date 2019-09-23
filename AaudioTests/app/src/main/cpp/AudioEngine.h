//
// Created by chreeus on 9/14/19.
//

#ifndef AAUDIO_TESTS_AUDIOENGINE_H
#define AAUDIO_TESTS_AUDIOENGINE_H

#include <aaudio/AAudio.h>
#include "Oscillator.h"

class AudioEngine {
public:
    bool start();
    void stop();
    void restart();

    Oscillator* getOscillator();

    void setFrequency(float freq);
    void setToneOn(bool isToneOn);

private:
    Oscillator oscillator_;
    AAudioStream *stream_;
};


#endif //AAUDIO_TESTS_AUDIOENGINE_H
