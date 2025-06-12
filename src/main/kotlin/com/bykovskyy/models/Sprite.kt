package com.bykovskyy.models

data class Sprite(val id: String, var name: String, val width: Int, val height: Int) {
    val dimension get() = Dimension(width, height)
}
