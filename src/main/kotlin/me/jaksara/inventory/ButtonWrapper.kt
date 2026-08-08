package me.jaksara.inventory

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * A delegation wrapper for ClickableButton that exposes all public and internal methods.
 * This wrapper allows clean access to both the public API and internal functionality.
 */
public class ButtonWrapper(private val clickableButton: ClickableButton) : Cloneable {

    // ============ Property Delegates ============

    public var handler: ButtonHandler
        get() = clickableButton.handler
        set(value) { clickableButton.handler = value }

    public var slotIndex: Int
        get() = clickableButton.slotIndex
        set(value) { clickableButton.slotIndex = value }

    public var builder: ClickableButton.() -> Unit
        get() = clickableButton.builder
        set(value) { clickableButton.builder = value }

    public val event: InventoryOpenEvent
        get() = clickableButton.event

    public var executor: ExecutionContext.() -> Unit
        get() = clickableButton.executor
        set(value) { clickableButton.executor = value }

    public var material: () -> Material
        get() = clickableButton.material
        set(value) { clickableButton.material = value }

    public var title: () -> String
        get() = clickableButton.title
        set(value) { clickableButton.title = value }

    public var lore: () -> List<Component>
        get() = clickableButton.lore
        set(value) { clickableButton.lore = value }

    public var item: ItemStack
        get() = clickableButton.item
        set(value) { clickableButton.item = value }

    public var border: Boolean
        get() = clickableButton.border
        set(value) { clickableButton.border = value }

    public var visible: Boolean
        get() = clickableButton.visible
        private set(value) { /* Read-only for external callers */ }

    // ============ Public Method Delegates ============

    public override fun clone(): ClickableButton = clickableButton.clone()

    /**
     * Sets a fixed title for this button.
     * @param title The title to display.
     */
    public fun title(title: String) {
        clickableButton.title(title)
    }

    /**
     * Sets the title provider.
     * @param title A function that returns the title to display.
     */
    public fun title(title: () -> String) {
        clickableButton.title(title)
    }

    /** Java-friendly overload: set the title using a Supplier<String> */
    public fun title(titleSupplier: Supplier<String>) {
        clickableButton.title(titleSupplier)
    }

    /** Java-friendly getter for the computed title */
    public fun getTitleString(): String = clickableButton.getTitleString()

    /**
     * Set this button to a fixed material
     * @param material the item material type
     */
    public fun material(material: Material) {
        clickableButton.material(material)
    }

    /**
     * Sets the material supplier.
     * @param material A function that returns the material to use.
     */
    public fun material(material: () -> Material) {
        clickableButton.material(material)
    }

    /** Java-friendly overload: provide material via Supplier<Material> */
    public fun material(materialSupplier: Supplier<Material>) {
        clickableButton.material(materialSupplier)
    }

    public fun getMaterialType(): Material = clickableButton.getMaterialType()

    /**
     * Set the execution callback when button is clicked
     * @param exec callback block
     */
    public fun onClick(exec: ExecutionContext.() -> Unit) {
        clickableButton.onClick(exec)
    }

    /** Java-friendly overload: accept Consumer<ExecutionContext> */
    public fun onClick(consumer: Consumer<ExecutionContext>) {
        clickableButton.onClick(consumer)
    }

    /**
     * Set lore using Component objects
     * @param lines lore lines as Components
     */
    public fun lore(vararg lines: Component) {
        clickableButton.lore(*lines)
    }

    /**
     * Set lore using Component objects
     * @param lines lore lines as Components
     */
    public fun loreAsComponents(lines: List<Component>) {
        clickableButton.loreAsComponents(lines)
    }

    /**
     * Set lore using Strings with MiniMessage support
     * @param lines lore lines as MiniMessage strings
     */
    public fun lore(vararg lines: String) {
        clickableButton.lore(*lines)
    }

    /**
     * Set lore using Strings with MiniMessage support
     * @param lines lore lines as MiniMessage strings
     */
    public fun lore(lines: List<String>) {
        clickableButton.lore(lines)
    }

    /**
     * Make this button disappear or appear. [visible] is true by default
     * @param state when true, it will appear. or else it will disappear.
     */
    public fun setVisible(state: Boolean) {
        clickableButton.setVisible(state)
    }

    /**
     * Set a button [builder] for this button.
     * [builder] will get initialized on [build]
     */
    public fun builder(init: ClickableButton.() -> Unit) {
        clickableButton.builder(init)
    }

    /** Java-friendly builder setter */
    public fun builder(init: Consumer<ClickableButton>) {
        clickableButton.builder(init)
    }

    /**
     * Get player head texture
     */
    public fun getHead(): ItemStack = clickableButton.getHead()

    // ============ Internal Method Delegates ============

    /**
     * Initialize the builder for this button
     */
    public fun build() {
        clickableButton.build()
    }

    /**
     * Rebuild all buttons in the handler
     */
    public fun rebuildAll() {
        clickableButton.rebuildAll()
    }

    /**
     * Refresh or update this button's display
     */
    public fun refresh() {
        clickableButton.refresh()
    }

    // ============ Getters for Wrapped Instance ============

    /**
     * Get the wrapped ClickableButton instance
     */
    public fun getWrapped(): ClickableButton = clickableButton
}