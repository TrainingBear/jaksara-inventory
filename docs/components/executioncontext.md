# Component: ExecutionContext

Responsibility

Encapsulates runtime information available inside a button's onClick handler.

What is available

- invClickEvent: InventoryClickEvent — the raw event
- source: ClickableButton — the clicked button
- player: Player — convenience cast of whoClicked

Helper methods

- getPlayerChatInput(title: Title = ..., message: String, callback: (String) -> Unit)
  - Closes inventory, shows a title and waits for chat input. Uses a per-player `chatInputCallback`.
- getItem(id: Int): List<ItemStack?> — returns items at logical id
- getButton(id: Int): ButtonHandler — get handler for id
- refresh(), refreshAll() — refresh this button or all buttons
- rebuild(), rebuildAll() — rebuild from builder(s)
- openNextPage(id: Int), openPrevPage(id: Int) — paginate ButtonHandler at id

Notes

- `getPlayerChatInput` uses Paper's AsyncChatEvent and ChatInputListener to capture chat text; callback runs on main thread.
- Avoid long blocking operations in onClick; schedule async tasks via Bukkit scheduler if needed.
