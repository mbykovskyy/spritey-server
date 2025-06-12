package com.bykovskyy.packer

import com.bykovskyy.models.Dimension
import com.bykovskyy.models.Rectangle

fun expandToMaxDimensions(freeZones: List<Rectangle>, sprite: Dimension, sheetSize: Dimension, maxSheetSize: Dimension): Dimension {
    // Go through zones to see how much a zone needs to be expanded to fit a sprite
    for (zone in freeZones) {
        val expandWidthBy = sprite.width - zone.width
        val canExpandWidth = zone.right == sheetSize.width
        val fitsWidth = sheetSize.width + expandWidthBy <= maxSheetSize.width

        if (zone.height >= sprite.height && canExpandWidth && fitsWidth)
            return Dimension(expandWidthBy, 0)

        val expandHeightBy = sprite.height - zone.height
        val canExpandHeight = zone.bottom == sheetSize.height
        val fitsHeight = sheetSize.height + expandHeightBy <= maxSheetSize.height

        if (zone.width >= sprite.width && canExpandHeight && fitsHeight)
            return Dimension(0, expandHeightBy)

        if (canExpandWidth && canExpandHeight && fitsWidth && fitsHeight)
            return Dimension(expandWidthBy, expandHeightBy)
    }
    // If we reach this point it means one of the following: either there
    // are no zones, no zone can be expanded or expanding a zone will
    // break the maximum size constraint. Try to expand either width or
    // height based on sprite width and height, which ever is smallest.
    val canExpandSheetWidth = (sheetSize.width + sprite.width <= maxSheetSize.width)
    val canExpandSheetHeight = (sheetSize.height + sprite.height <= maxSheetSize.height)
    val shouldExpandSheetHeight = sprite.width > sprite.height || !canExpandSheetWidth

    if (canExpandSheetHeight && shouldExpandSheetHeight) {
        if (sprite.width > maxSheetSize.width)
            throw RuntimeException("Sheet maximum width is too small")

        val expandWidthBy = sprite.width - sheetSize.width
        return Dimension(expandWidthBy.coerceAtLeast(0), sprite.height)
    }

    if (canExpandSheetWidth) {
        if (sprite.height > maxSheetSize.height)
            throw RuntimeException("Sheet maximum height is too small")

        val expandHeightBy = sprite.height - sheetSize.height
        return Dimension(sprite.width, expandHeightBy.coerceAtLeast(0))
    }

    throw RuntimeException("Maximum sheet dimensions are too small")
}
