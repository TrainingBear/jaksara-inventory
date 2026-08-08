package me.jaksara.inventory

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.bukkit.inventory.Inventory

public class PlayerData {
    public var chatInputCallback: ((String) -> Boolean)? = null
    public val inventories: Cache<String, Inventory> = Caffeine.newBuilder()
        .expireAfterAccess(CustomMenu.expireAfterAccessDuration)
        .evictionListener<String, Inventory> { _, v1, _ ->
            v1?.close()
        }.removalListener<String, Inventory> { _, inv, _ ->
            inv?.close()
        }
        .build()
}