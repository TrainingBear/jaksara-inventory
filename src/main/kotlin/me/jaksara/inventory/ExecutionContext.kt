package me.jaksara.inventory

import me.jaksara.inventory.annotation.Executor
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

@ConsistentCopyVisibility
@Executor
public data class ExecutionContext internal constructor(
    val invClickEvent: InventoryClickEvent,
    val source: ClickableButton
) {
    val player: Player = invClickEvent.whoClicked as Player

    public fun getPlayerChatInput(callback: (String) -> Unit) {
        player.closeInventory()
        player.showTitle(
            Title.title(
                "<green>Please Open chat and send message to input!".deserialize(),
                "<yellow>type \"cancel\" or \"q\" without quotes to cancel chat input".deserialize()
            )
        )
        "type \"cancel\" or \"q\" without quotes to cancel chat input".info(player)
        player.jplayer().chatInputCallback = { input ->
            if (input != "cancel" && input != "q")
                callback(input)
            player.openInventory(invClickEvent.inventory)
            player.jplayer().chatInputCallback = null
        }
    }

    /**
     * Refresh or update button appearance that only reapply [ClickableButton.lore], [ClickableButton.title], [ClickableButton.material]
     */
    public fun refresh() {
        source.refresh()
    }

    /**
     * [refresh] for every button inside this menu
     * @see refresh
     */
    public fun refreshAll() {
        source.refreshAll()
    }

    /**
     * Rebuild this button entirely from default initialization of [InventoryMenuDsl.createButton] or [InventoryMenuDsl.button]
     * @suppress use [refresh] instead if u only want to update the appearance of this button (title, lore, material).
     * @see refresh
     */
    public fun rebuild() {
        source.rebuild()
    }

    /**
     * [rebuild] for every button inside this menu
     * @see rebuild
     */
    public fun rebuildAll() {
        source.rebuildAll()
    }
}