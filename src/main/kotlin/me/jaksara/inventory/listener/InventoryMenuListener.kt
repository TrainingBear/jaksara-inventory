package me.jaksara.inventory.listener

import com.sun.source.util.Plugin
import me.jaksara.inventory.ClickableButton
import me.jaksara.inventory.ExecutionContext
import me.jaksara.inventory.InventoryMenuDsl
import me.jaksara.inventory.error
import me.jaksara.inventory.namespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.persistence.PersistentDataType

internal class InventoryMenuListener(public val plugin: org.bukkit.plugin.Plugin) : Listener {
    @EventHandler
    public fun invClick(e: InventoryClickEvent) {
        if (e.inventory.holder !is InventoryMenuDsl) return
        val dsl = e.inventory.holder as InventoryMenuDsl
        val id =
            e.currentItem?.itemMeta?.persistentDataContainer?.get(dsl.title.namespacedKey(), PersistentDataType.INTEGER) ?: return
        e.isCancelled = true
        try {
            val context = ExecutionContext(e);
            dsl.executor[id]!!.invoke(context)
        } catch (e: Exception) {
            "Failed to execute button with id: $id from ${dsl.title}!".error(plugin)
        }
    }

    @EventHandler
    public fun invOpen(e: InventoryOpenEvent) {
        if (e.inventory.holder !is InventoryMenuDsl) return
        val dsl = e.inventory.holder as InventoryMenuDsl
        dsl.futureButton.forEach { (id, init) ->
            try {
                val button = ClickableButton(dsl, id, e)
                init.invoke(button)
                button.build()
                dsl.buttons[id] = button
            } catch (e: Exception) {
                "Failed to load button.id: $id from ${dsl.title} inventory!".error(plugin)
                throw e
            }
        }
    }

    @EventHandler
    public fun invClose(e: InventoryCloseEvent) {
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