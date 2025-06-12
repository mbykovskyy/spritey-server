package com.bykovskyy.models

data class Dimension(val width: Int, val height: Int) {
    fun expandBy(size: Dimension): Dimension = Dimension(width + size.width, height + size.height)
}

fun findPowerOfTwo(value: Int): Int {
    return if (value <= 0) 1 else 1 shl (32 - (value - 1).countLeadingZeroBits())
}
