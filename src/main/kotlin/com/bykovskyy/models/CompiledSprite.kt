package com.bykovskyy.models

data class CompiledSprite(val name: String, val x: Int, val y: Int, val width: Int, val height: Int) {
    val rectangle get() = Rectangle(x, y, width, height)
}
