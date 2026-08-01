package me.jaksara.inventory

import org.bukkit.inventory.ItemStack

public class ButtonHandler(
    public val root: InventoryMenuDsl,
    public val id: Int,
) {
    public var buttons: Paginator<ClickableButton> = Paginator(root.indexedLayout[id]!!.size, emptyList())
    public var lastPage: Int = -1
        private set

    /**
     * Fill a whole layout with slot [id] to [elements]
     * @param elements the elements that will be placed inside layout
     * @param action builder for each item/button. with
     * @see [openNextPage]
     * @see [openPrevPage]
     */
    public fun fill(elements: List<ItemStack>, action: (Int, ItemStack) -> ClickableButton.() -> Unit) {
        val result = mutableListOf<ClickableButton>()
        elements.forEachIndexed { index, item ->
            val button = ClickableButton(this)
            val builder = action(index, item)
            builder.invoke(button)
            button.builder = builder
            result.add(button)
        }
        fill(result)
    }

    public fun fill(size: Int, action: (Int) -> ClickableButton.() -> Unit) {
        val result = mutableListOf<ClickableButton>()
        for (index in 0..<size) {
            val button = ClickableButton(this)
            val builder = action(index)
            builder.invoke(button)
            button.builder = builder
            result.add(button)
        }
        fill(result)
    }

    public fun fill(vararg buttons: ClickableButton) {
        this@ButtonHandler.buttons = Paginator(root.indexedLayout[id]!!.size, buttons.map {
            it.handler = this
            it
        })
    }

    public fun fill(buttons: List<ClickableButton>) {
        this@ButtonHandler.buttons = Paginator(root.indexedLayout[id]!!.size, buttons.map {
            it.handler = this
            it
        })
    }

    public fun add(button: ClickableButton): ClickableButton {
        button.handler = this
        buttons.add(button)
        return button
    }

    /**
     * @return uninitialized button.
     */
    public fun add(init: ClickableButton.() -> Unit): ClickableButton {
        val button = ClickableButton(this)
        button.builder = init
        buttons.add(button)
        return button
    }

    public fun remove(button: ClickableButton): Boolean {
        return buttons.remove(button)
    }

    /**
     * Open the next page
     */
    public fun openNextPage() {
        buttons.page = (buttons.page + 1).coerceIn(1, buttons.totalPages)
        build()
    }

    /**
     * Open the previous page
     */
    public fun openPrevPage() {
        buttons.page = (buttons.page - 1).coerceIn(1, buttons.totalPages)
        buttons.prev()
        build()
    }

    internal fun build(force: Boolean = false) {
        if (lastPage == buttons.page && !force) return
        lastPage = buttons.page
        val iterator = buttons.get().iterator()
        root.indexedLayout[id]!!.forEach { index ->
            if (iterator.hasNext()) {
                val button = iterator.next()
                button.slotIndex = index
                try {
                    button.build()
                } catch (e: Exception){
                    "Failed to load button with id: $id, name: ${button.title()}.!".error()
                    throw e
                }
            } else root.inv.setItem(index, AIR)
        }
//        root.plugin.server.broadcast("button with id: $id built!".deserialize())
    }
}