package com.bykovskyy.storage

import com.bykovskyy.models.CompiledSheet
import kotlinx.serialization.json.Json
import java.io.File

class CompiledSheetStorage() {
    fun store(sheet: CompiledSheet) {
        val sheetDir = "localStorage/${sheet.id}"
        File(sheetDir).mkdirs()

        File("$sheetDir/compiled-sheet.json").printWriter().use {
            it.println(Json { prettyPrint = true }.encodeToString(sheet))
        }
    }
}
