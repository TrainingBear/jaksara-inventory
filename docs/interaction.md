Button Interaction
==================

When a button is clicked, its ClickableButton.executor (set via onClick) is invoked with ExecutionContext.

ExecutionContext highlights:
- player: the Player who clicked
- invClickEvent: the InventoryClickEvent
- getPlayerChatInput(title, message) { callback }
- getItem(id): List<ItemStack?> for the given layout id
- getButton(id): ButtonHandler for a handler id
- refresh(): refresh this button's display
- refreshAll(): refresh every button in the menu
- rebuild()/rebuildAll(): rebuild from builder lambdas
- openNextPage(id)/openPrevPage(id): paginator navigation

Example (Kotlin):

```kotlin
button(0) {
  material(Material.COOKIE)
  title { "Cookies: ${someCounter}" }
  onClick {
    someCounter++
    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
    refresh()
  }
}
```

Use ExecutionContext.getPlayerChatInput to capture chat text input from the player.
