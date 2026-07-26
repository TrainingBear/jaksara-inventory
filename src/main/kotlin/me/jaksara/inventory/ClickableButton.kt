package me.jaksara.inventory

import me.jaksara.inventory.annotation.Button
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
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
    public var visible: Boolean = true
        private set

    private val s: ClickableButton.() -> Unit = new@{
        this@new.material = this@ClickableButton.material
        this@new.executor = this@ClickableButton.executor
        this@new.title = this@ClickableButton.title
        this@new.item = this@ClickableButton.item
        this@new.lore = this@ClickableButton.lore
        this@new.menu = this@ClickableButton.menu
    }
    public var filled: Paginator<ClickableButton.() -> Unit>

    init {
        val result = mutableListOf<ClickableButton.() -> Unit>()
        val viewSize = root.indexedLayout[id]!!.size
        repeat(viewSize) {
            result.add(s)
        }
        filled = Paginator(viewSize, result)
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
        val toList = lines.toList()
        lore = { toList }
    }

    /**
     * Set lore using Component objects
     * @param lines lore lines as Components
     */
    public fun loreAsComponents(lines: List<Component>) {
        lore = { lines }
    }

    /**
     * Set lore using Strings with MiniMessage support
     * @param lines lore lines as MiniMessage strings
     */
    public fun lore(vararg lines: String) {
        val deserialized = lines.map { it.deserialize() }
        lore = { deserialized }
    }

    /**
     * Set lore using Strings with MiniMessage support
     * @param lines lore lines as MiniMessage strings
     */
    public fun lore(lines: List<String>) {
        val deserialized = lines.map { it.deserialize() }
        lore = { deserialized }
    }

    /**
     * Create a submenu for this button
     * @param title Inventory title
     * @param init inventory builder block
     * @return [InventoryMenuDsl], created menu
     */
    public fun createMenu(title: String, init: InventoryMenuDsl.() -> Unit): InventoryMenuDsl {
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
        fill(result)
    }

    public fun fill(size: Int, action: (Int) -> ClickableButton.() -> Unit) {
        val result = mutableListOf<ClickableButton.() -> Unit>()
        for (index in 0..<size) {
            result.add(action(index))
        }
        fill(result)
    }

    public fun fill(buttons: List<ClickableButton.() -> Unit>) {
        filled = Paginator(root.indexedLayout[id]!!.size, buttons)
    }

    /**
     * Make this button disappear or appear. [visible] is true by default
     * @param state when true, it will appear. or else it will disappear.
     */
    public fun setVisible(state: Boolean){
        if(state==visible) return
        visible = state
        refresh()
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

    internal fun init() {
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
                root.executor[delta] = this
                if(visible) root.inv.setItem(index, button.item)
                else root.inv.setItem(index, AIR)
            } else root.inv.setItem(index, AIR)
        }
//        root.plugin.server.broadcast("button with id: $id built!".deserialize())
    }

    internal fun rebuild() {
        root.futureButton[id]!!.invoke(this)
        refresh()
    }

    /**
     * Refresh or update this [ClickableButton]
     */
    internal fun refresh() {
        init()
    }

    internal fun rebuildAll() {
        for (button in root.buttons.values) {
            button.rebuildAll()
        }
    }

    /**
     * Refresh or update this [ClickableButton] and all buttons inside current menu
     */
    internal fun refreshAll() {
        for (button in root.buttons.values) {
            button.refresh()
        }
    }

    internal fun buildLore() {
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
    public fun getHead(): ItemStack {
        return ItemStack(Material.PLAYER_HEAD).also {
            val meta = it.itemMeta as SkullMeta
            meta.owningPlayer = event.player as OfflinePlayer?
        }
    }
}