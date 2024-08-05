package com.bykovskyy.routes

import com.bykovskyy.models.Sprite
import com.bykovskyy.storage.SpritesStorage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.UUID
import javax.imageio.ImageIO

fun Application.configureSpriteRouting() {
    routing {
        post("/{sheetId}/sprites") {
            val sheetId = call.parameters["sheetId"] as String
            val spritesDir = "localStorage/${sheetId}/sprites"
            File(spritesDir).mkdirs()

            val sprites = arrayListOf<Sprite>()

            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val id = UUID.randomUUID().toString()
                        val imageFile = File("$spritesDir/$id")
                        imageFile.writeBytes(part.streamProvider().readBytes())

                        val image = ImageIO.read(imageFile)
                        sprites.add(Sprite(
                            id = id,
                            name = part.originalFileName as String,
                            width = image.width,
                            height = image.height
                        ))
                    }
                    else -> {}
                }
                part.dispose()
            }

            SpritesStorage(sheetId).add(sprites)
            call.respond(sprites)
        }

        get("{sheetId}/sprites") {
            val sheetId = call.parameters["sheetId"] as String
            val page = call.request.queryParameters["page"]?.toIntOrNull()
            val limit = call.request.queryParameters["limit"]?.toIntOrNull()
            val search = call.request.queryParameters["search"]

            if (page == null || page < 1 || limit == null || limit < 1) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                call.respond(SpritesStorage(sheetId).get(search, page, limit))
            }
        }

        patch("{sheetId}/sprites/{id}") {
            val sheetId = call.parameters["sheetId"] as String
            val spriteId = call.parameters["id"] as String

            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "name" -> {
                                SpritesStorage(sheetId).updateSpriteName(spriteId, part.value)
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }
            call.respond(HttpStatusCode.NoContent)
        }

        delete("{sheetId}/sprites/{id}") {
            val sheetId = call.parameters["sheetId"] as String
            val spriteId = call.parameters["id"] as String

            SpritesStorage(sheetId).remove(spriteId)
            File("localStorage/$sheetId/sprites/$spriteId").delete()
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
