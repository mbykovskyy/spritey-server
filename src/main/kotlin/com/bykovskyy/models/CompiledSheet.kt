package com.bykovskyy.models

data class CompiledSheet(
    val width: Int,
    val height: Int,
    val backgroundColor: String,
    val sprites: List<CompiledSprite>
)
