package me.jaksara.inventory

import kotlin.math.max
import kotlin.math.min

public class Paginator<T>(
    private val viewSize: Int,
    elements: Collection<T>
): ArrayList<T>(elements) {
    public var page: Int = 1
    public val totalPages: Int
        get() = max(1, (size + viewSize - 1) / viewSize)

    public fun get(): List<T> {
        val from = (page - 1) * viewSize
        val to = min(from + viewSize, size)
        return subList(from, to)
    }

    public fun next(): List<T> {
        page = min(totalPages, page + 1)
        return get()
    }

    public fun prev(): List<T> {
        page = max(1, page - 1)
        return get()
    }
}