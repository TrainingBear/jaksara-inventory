# Cookbook — Recipes

Confirmation menu

Use the provided helper:

```kotlin
ConfirmationMenu.open("Confirm", listOf("Are you sure?"), player, plugin, object: ConfirmationCallback {
  override fun confirm(event: InventoryClickEvent) { /* do it */ }
  override fun cancel(event: InventoryClickEvent) { /* cancelled */ }
})
```

Paginated menu

- Use `ButtonHandler.fill(...)` to create a list of ClickableButton and store in an id. Use `openNextPage(id)` from ExecutionContext to switch pages.

Editable list

- Use `listButton(id, material, title, lore, list)` which uses `getPlayerChatInput` under the hood to add/remove elements.

Shop menu

- Create ClickableButton instances with dynamic price in title provider and in `onClick` check balance then perform purchase and `refresh()`.

Settings menu

- Use `optionButton` for small enumerations and `listButton` for multi-value settings.
