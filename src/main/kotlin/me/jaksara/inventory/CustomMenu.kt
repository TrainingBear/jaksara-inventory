package me.jaksara.inventory

import me.jaksara.inventory.listener.ChatInputListener
import me.jaksara.inventory.listener.InventoryMenuListener
import org.bukkit.plugin.Plugin
import org.bukkit.Material
import java.time.Duration
import java.util.function.Consumer

public object CustomMenu {
    /**
     * Expire duration after player access any Custom Inventory.
     *
     * when the time has come, it automatically closes the inventory.
     * and free it from memory
     */
    public var expireAfterAccessDuration: Duration = Duration.ofMinutes(60)
    public var plugin: Plugin? = null

    @JvmStatic
    public fun init(plugin: Plugin) {
        this.plugin = plugin
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

    @JvmStatic
    public fun createButton(handler: ButtonHandler? = null, init: ClickableButton.()-> Unit): ClickableButton {
        val button = if(handler != null) ClickableButton(handler) else ClickableButton()
        button.builder = init
        try{
            init.invoke(button)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return button
    }

    /** Java-friendly overload so Java can pass a Consumer<ClickableButton> */
    @JvmStatic
    public fun createButton(handler: ButtonHandler? = null, init: Consumer<ClickableButton>): ClickableButton {
        return createButton(handler) { init.accept(this) }
    }

    /** Java-friendly createMenu that returns an editable InventoryMenuDsl instance (no init lambda) */
    @JvmStatic
    public fun createMenu(name: String, plugin: Plugin): InventoryMenuDsl {
        val menu = InventoryMenuDsl(name, plugin)
        menu.build()
        return menu
    }
}