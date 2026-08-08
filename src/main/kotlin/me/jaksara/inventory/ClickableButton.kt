package me.jaksara.inventory

import me.jaksara.inventory.annotation.Button
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

@Button
public class ClickableButton internal constructor() : Cloneable {
    public lateinit var handler: ButtonHandler

    public constructor(handler: ButtonHandler) : this() {
        this.handler = handler
    }

    internal var slotIndex: Int = 0
    public var builder: ClickableButton.() -> Unit = {}
    public val event: InventoryOpenEvent
        get() = handler.root.event
    public var executor: ExecutionContext.() -> Unit = {
//        Bukkit.broadcast("Default executor invoked!".deserialize())
    }
    public var material: () -> Material = { Material.BEDROCK }
    public var title: () -> String = { "example" }
    public var lore: () -> List<Component> = { listOf() }
    public var item: ItemStack = ItemStack(material())
    public var border: Boolean = false
    public var visible: Boolean = true
        private set

    public override fun clone(): ClickableButton {
        val clone = super.clone() as ClickableButton
        clone.item = item.clone()
        return clone
    }

    public fun getWrapper(): ButtonWrapper = ButtonWrapper(this)


    /**
     * Sets a fixed title for this button.
     *
     * @param title The title to display.
     */
    public fun title(title: String) {
        this.title = { title }
    }

    /**
     * Sets the title provider.
     *
     * The supplied lambda is invoked whenever the button's title is requested,
     * allowing the title to be determined dynamically at runtime.
     *
     * @param title A function that returns the title to display.
     */
    public fun title(title: () -> String) {
        this.title = title
    }

    /** Java-friendly overload: set the title using a Supplier<String> */
    public fun title(titleSupplier: java.util.function.Supplier<String>) {
        this.title = { titleSupplier.get() }
    }

    /** Java-friendly getter for the computed title */
    @JvmName("getTitleString")
    public fun getTitleString(): String = title()

    /**
     * Set this button to a fixed material
     * @param material the item material type
     */
    public fun material(material: Material) {
        this.material = { material }
    }

    /**
     * Sets the material supplier.
     *
     * The lambda is invoked whenever the material is needed, allowing the
     * material to be determined dynamically instead of being stored as a
     * fixed value.
     * @param material A function that returns the material to use.
     */
    public fun material(material: () -> Material) {
        this.material = material
    }

    /** Java-friendly overload: provide material via Supplier<Material> */
    public fun material(materialSupplier: java.util.function.Supplier<Material>) {
        this.material = { materialSupplier.get() }
    }

    @JvmName("getMaterialType")
    public fun getMaterialType(): Material = material()

    /**
     * Set the execution callback when button is clicked
     * @param exec callback block
     */
    public fun onClick(exec: ExecutionContext.() -> Unit) {
//        Bukkit.broadcast("Registered ${title()} as $exec".deserialize())
        executor = exec
    }

    /** Java-friendly overload: accept Consumer<ExecutionContext> */
    public fun onClick(consumer: java.util.function.Consumer<ExecutionContext>) {
        executor = { consumer.accept(this) }
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
     * Make this button disappear or appear. [visible] is true by default
     * @param state when true, it will appear. or else it will disappear.
     */
    public fun setVisible(state: Boolean) {
        if (state == visible) return
        visible = state
        refresh()
    }

    /**
     * Set a button [builder] for this button.
     *
     * [builder] will get initialized on [build]
     */
    public fun builder(init: ClickableButton.() -> Unit) {
        builder = init
    }

    /** Java-friendly builder setter */
    public fun builder(init: java.util.function.Consumer<ClickableButton>) {
        builder = { init.accept(this) }
    }

    internal fun build() {
        builder(this)
        refresh()
    }

    internal fun rebuildAll() {
        handler.build(true)
    }

    /**
     * Refresh or update this [ClickableButton]
     */
    internal fun refresh() {
        if (visible) {
            item.type = material()
            val delta = (handler.id * 1000) + slotIndex
            item.editMeta {
                if (border)
                    it.addItemFlags(*ItemFlag.entries.toTypedArray())
                it.displayName(title().deserialize())
                it.lore(lore.invoke())
                it.persistentDataContainer.set(handler.root.title.namespacedKey(), PersistentDataType.INTEGER, delta)
            }
            handler.root.executor[delta] = this

            handler.root.inv.setItem(slotIndex, item)
//            Bukkit.broadcast("${title()} registered with id: $delta. task: $executor".deserialize())
        } else handler.root.inv.setItem(slotIndex, AIR)

    }

    /**
     * Get player head texture
     */
    public fun getHead(): ItemStack {
        return ItemStack(Material.PLAYER_HEAD).also {
            val meta = it.itemMeta as SkullMeta
            meta.owningPlayer = handler.root.event.player as OfflinePlayer?
        }
    }
}