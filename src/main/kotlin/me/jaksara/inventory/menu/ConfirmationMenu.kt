package me.jaksara.inventory.menu

import me.jaksara.inventory.CustomMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin

public object ConfirmationMenu {
    public fun open(
        title: String,
        description: List<String>,
        player: Player,
        plugin: Plugin,
        callback: ConfirmationCallback
    ) {
        CustomMenu.createMenu(title, plugin) {
            layout(
                0, 0, 0, 0, 1, 2, 2, 2, 2,
                0, 0, 6, 0, 1, 2, 7, 2, 2,
                0, 0, 0, 0, 1, 2, 2, 2, 2,
            )
            border(0, Material.LIME_STAINED_GLASS_PANE)
            border(2, Material.RED_STAINED_GLASS_PANE)
            button(1) {
                material = Material.PURPLE_STAINED_GLASS_PANE
                this.title = title
                lore(*description.toTypedArray())
            }
            button(6) {
                material = Material.LIME_CONCRETE
                this.title = "<green><bold>Confirm"
                onClick {
                    player.closeInventory()
                    callback.confirm(invClickEvent)
                }
            }
            button(7) {
                material = Material.RED_CONCRETE
                this.title = "<green><bold>Cancel"
                onClick {
                    player.closeInventory()
                    callback.cancel(invClickEvent)
                }
            }
        }.open(player)
    }
}

public interface ConfirmationCallback {
    public fun confirm(event: InventoryClickEvent)
    public fun cancel(event: InventoryClickEvent)
}