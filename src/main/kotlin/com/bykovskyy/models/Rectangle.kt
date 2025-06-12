package com.bykovskyy.models

data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
    val left get() = x
    val top get() = y
    val right get() = x + width
    val bottom get() = y + height
    val area get() = width * height
    val distanceFromOrigin get() = x + y
    val location get() = Point(x, y)
    val dimension get() = Dimension(width, height)

    constructor(r: Rectangle): this(r.x, r.y, r.width, r.height)

    fun contains(other: Rectangle): Boolean =
        x <= other.x && y <= other.y && right >= other.right && bottom >= other.bottom

    fun intersects(other: Rectangle): Boolean =
        right >= other.x && x <= other.right && bottom >= other.y && y <= other.bottom

    fun subtract(other: Rectangle): List<Rectangle> {
        if (!other.intersects(this)) return listOf(this)

        return buildList {
            if (other.left > left)
                add(Rectangle(left, top, other.left - left, height))

            if (other.top > top)
                add(Rectangle(left, top, width, other.top - top))

            if (other.right < right)
                add(Rectangle(other.right, top, right - other.right, height))

            if (other.bottom < bottom)
                add(Rectangle(left, other.bottom, width, bottom - other.bottom))
        }
    }
}
