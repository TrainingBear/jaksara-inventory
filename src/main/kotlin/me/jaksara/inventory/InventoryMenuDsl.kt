package me.jaksara.inventory

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


@Menu
public open class InventoryMenuDsl(public var title: String, public val plugin: Plugin) : InventoryHolder, Cloneable {
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
     * Create layout with size of divisor 9. (size % 9 == 0) Each element
     * represents id of item. throw [IllegalArgumentException] if layout size
     * not divisible by 9.
     * @param layout Layout id's
     */
    public fun layout(vararg layout: Int) {
        layout {
            layout
        }
    }

    /**
     * Create layout with size of divisor 9. (size % 9 == 0) Each element
     * represents id of item. throw [IllegalArgumentException] if layout size
     * not divisible by 9.
     * @param layout Layout id's
     */
    public fun layout(out: () -> IntArray) {
        layout = out()
        if (layout.size % 9 != 0) throw IllegalArgumentException("You can't create an inventory with size of ${layout.size}, the inventory size must be divisible by 9 and != 0")
        indexedLayout.clear()
        for ((index, i) in layout.withIndex()) {
            indexedLayout.computeIfAbsent(i) { mutableListOf() }.add(index)
        }
    }

    /**
     * @param id target item id
     * @return List of ItemStack at the given id
     */
    public fun getItem(id: Int): List<ItemStack?> {
        return indexedLayout[id]!!.map { inv.getItem(it) }
    }

    /**
     * Template button for exit (close inventory)
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
     * @param id target id
     * @param material material
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
     * @param id target id
     * @param material target material
     * @param name button title
     * @param element options
     * @param lore description button
     * @param callback callback when selection changes
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
     * Create a custom button with a body
     * @param id target id
     * @param init body/builder for the button
     */
    public fun button(id: Int, init: ClickableButton.() -> Unit) {
        futureButton[id] = init
    }

    public fun getButton(id: Int): ClickableButton {
        return buttons[id]!!
    }

    /**
     * Make player open this inventory
     * @param player target player
     * @param forceUpdate if true: it create a new inventory instance
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

    public fun build() {
        inv = plugin.server.createInventory(this, layout.size, title.deserialize())
    }

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

