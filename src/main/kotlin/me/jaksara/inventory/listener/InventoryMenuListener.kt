package me.jaksara.inventory.listener

import me.jaksara.inventory.ClickableButton
import me.jaksara.inventory.ExecutionContext
import me.jaksara.inventory.InventoryMenuDsl
import me.jaksara.inventory.deserialize
import me.jaksara.inventory.error
import me.jaksara.inventory.namespacedKey
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.persistence.PersistentDataType

internal class InventoryMenuListener(val plugin: org.bukkit.plugin.Plugin) : Listener {
    @EventHandler
    fun invClick(e: InventoryClickEvent) {
        if (e.inventory.holder !is InventoryMenuDsl) return
        val dsl = e.inventory.holder as InventoryMenuDsl
        val id =
            e.currentItem?.itemMeta?.persistentDataContainer?.get(dsl.title.namespacedKey(), PersistentDataType.INTEGER)
                ?: return
        e.isCancelled = true
        try {
            val executor = dsl.executor[id]!!
            val context = ExecutionContext(e, executor)
//            "Executing click event with id: $id title: ${executor.id} from ${dsl.title}!".error(plugin)
//            Bukkit.broadcast("Executing ${executor.title} with id: $id task: ${executor.executor}".deserialize())
            executor.executor.invoke(context)
        } catch (e: Exception) {
            "Failed to execute button with id: $id from ${dsl.title}!".error(plugin)
            throw e
        }
    }

    @EventHandler
    fun invOpen(e: InventoryOpenEvent) {
        if (e.inventory.holder !is InventoryMenuDsl) return
        val dsl = e.inventory.holder as InventoryMenuDsl
        dsl.event = e
        dsl.futureButton.forEach { (id, handler) ->
            try {
                handler.build(true)
            } catch (e: Exception) {
                "Failed to load Custom Menu with id: $id, from ${dsl.title} inventory!".error(plugin)
                throw e
            }
        }
    }

    @EventHandler
    fun invClose(e: InventoryCloseEvent) {
        if (e.inventory.holder !is InventoryMenuDsl) return
        val dsl = e.inventory.holder as InventoryMenuDsl
        try {
            dsl.close(e)
        } catch (e: Exception) {
            "Failed to close ${dsl.title} inventory!".error(plugin)
            throw e
        }
    }
}