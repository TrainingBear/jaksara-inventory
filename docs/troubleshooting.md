# Troubleshooting

Menu does not open or items missing

- Ensure you called `CustomMenu.init(plugin)`.
- Ensure `layout` size is divisible by 9 and not larger than 54.

Clicks not triggering executor

- The persistent data integer is set during `ClickableButton.refresh()` — ensure items were built (InventoryOpenEvent triggers build).
- If you generate inventories manually, call `menu.build()` before opening.

Chat input not captured

- Paper's AsyncChatEvent is used. Ensure server runs a Paper-compatible build and ChatInputListener is registered.

Title collisions

- The persistent key is derived from menu title. If two menus use the same title, their namespaced key may collide; use unique titles or templated titles.
