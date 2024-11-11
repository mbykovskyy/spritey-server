package com.bykovskyy.models

fun findPowerOfTwo(value: Int): Int {
    return if (value <= 0) 1 else 1 shl (32 - (value - 1).countLeadingZeroBits())
}
