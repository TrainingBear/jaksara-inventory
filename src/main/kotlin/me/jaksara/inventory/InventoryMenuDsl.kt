package me.jaksara.inventory

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import me.jaksara.inventory.annotation.Menu
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
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
public open class InventoryMenuDsl internal constructor(public var title: String, public val plugin: Plugin) :
    InventoryHolder, Cloneable {
    public var inv: Inventory = plugin.server.createInventory(this, 9)
    protected var layout: IntArray = intArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0,
    )
    public var indexedLayout: HashMap<Int, MutableList<Int>> = HashMap()
        private set

    public val executor: HashMap<Int, ClickableButton> = HashMap()
    public val futureButton: HashMap<Int, ClickableButton.() -> Unit> = HashMap()
    public val buttons: HashMap<Int, ClickableButton> = HashMap()
    public val tasks: MutableList<Closeable> = mutableListOf()

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

    protected val selectedElement: Int2IntOpenHashMap = Int2IntOpenHashMap().apply {
        defaultReturnValue(-1)
    }

    private fun getOptionLine(id: Int, idx: Int, lines: List<String>): String {
        val selected = selectedElement[id]
        return if (selected == idx) {
            "<green> <bold>><reset><green> ${lines[idx]}"
        } else "<gray> - ${lines[idx]}"
    }

    /**
     * Create a template button for option selection
     * @param id target of item/button placement id.
     * @param material target material
     * @param title button title
     * @param options options
     * @param selectedIndex the selected element position. in range 0 <= [selectedIndex] < [List.size]. it specified -1 by default, meaning has no selected element
     * @param lore description button
     * @param callback callback when selection changes
     *
     * @throws [NullPointerException] if [id] is not exist in [layout]
     * @see listButton
     */
    public fun optionButton(
        id: Int,
        material: Material,
        title: String,
        lore: List<String> = emptyList(),
        options: List<String>,
        selectedIndex: Int = -1,
        callback: (String) -> Unit = {}
    ) {
        val init: ClickableButton.() -> Unit = option@{
            selectedElement[id] = selectedIndex
            this@option.material = material
            this@option.title = title
            val completeLore = mutableListOf<String>()
            completeLore += lore
            for (i in 1 until options.size) completeLore += this@InventoryMenuDsl.getOptionLine(id, i, options)

            lore(*completeLore.toTypedArray())
            onClick {
                if (invClickEvent.isLeftClick) {
                    this@InventoryMenuDsl.selectedElement[id] = this@InventoryMenuDsl.selectedElement[id] + 1
                    this@InventoryMenuDsl.selectedElement[id] =
                        min(this@InventoryMenuDsl.selectedElement[id], options.size - 1)
                } else if (invClickEvent.isRightClick) {
                    this@InventoryMenuDsl.selectedElement[id] = this@InventoryMenuDsl.selectedElement[id] - 1
                    this@InventoryMenuDsl.selectedElement[id] = max(this@InventoryMenuDsl.selectedElement[id], 0)
                } else return@onClick
                val completeLore = mutableListOf<String>()
                completeLore += lore
                for (i in 1 until options.size) completeLore += this@InventoryMenuDsl.getOptionLine(id, i, options)
                lore(*completeLore.toTypedArray())
                callback(options[this@InventoryMenuDsl.selectedElement[id]])
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                refresh()
            }
        }
        futureButton[id] = init
    }

    /**
     * Create a template button for option selection
     * @param id target of item/button placement id.
     * @param material target material
     * @param title button title
     * @param options options
     * @param selectedElement the selected element of [options]. if the element is not inside [options] it will  be unselected
     * @param lore description button
     * @param callback callback when selection changes
     *
     * @throws [NullPointerException] if [id] is not exist in [layout]
     * @see listButton
     */
    public fun optionButton(
        id: Int,
        material: Material,
        title: String,
        lore: List<String> = emptyList(),
        options: List<String>,
        selectedElement: String = "",
        callback: (String) -> Unit = {}
    ){
        optionButton(id, material, title, lore, options, options.indexOf(selectedElement), callback)
    }

    /**
     * Create a template button for list button
     * @param id target of item/button placement id.
     * @param material target material
     * @param title button title
     * @param lore description button
     * @param list container for storing element
     * @param callback callback when selection changes. with param [list]
     *
     * @throws [NullPointerException] if [id] is not exist in [layout]
     * @see optionButton
     */
    @JvmOverloads
    public fun listButton(
        id: Int,
        material: Material,
        title: String,
        lore: List<String> = emptyList(),
        list: MutableList<String>,
        callback: (MutableList<String>) -> Unit = {}
    ) {
        val init: ClickableButton.() -> Unit = list@{
            this.material = material
            this.title = title
            val completeLore = mutableListOf<String>()
            completeLore.addAll(lore)
            completeLore += ""
            completeLore += "<yellow>Left Click to add element"
            completeLore += "<yellow>Middle Click to remove last element"
            completeLore += "<yellow>Right Click to remove specified element"
            for (string in list)
                completeLore += "<gray> - <white>$string"

            this.lore(completeLore)
            onClick {
                if (invClickEvent.isLeftClick) getPlayerChatInput { element ->
                    list.add(element)
                }
                else if (invClickEvent.isRightClick) getPlayerChatInput { element ->
                    list.remove(element)
                }
                else if (invClickEvent.click == ClickType.MIDDLE) list.removeLast()
                else return@onClick
                callback(list)
                val completeLore = mutableListOf<String>()
                completeLore.addAll(lore)
                for (string in list)
                    completeLore += "<gray> - <white>$string"
                completeLore += ""
                completeLore += "<yellow>Left Click to add element"
                completeLore += "<yellow>Middle Click to remove last element"
                completeLore += "<yellow>Right Click to remove specified element"
                this@list.lore(completeLore)
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
     * Create a button to Custom menu
     * @param id target of item/button placement id.
     * @param init body/builder for the button
     * @throws [NullPointerException] if [id] is not exist in [layout]
     */
    public fun createButton(id: Int, init: ClickableButton.() -> Unit) {
        button(id, init)
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
    public fun open(player: Player, forceUpdate: Boolean = false): Inventory {
        if (forceUpdate) {
            build()
            player.openInventory(inv)
            return inv
        }
        player.openInventory(player.jplayer().inventories.get(title) {
            build()
            inv
        })
        return inv
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

