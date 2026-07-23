package me.jaksara.inventory

import kotlin.math.max
import kotlin.math.min

public class Paginator<T>(viewSize: Int, elements: List<T>) {
    public val pages: HashMap<Int, List<T>> = HashMap()
    public var page: Int = 1;
    init {
        var page = 1;
        var result = mutableListOf<T>()
        for ((cur, element) in elements.withIndex()) {
            if(cur >= viewSize) {
                pages[page] = result
                result.clear()
                page++
            }
            result.add(element)
        }
        if(result.isNotEmpty()) {
            pages[page] = result
        }
        else page--;
    }
    public fun get(): List<T> {
        return pages[page]!!
    }
    public fun next(): List<T>{
        page = min(pages.size, page+1)
        return pages[page]!!
    }
    public fun prev(): List<T> {
        page = max(1, page-1)
        return pages[page]!!
    }
}