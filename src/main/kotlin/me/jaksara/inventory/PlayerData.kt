package me.jaksara.inventory

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.bukkit.inventory.Inventory
import java.util.UUID

public class PlayerData(uuid: UUID) {
    public var chatInputCallback: ((String) -> Unit)? = null
    public val inventories: Cache<String, Inventory> = Caffeine.newBuilder()
        .expireAfterAccess(CustomMenu.expireAfterAccessDuration)
        .evictionListener<String, Inventory> { _, v1, _ ->
            v1?.close()
        }.removalListener<String, Inventory> { _, inv, _ ->
            inv?.close()
        }
        .build()
}