package me.jaksara.inventory

import me.jaksara.inventory.listener.ChatInputListener
import me.jaksara.inventory.listener.InventoryMenuListener
import org.bukkit.plugin.Plugin

public object CustomMenu {
    @JvmStatic
    public fun init(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(ChatInputListener(plugin), plugin)
        plugin.server.pluginManager.registerEvents(InventoryMenuListener(plugin), plugin)
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