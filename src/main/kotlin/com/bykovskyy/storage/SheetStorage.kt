package com.bykovskyy.storage

import com.bykovskyy.models.Sheet
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


class SheetStorage() {
    fun get(sheetId: String): Sheet? {
        val sheetJsonFile = File("localStorage/$sheetId/sheet.json")

        return if (sheetJsonFile.exists())
            jacksonObjectMapper().readValue<Sheet>(sheetJsonFile)
        else
            null
    }

    fun store(sheet: Sheet) {
        val sheetDir = "localStorage/${sheet.id}"
        File(sheetDir).mkdirs()

        jacksonObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .writeValue(File("$sheetDir/sheet.json"), sheet)
    }
}
