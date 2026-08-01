# DSL Guide

This page describes the public DSL functions you use when building menus. All signatures below reflect the current code.

layout(vararg layout: Int)
- Purpose: Define the inventory layout mapping logical `id` values to absolute slot indices.
- Parameters: a variable-length list of integers. The total number of slots must be divisible by 9 (e.g., 9, 18, ..., 54).
- Returns: Unit
- Example:
```kotlin
layout(
  0,0,0,1,1,1,0,0,0
)
```
- Common mistakes: passing a length not divisible by 9 → IllegalArgumentException.

button(id: Int, init: ClickableButton.() -> Unit): ClickableButton
- Purpose: Create a button template at logical id and return a ClickableButton for configuration.
- Parameters: id — layout id where this button will be placed; init — builder block.
- Returns: ClickableButton
- Example:
```kotlin
button(1) {
  material(Material.DIAMOND)
  title("Buy")
  onClick { player.sendMessage("Bought!") }
}
```
- Best practices: Use `title { ... }` lambda when title depends on runtime state and call `refresh()` inside callbacks to update appearance.

createButton(handler: ButtonHandler? = null, init: ClickableButton.() -> Unit)
- Java-friendly overloads exist. Use `CustomMenu.createButton(...)` to build reusable button instances.

fill(id: Int, buttons: List<ClickableButton>): ButtonHandler
- Purpose: Use a ButtonHandler/Paginator to fill every slot mapped to id with a sequence of buttons.

optionButton / listButton / exit / border
- Purpose: Convenience templates available on InventoryMenuDsl; see source for exact overloads and parameters.

open(player: Player, forceUpdate: Boolean = false): Inventory
- Opens the menu for a player. `forceUpdate` forces a fresh inventory instance.

getButton/getItem/getButtonHandler (ExecutionContext)
- ExecutionContext exposes helper functions for runtime callbacks.

Notes

- Java-friendly overloads accept `Consumer` or `BiConsumer` where appropriate.
- Use `CustomMenu.init(plugin)` to register internal listeners before opening menus.
- Prefer `refresh()` over full `rebuild()` when changing only title/lore/material.
