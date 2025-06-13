package com.bykovskyy.storage

import com.bykovskyy.models.Sheet
import kotlinx.serialization.json.Json
import java.io.File


class SheetStorage() {
    fun get(sheetId: String): Sheet? {
        val sheetJsonFile = File("localStorage/$sheetId/sheet.json")

        return if (sheetJsonFile.exists())
            Json.decodeFromString<Sheet>(sheetJsonFile.reader().use { it.readText() })
        else
            null
    }

    fun store(sheet: Sheet) {
        val sheetDir = "localStorage/${sheet.id}"
        File(sheetDir).mkdirs()

        File("$sheetDir/sheet.json").printWriter().use {
            it.println(Json { prettyPrint = true }.encodeToString(sheet))
        }
    }
}
