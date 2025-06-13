package com.bykovskyy.models

import kotlinx.serialization.Serializable

@Serializable
data class  Sheet(
    val id: String,
    val maxWidth: Int,
    val maxHeight: Int,
    val isPowerOfTwo: Boolean,
    val isMaintainAspectRatio: Boolean,
    val backgroundColor: String
)
