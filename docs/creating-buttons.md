Creating Buttons
=================

Buttons are represented by ClickableButton. Create them via InventoryMenuDsl.button(id) or CustomMenu.createButton.

Kotlin patterns:
- button(id) { ... } creates a button template bound to a layout id.
- createButton { ... } creates a standalone button.
- fill(id, listOf(buttons)) and ButtonHandler.fill allow paginated lists.

Common ClickableButton configuration:
- material(Material)
- title(String) or title { "dynamic" }
- lore(vararg String) or lore(Component...)
- onClick { ExecutionContext.() -> Unit }
- setVisible(true/false)
- builder { } to set a builder lambda applied on build

Java notes: many Kotlin functions have Java-friendly overloads accepting Consumer/Supplier/BiConsumer.
