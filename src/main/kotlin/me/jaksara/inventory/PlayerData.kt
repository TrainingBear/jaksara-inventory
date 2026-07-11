package me.jaksara.inventory

import org.bukkit.inventory.Inventory
import java.util.UUID

public class PlayerData(uuid: UUID) {
    public var chatInputCallback: ((String) -> Unit)? = null
    public val inventories: MutableMap<String, Inventory> = mutableMapOf()
}