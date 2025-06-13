package com.bykovskyy.packer

import com.bykovskyy.models.CompiledSheet
import com.bykovskyy.models.CompiledSprite
import com.bykovskyy.models.Dimension
import com.bykovskyy.models.Point
import com.bykovskyy.models.Rectangle
import com.bykovskyy.models.Sheet
import com.bykovskyy.models.Sprite
import kotlin.collections.sortedBy

fun findLocation(zones: List<Rectangle>, sprite: Dimension): Point? {
    return zones.find {
        it.contains(Rectangle(it.x, it.y, sprite.width, sprite.height))
    }?.location
}

fun removeContainedZones(zones: List<Rectangle>): List<Rectangle> {
    val zonesSortedByArea = zones.sortedBy { it.area }
    return zonesSortedByArea.filterIndexed { i, zone ->
        (i + 1 until zonesSortedByArea.size).none { zonesSortedByArea[it].contains(zone) }
    }
}

fun recalculateZones(zones: List<Rectangle>, sprite: Rectangle): List<Rectangle> {
    val rect = Rectangle(sprite.x, sprite.y, sprite.width, sprite.height)
    val newZones = buildList {
        zones.forEach { addAll(it.subtract(rect)) }
    }
    return removeContainedZones(newZones).sortedBy { it.distanceFromOrigin }
}

fun expandZone(zone: Rectangle, expandBy: Dimension, sheetSize: Dimension): Rectangle {
    val newWidth = if (zone.right == sheetSize.width) zone.width + expandBy.width else zone.width
    val newHeight = if (zone.bottom == sheetSize.height) zone.height + expandBy.height else zone.height
    return Rectangle(zone.x, zone.y, newWidth, newHeight)
}

fun expandZones(zones: List<Rectangle>, expandBy: Dimension, sheetSize: Dimension): List<Rectangle> {
    return buildList {
        zones.forEach {
            add(expandZone(it, expandBy, sheetSize))
        }
        if (expandBy.width > 0)
            add(Rectangle(sheetSize.width, 0, expandBy.width, sheetSize.height + expandBy.height))
        if (expandBy.height > 0)
            add(Rectangle(0, sheetSize.height, sheetSize.width + expandBy.width, expandBy.height))
    }
}

typealias ExpandStrategy = (freeZones: List<Rectangle>, sprite: Dimension, sheetSize: Dimension, maxSheetSize: Dimension) -> Dimension

fun getExpandStrategy(sheet: Sheet): ExpandStrategy {
    return ::expandToMaxDimensions
}

fun pack(sheet: Sheet, sprites: List<Sprite>): CompiledSheet {
    require(sprites.isNotEmpty()) { "There are no sprites to pack into a sprite sheet." }

    var currentSheetSize = Dimension(0, 0)
    val maxSheetSize = Dimension(sheet.maxWidth, sheet.maxHeight)
    var freeZones = listOf<Rectangle>()

    val compiledSprites = sprites.sortedByDescending { it.width }
        .map { sprite ->
            var location = findLocation(freeZones, sprite.dimension)

            if (location == null) {
                val expandBy = getExpandStrategy(sheet)(freeZones, sprite.dimension, currentSheetSize, maxSheetSize)
                freeZones = expandZones(freeZones, expandBy, currentSheetSize)

                // Expand sheet after expanding zones because expandZones uses current sheet dimension
                // to expands zones that are at the right and bottom edges of the sheet
                currentSheetSize = currentSheetSize.expandBy(expandBy)

                location = findLocation(freeZones, sprite.dimension)
                if (location == null) {
                    throw RuntimeException("Maximum sheet dimensions are too small.")
                }
            }

            val compiledSprite = CompiledSprite(sprite.name, location.x, location.y, sprite.width, sprite.height)
            freeZones = recalculateZones(freeZones, compiledSprite.rectangle)
            compiledSprite
    }
    return CompiledSheet(sheet.id, currentSheetSize, sheet.backgroundColor, compiledSprites)
}
