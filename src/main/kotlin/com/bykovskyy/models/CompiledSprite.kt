package com.bykovskyy.models

import kotlinx.serialization.Serializable

@Serializable
data class CompiledSprite(val id: String, val name: String, val x: Int, val y: Int, val width: Int, val height: Int) {
    val rectangle get() = Rectangle(x, y, width, height)
}
