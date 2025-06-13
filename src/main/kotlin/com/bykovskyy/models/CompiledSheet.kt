package com.bykovskyy.models

import kotlinx.serialization.Serializable

@Serializable
data class CompiledSheet(
    val id: String,
    val width: Int,
    val height: Int,
    val backgroundColor: String,
    val sprites: List<CompiledSprite>
) {
    constructor(
        id: String, size: Dimension, backgroundColor: String, sprites: List<CompiledSprite>
    ): this(id, size.width, size.height, backgroundColor, sprites)
}
