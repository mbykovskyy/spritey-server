package com.bykovskyy.routes

import com.bykovskyy.models.Sheet
import com.bykovskyy.models.findPowerOfTwo
import com.bykovskyy.packer.pack
import com.bykovskyy.storage.CompiledSheetStorage
import com.bykovskyy.storage.SheetStorage
import com.bykovskyy.storage.SpritesStorage
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureSheetRouting() {
    routing {
        post("/sheet") {
            val sheet = Sheet(
                id = UUID.randomUUID().toString(),
                maxWidth = 8192,
                maxHeight = 8192,
                isPowerOfTwo = false,
                isMaintainAspectRatio = false,
                backgroundColor = "FF00FF"
            )
            SheetStorage().store(sheet)
            call.respond(sheet)
        }

        get("/sheet/{id}") {
            val sheetId = call.parameters["id"] as String
            val sheet = SheetStorage().get(sheetId)

            if (sheet == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(sheet)
            }
        }

        patch("/sheet/{id}") {
            val sheetId = call.parameters["id"] as String
            val sheet = SheetStorage().get(sheetId)

            if (sheet === null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                val params = call.receiveParameters()
                val isPowerOfTwo = params["isPowerOfTwo"]?.toBoolean() ?: sheet.isPowerOfTwo
                val isMaintainAspectRatio = params["isMaintainAspectRatio"]?.toBoolean() ?: sheet.isMaintainAspectRatio
                val backgroundColor = params["backgroundColor"] ?: sheet.backgroundColor

                var maxWidth = params["maxWidth"]?.toIntOrNull() ?: sheet.maxWidth
                var maxHeight = params["maxHeight"]?.toIntOrNull() ?: sheet.maxHeight

                if (isPowerOfTwo) {
                    maxWidth = findPowerOfTwo(maxWidth)
                    maxHeight = findPowerOfTwo(maxHeight)
                }

                val newSheet = sheet.copy(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    isPowerOfTwo = isPowerOfTwo,
                    isMaintainAspectRatio = isMaintainAspectRatio,
                    backgroundColor = backgroundColor
                )

                SheetStorage().store(newSheet)
                call.respond(newSheet)
            }
        }

        post("/sheet/{id}/compile") {
            val sheetId = call.parameters["id"] as String
            val sheet = SheetStorage().get(sheetId)

            if (sheet === null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                val params = call.receiveParameters()
                val imageType = params["imageType"] ?: "png"
                val metadataType = params["metadataType"] ?: "json"

                val compiledSheet = pack(sheet, SpritesStorage(sheetId).getAll())
                CompiledSheetStorage().store(compiledSheet)
            }
        }
    }
}
