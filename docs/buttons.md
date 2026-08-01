# Buttons

Common button helpers provided by InventoryMenuDsl

- button(id) — base builder for custom buttons
- border(id, material) — convenience button template for decorative borders
- exit(id, material) — convenience button that closes the inventory
- optionButton(...) — template for option selectors (left/right to cycle)
- listButton(...) — editable list helper that uses chat input to add/remove items

Examples

```kotlin
button(6) {
  material(Material.LIME_CONCRETE)
  title("<green><bold>Confirm")
  onClick { player.closeInventory(); /* callback */ }
}

optionButton(1, Material.PAPER, "Choose", options = listOf("One","Two"), selectedIndex = 0) { sel ->
  // sel = selected option
}
```

Best practices

- Use these helpers when they fit common patterns; they manage UI text and interactions for you.
- Call `refresh()` after mutating backing state so UI updates.
