package me.jaksara.inventory

import me.jaksara.inventory.listener.ChatInputListener
import me.jaksara.inventory.listener.InventoryMenuListener
import org.bukkit.plugin.Plugin
import java.time.Duration

public object CustomMenu {
    /**
     * Expire duration after player access any Custom Inventory.
     *
     * when the time has come, it automatically closes the inventory.
     * and free it from memory
     */
    public var expireAfterAccessDuration: Duration = Duration.ofMinutes(60)
    @JvmStatic
    public fun init(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(ChatInputListener(plugin), plugin)
        plugin.server.pluginManager.registerEvents(InventoryMenuListener(plugin), plugin)
        plugin.logger.info("Custom Menu has been successfully initialized!")
    }

    /**
     * Create a new inventory menu
     * @param name inventory title
     * @param plugin the plugin instance (required for inventory creation)
     * @param init builder block
     * @return the built menu
     */
    @JvmStatic
    public fun createMenu(name: String, plugin: Plugin, init: InventoryMenuDsl.() -> Unit): InventoryMenuDsl {
        val menu = InventoryMenuDsl(name, plugin)
        init(menu)
        menu.build()
        return menu
    }
}