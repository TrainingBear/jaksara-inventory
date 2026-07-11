package me.jaksara.inventory

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

@Executor
public data class ExecutionContext(val invClickEvent: InventoryClickEvent) {
    val player: Player = invClickEvent.whoClicked as Player
    val action: InventoryAction = invClickEvent.action

    public fun getPlayerChatInput(callback: (String) -> Unit) {
        player.closeInventory()
        "Please Open chat and send message to input!".info(player)
        "type \"cancel\" or \"q\" without quotes to cancel chat input".info(player)
        player.jplayer().chatInputCallback = { input ->
            if (input != "cancel" && input != "q")
                callback(input)
            player.openInventory(invClickEvent.inventory)
            player.jplayer().chatInputCallback = null
        }
    }
}