package me.jaksara.inventory

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.util.UUID

@Button
public class ClickableButton internal constructor(
    public val root: InventoryMenuDsl,
    public val id: Int,
    public val event: InventoryOpenEvent
) {
    public val player: Player = event.player as Player
    public var executor: ExecutionContext.() -> Unit = {}
    public var material: Material = Material.BEDROCK
    public var title: String = "example"
    public var lore: () -> List<Component> = { listOf() }
    public var item: ItemStack = ItemStack(material)
    public var menu: InventoryMenuDsl? = null
    public var border: Boolean = false

    private val s: ClickableButton.() -> Unit = {
        this@ClickableButton.material = material
        this@ClickableButton.executor = executor
        this@ClickableButton.title = title
        this@ClickableButton.item = item
        this@ClickableButton.lore = lore
        this@ClickableButton.menu = menu
    }
    public var filled: PageState<ClickableButton.() -> Unit>

    init {
        val result = mutableListOf<ClickableButton.() -> Unit>()
        val viewSize = root.indexedLayout[id]!!.size
        repeat(viewSize) {
            result.add(s)
        }
        filled = PageState(viewSize, result)
    }

    /**
     * Set the execution callback when button is clicked
     * @param exec callback block
     */
    public fun onClick(exec: ExecutionContext.() -> Unit) {
        executor = exec
    }

    /**
     * Set lore using Component objects
     * @param lines lore lines as Components
     */
    public fun lore(vararg lines: Component) {
        lore = {
            lines.toList()
        }
    }

    /**
     * Set lore using Strings with MiniMessage support
     * @param lines lore lines as MiniMessage strings
     */
    public fun lore(vararg lines: String) {
        lore = {
            lines.map { it.deserialize() }
        }
    }

    /**
     * Create a submenu for this button
     * @param title Inventory title
     * @param init inventory builder block
     * @return [InventoryMenuDsl], created menu
     */
    public fun createMenu(title: String, init: InventoryMenuDsl.() -> Unit) : InventoryMenuDsl {
        menu = InventoryMenuDsl(title, root.plugin)
        init(menu!!)
        return menu!!
    }

    /**
     * Fill a whole layout with slot [id] to [elements]
     * @param elements the elements that will be placed inside layout
     * @param action builder for each item/button. with
     * @see [openNextPage]
     * @see [openPrevPage]
     */
    public fun fill(elements: List<ItemStack>, action: (Int, ItemStack) -> ClickableButton.() -> Unit) {
        val result = mutableListOf<ClickableButton.() -> Unit>()
        elements.forEachIndexed { index, item ->
            result.add(action(index, item))
        }
        filled = PageState(root.indexedLayout[id]!!.size, result)
    }

    /**
     * Open the next page
     */
    public fun openNextPage() {
        filled.next()
        refresh()
    }

    /**
     * Open the previous page
     */
    public fun openPrevPage() {
        filled.prev()
        refresh()
    }

    public fun build() {
        if (filled.pages.isEmpty()) return
        val iterator = filled.get().iterator()
        root.indexedLayout[id]!!.forEach { index ->
            if (iterator.hasNext()) {
                val button = ClickableButton(root, id, event)
                iterator.next().invoke(button)

                button.buildLore()

                val delta = (id * 1000) + index
                button.item.editMeta {
                    it.persistentDataContainer.set(root.title.namespacedKey(), PersistentDataType.INTEGER, delta)
                }
                root.executor[delta] = button.executor
                root.inv.setItem(index, button.item)
            } else root.inv.setItem(index, AIR)
        }
    }

    /**
     * Refresh or update this [ClickableButton]
     */
    public fun refresh() {
        build()
    }

    /**
     * Refresh or update this [ClickableButton] and all buttons inside current menu
     */
    public fun refreshAll() {
        for (button in root.buttons.values) {
            button.refresh()
        }
    }

    public fun buildLore() {
        if (border) {
            item = item.withType(material)
            item.editMeta {
                it.addItemFlags(*ItemFlag.entries.toTypedArray())
            }
            return
        }
        item = item.withType(material)
        item.editMeta {
            it.displayName(title.deserialize())
            it.lore(lore.invoke())
            it.persistentDataContainer.set(root.title.namespacedKey(), PersistentDataType.INTEGER, id)
        }
    }

    /**
     * Get player head texture
     */
    public fun getHead(owner: UUID): ItemStack {
        return ItemStack(Material.PLAYER_HEAD).also {
            val meta = it.itemMeta as SkullMeta
            meta.owningPlayer = Bukkit.getPlayer(owner)
        }
    }
}