package me.jaksara.inventory

import me.jaksara.inventory.annotation.Menu
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.io.Closeable
import kotlin.math.max
import kotlin.math.min


/**
 * Do not construct a Custom Menu manually. instead use [CustomMenu.createMenu] to
 * start making Custom Menu
 */
@Menu
public open class InventoryMenuDsl internal constructor(public var title: String, public val plugin: Plugin) : InventoryHolder, Cloneable {
    public var inv: Inventory = plugin.server.createInventory(this, 9)
    protected var layout: IntArray = intArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
    )
    public var indexedLayout: MutableMap<Int, MutableList<Int>> = mutableMapOf<Int, MutableList<Int>>()
        private set

    public val executor: MutableMap<Int, ExecutionContext.() -> Unit> = mutableMapOf<Int, ExecutionContext.() -> Unit>()
    public val futureButton: MutableMap<Int, ClickableButton.() -> Unit> =
        mutableMapOf()
    public val buttons: MutableMap<Int, ClickableButton> = mutableMapOf<Int, ClickableButton>()
    public val tasks: MutableList<Closeable> = mutableListOf<Closeable>()

    /**
     * Create layout that represent [buttons]/item placement. with each element of layout,
     * identified as id of [buttons] or item.
     *
     * the size of [layout] must be divisible by 9
     * (size % 9 == 0), and layout size not exceed 9x6 (54).
     * @param layout Layout id's
     * @throws IllegalArgumentException if the layout size is not divisible by 9, or if the layout size exceed 9x6
     */
    public fun layout(vararg layout: Int) {
        this.layout = layout
        if (layout.size % 9 != 0) throw IllegalArgumentException("You can't create an inventory with size of ${layout.size}, the inventory size must be divisible by 9 and != 0")
        indexedLayout.clear()
        for ((index, i) in layout.withIndex()) {
            indexedLayout.computeIfAbsent(i) { mutableListOf() }.add(index)
        }
    }

    /**
     * @param id target of item/button placement id
     * @return List of ItemStack at the given id
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun getItem(id: Int): List<ItemStack?> {
        return indexedLayout[id]!!.map { inv.getItem(it) }
    }

    /**
     * Template button for exit (close inventory)
     * throw [NullPointerException] if [id] is not exist in [layout]
     * @param id target of item/button placement id
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun exit(id: Int, material: Material) {
        val init: ClickableButton.() -> Unit = border@{
            this@border.material = material
            this@border.title = "<red>Exit"
            this@border.onClick {
                player.closeInventory()
            }
        }
        futureButton[id] = init
    }

    /**
     * Template button for border
     * @param id target of item/button placement id
     * @param material material
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun border(id: Int, material: Material) {
        val init: ClickableButton.() -> Unit = border@{
            this@border.material = material
            this@border.title = ""
            this@border.border = true
        }
        futureButton[id] = init
    }

    protected val selectedElement: MutableMap<Int, Int> = mutableMapOf<Int, Int>()
    private fun getOptionLine(id: Int, idx: Int, lines: List<String>): String {
        val selected = selectedElement[id]!!
        return if (selected == idx) {
            "<green> <bold>><reset><green> ${lines[idx]}"
        } else "<gray> - ${lines[idx]}"
    }

    /**
     * Create a template button for option selection
     * @param id target of item/button placement id.
     * @param material target material
     * @param name button title
     * @param element options
     * @param lore description button
     * @param callback callback when selection changes
     *
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun optionButton(
        id: Int,
        material: Material,
        name: String,
        element: List<String>,
        lore: List<String>,
        callback: (String) -> Unit = {}
    ) {
        val init: ClickableButton.() -> Unit = option@{
            this@InventoryMenuDsl.selectedElement[id] = 0

            this@option.material = material
            this@option.title = name
            val completeLore = mutableListOf<String>()
            completeLore += lore
            completeLore += ""
            element.forEachIndexed { index, string ->
                completeLore += this@InventoryMenuDsl.getOptionLine(id, index, element)
            }
            lore(*completeLore.toTypedArray())
            onClick {
                if (invClickEvent.isLeftClick) {
                    this@InventoryMenuDsl.selectedElement[id] = this@InventoryMenuDsl.selectedElement[id]!! + 1
                    this@InventoryMenuDsl.selectedElement[id] =
                        min(this@InventoryMenuDsl.selectedElement[id]!!, element.size - 1)
                }
                if (invClickEvent.isRightClick) {
                    this@InventoryMenuDsl.selectedElement[id] = this@InventoryMenuDsl.selectedElement[id]!! - 1
                    this@InventoryMenuDsl.selectedElement[id] = max(this@InventoryMenuDsl.selectedElement[id]!!, 0)
                }
                val completeLore = mutableListOf<String>()
                completeLore += lore
                completeLore += ""
                element.forEachIndexed { index, string ->
                    completeLore += this@InventoryMenuDsl.getOptionLine(id, index, element)
                }
                lore(*completeLore.toTypedArray())
                callback(element[this@InventoryMenuDsl.selectedElement[id]!!])
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                refresh()
            }
        }
        futureButton[id] = init
    }

    /**
     * Create a button to Custom menu
     * @param id target of item/button placement id.
     * @param init body/builder for the button
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun button(id: Int, init: ClickableButton.() -> Unit) {
        futureButton[id] = init
    }

    /**
     * @param id target of item/button placement id.
     * @return [ClickableButton]
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun getButton(id: Int): ClickableButton {
        return buttons[id]!!
    }

    /**
     * Make [player] open this inventory with individually instance
     * @param player target player
     * @param forceUpdate if [forceUpdate] true, it always creates a new inventory instance for [player]
     */
    @JvmOverloads
    public fun open(player: Player, forceUpdate: Boolean = false) {
        if (forceUpdate) {
            build()
            player.openInventory(inv)
            return
        }
        player.openInventory(player.jplayer().inventories.getOrPut(title) {
            build()
            inv
        })
    }

    /**
     * Build a new inventory instance
     */
    public fun build() {
        inv = plugin.server.createInventory(this, layout.size, title.deserialize())
    }

    /**
     * this [close] is getting called when player close this inventory instance
     */
    public open fun close(event: InventoryCloseEvent) {
        tasks.forEach { it.close() }
        layout.toSet().forEach {
            indexedLayout[it]!!.forEach { index ->
                val item = inventory.getItem(index)
                if (futureButton[it] == null && item != null) {
                    inv.setItem(index, null)
                    event.player.inventory.addItem(item)
                }
            }
        }
    }

    override fun getInventory(): Inventory {
        return inv
    }
}

