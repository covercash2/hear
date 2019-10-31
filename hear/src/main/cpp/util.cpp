//
// Created by chreeus on 10/9/19.
//

#include <android/log.h>
#include "util.h"

StreamBuilder makeStreamBuilder() {
    AAudioStreamBuilder *builder = nullptr;
    aaudio_result_t result = AAudio_createStreamBuilder(&builder);
    if (result != AAUDIO_OK) {
        __android_log_print(ANDROID_LOG_ERROR, __func__, "failed to create stream builder %s (%d)",
                            AAudio_convertResultToText(result), result);
        return StreamBuilder(nullptr, &AAudioStreamBuilder_delete);
    }
    return StreamBuilder(builder, &AAudioStreamBuilder_delete);
}
