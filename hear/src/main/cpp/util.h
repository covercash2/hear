//
// Created by chreeus on 10/9/19.
//

#ifndef AAUDIO_TESTS_UTIL_H
#define AAUDIO_TESTS_UTIL_H

#include <aaudio/AAudio.h>
#include <memory>

using StreamBuilder = std::unique_ptr<AAudioStreamBuilder, decltype(&AAudioStreamBuilder_delete)>;

StreamBuilder makeStreamBuilder();

#endif //AAUDIO_TESTS_UTIL_H
