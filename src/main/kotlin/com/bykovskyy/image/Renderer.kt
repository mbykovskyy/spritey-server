package com.bykovskyy.image

import com.bykovskyy.models.CompiledSheet
import com.bykovskyy.storage.SpritesStorage
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage

inline fun BufferedImage.withGraphics(block: (Graphics) -> Unit): BufferedImage {
    val gfx = this.graphics
    try {
        block(gfx)
    } finally {
        gfx.dispose()
    }
    return this
}

fun render(sheet: CompiledSheet): BufferedImage {
    val image = BufferedImage(sheet.width, sheet.height, BufferedImage.TYPE_INT_RGB)

    return image.withGraphics { gfx ->
        gfx.color = Color(sheet.backgroundColor.toInt(16))
        gfx.fillRect(0, 0, sheet.width, sheet.height)

        val spriteStorage = SpritesStorage(sheet.id)

        sheet.sprites.forEach { sprite ->
            gfx.drawImage(spriteStorage.getImage(sprite.id), sprite.x, sprite.y, null)
        }
    }
}
