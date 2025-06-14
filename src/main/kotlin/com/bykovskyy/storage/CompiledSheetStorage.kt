package com.bykovskyy.storage

import com.bykovskyy.models.CompiledSheet
import kotlinx.serialization.json.Json
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class CompiledSheetStorage() {
    fun store(sheet: CompiledSheet) {
        val sheetDir = "localStorage/${sheet.id}/compiled"
        File(sheetDir).mkdirs()

        File("$sheetDir/sheet.json").printWriter().use {
            it.println(Json { prettyPrint = true }.encodeToString(sheet))
        }
    }

    fun store(sheet: CompiledSheet, image: BufferedImage) {
        val sheetDir = "localStorage/${sheet.id}/compiled"
        File(sheetDir).mkdirs()

        File("$sheetDir/sheet.json").printWriter().use {
            it.println(Json { prettyPrint = true }.encodeToString(sheet))
        }

        ImageIO.write(image, "png", File("$sheetDir/image.png"))
    }
}
