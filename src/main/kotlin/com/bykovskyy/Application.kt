package com.bykovskyy

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.content.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.response.respondText
import io.ktor.server.response.respond
import io.ktor.server.request.receiveMultipart
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cors.routing.CORS
import java.io.File

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        allowHost("localhost:3000")
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello world!")
        }
        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
        post("/files") {
            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        val fileDescription = part.value
                    }
                    is PartData.FileItem -> {
                        val filename = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        File("uploads/$filename").writeBytes(fileBytes)
                    }
                    else -> {}
                }
                part.dispose()
            }

            call.respondText("Hello world!")
        }
    }
}
