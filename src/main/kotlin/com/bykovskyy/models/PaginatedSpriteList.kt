package com.bykovskyy.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedSpriteList(val sprites: List<Sprite>, val totalCount: Int, val startIndex: Int)
