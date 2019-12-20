package dev.covercash.hearlib

inline class Index(val idx: Int)

inline class Playing(val playing: Boolean)
inline class Frequency(val freq: Float)
inline class Level(val level: Float)

// 'i' represents an index in the corresponding C++ enum
enum class WaveShape(val i: Int) {
    SAW(0),
    SINE(1),
    SQUARE(2),
    TRIANGLE(3),
}
