package com.bykovskyy.storage

import com.bykovskyy.models.PaginatedSpriteList
import com.bykovskyy.models.Sprite
import kotlinx.serialization.json.Json
import java.io.File


class SpritesStorage(private val sheetId: String) {
    fun getAll(): List<Sprite> {
        return getSprites()
    }

    fun get(search: String?, page: Int, limit: Int): PaginatedSpriteList {
        val sprites = if (search.isNullOrBlank()) getSprites() else getSprites().filter { it.name.startsWith(search) }
        val start = (page - 1) * limit

        if (start >= sprites.size) {
            // We are passed the bounds of the array, so return empty
            return PaginatedSpriteList(arrayListOf(), sprites.size, start)
        }

        val end = page * limit
        val normalizedEnd = if (end > sprites.size) sprites.size else end
        return PaginatedSpriteList(sprites.subList(start, normalizedEnd), sprites.size, start)
    }

    fun add(sprites: List<Sprite>) {
        store(getSprites() + sprites)
    }

    fun remove(spriteId: String) {
        store(getSprites().filter { it.id != spriteId })
    }

    fun updateSpriteName(spriteId: String, name: String) {
        val sprite = getSprites().find { it.id == spriteId }

        if (sprite != null) {
            store(getSprites().map { if (it.id == spriteId) Sprite(it.id, name, it.width, it.height) else it })
        }
    }

    private fun getSprites(): List<Sprite> {
        val spritesJsonFile = File("localStorage/$sheetId/sprites.json")

        return if (spritesJsonFile.exists())
            Json.decodeFromString<List<Sprite>>(spritesJsonFile.reader().use { it.readText() })
        else
            arrayListOf()
    }

    private fun store(sprites: List<Sprite>) {
        File("localStorage/$sheetId/sprites.json").printWriter().use {
            it.println(Json { prettyPrint = true }.encodeToString(sprites))
        }
    }
}
