package me.jaksara.inventory

import me.jaksara.inventory.annotation.Executor
import net.kyori.adventure.title.Title
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.Arrays
import java.util.Collections

@ConsistentCopyVisibility
@Executor
public data class ExecutionContext internal constructor(
    val invClickEvent: InventoryClickEvent,
    val source: ClickableButton
) {
    val player: Player = invClickEvent.whoClicked as Player

    @JvmOverloads
    public fun getPlayerChatInput(
        title: Title = Title.title(
            "<green>Please Open chat and send message to input!".deserialize(),
            "<yellow>type \"cancel\" or \"q\" without quotes to cancel chat input".deserialize()
        ),
        message: String = "type \"cancel\" or \"q\" without quotes to cancel chat input",
        callback: (String) -> Unit
    ) {
        player.closeInventory()
        player.showTitle(title)
        message.info(player)
        player.jplayer().chatInputCallback = { input ->
            if (input != "cancel" && input != "q") {
                callback(input)
            }
            player.openInventory(invClickEvent.inventory)
            player.jplayer().chatInputCallback = null
            true
        }
    }


    public fun getAnvilInput(
        title: String = "Enter your answer",
        text: String = "What is the meaning of life?",
        callback: (String) -> Boolean
    ) {
        AnvilGUI.Builder()
            .onClose { stateSnapshot ->
                player.openInventory(invClickEvent.inventory)
            }
            .onClick { slot, stateSnapshot ->
                if (slot != AnvilGUI.Slot.OUTPUT) {
                    return@onClick emptyList();
                }

                if (callback(stateSnapshot.text)) {
                    return@onClick listOf(AnvilGUI.ResponseAction.close());
                } else {
                    return@onClick listOf(AnvilGUI.ResponseAction.replaceInputText("Please Try Again"));
                }
            }
            .preventClose()
            .text(text)
            .title(title)
            .plugin(CustomMenu.plugin)
            .open(player);
    }

    /**
     * @param id target of item/button placement id inside this inventory
     * @return List of ItemStack at the given id
     * @throws [NullPointerException] if [id] is not exist in this [InventoryMenuDsl.layout]
     */
    public fun getItem(id: Int): List<ItemStack?> = source.handler.root.getItem(id)

    /**
     * @param id target of item/button placement id inside this inventory.
     * @return [ButtonHandlerWrapper] the handler that already built
     * @throws [NullPointerException] if [id] is not exist in this [InventoryMenuDsl.layout]
     */
    public fun getButton(id: Int): ButtonHandlerWrapper = source.handler.root.getButton(id)

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
        source.handler.buttons.forEach { it.refresh() }
    }

    /**
     * Rebuild this button entirely from [ClickableButton.builder].
     *
     * Or by default it will invoke [InventoryMenuDsl.createButton] or [InventoryMenuDsl.button]
     * @suppress use [refresh] instead if u only want to update the appearance of this button (title, lore, material).
     * @see refresh
     */
    public fun rebuild() {
        source.build()
    }

    /**
     * [rebuild] for every button inside this menu
     * @see rebuild
     */
    public fun rebuildAll() {
        source.rebuildAll()
    }

    /**
     * Open the next page
     * @param id target id
     */
    public fun openNextPage(id: Int) {
        getButton(id).openNextPage()
    }

    /**
     * Open the previous page
     * @param id target id
     */
    public fun openPrevPage(id: Int) {
        getButton(id).openPrevPage()
    }
}