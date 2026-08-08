package me.jaksara.inventory

import org.bukkit.inventory.ItemStack

public interface ButtonHandlerWrapper {
    public fun openNextPage()
    public fun openPrevPage()
    public fun fill(elements: List<ItemStack>, action: (Int, ItemStack) -> ClickableButton.() -> Unit)

    /** Java-friendly overload for fill with element builder as BiFunction<Integer, ItemStack, Consumer<ClickableButton>> */
    public fun fill(
        elements: java.util.List<ItemStack>,
        action: java.util.function.BiFunction<Int, ItemStack, java.util.function.Consumer<ClickableButton>>
    )

    public fun fill(size: Int, action: (Int) -> ClickableButton.() -> Unit)

    /** Java-friendly overload for fill with size and builder Function<Integer, Consumer<ClickableButton>> */
    public fun fill(size: Int, action: java.util.function.Function<Int, java.util.function.Consumer<ClickableButton>>)

    public fun fill(vararg buttons: ClickableButton)

    public fun fill(buttons: List<ClickableButton>)

    public fun add(button: ClickableButton): ClickableButton

    /**
     * @return uninitialized button.
     */
    public fun add(init: ClickableButton.() -> Unit): ClickableButton

    /** Java-friendly overload to add an uninitialized button using a Consumer<ClickableButton> */
    public fun add(init: java.util.function.Consumer<ClickableButton>): ClickableButton

    public fun remove(button: ClickableButton): Boolean

    public fun handle(): ButtonHandler = this as ButtonHandler
    public fun build(force: Boolean = false) {
        handle().buildInternal(force)
    }
}