ExecutionContext
================

Overview
--
ExecutionContext is the runtime context provided to ClickableButton.onClick handlers. It contains the InventoryClickEvent, the source ClickableButton, and convenience helpers.

Properties
- invClickEvent: InventoryClickEvent — the raw event
- source: ClickableButton — the button that was clicked
- player: Player — convenience getter for the clicking player

Functions (doc comments from source)

- getPlayerChatInput(title: Title = ..., message: String = ..., callback: (String) -> Unit)
  Opens a chat input flow for the player: closes the inventory, shows a title/message, and sets a chatInput callback for the player's jplayer() wrapper. When input is received (unless it is "cancel" or "q"), callback is invoked. Re-opens the inventory and clears the chat callback afterward.

- getItem(id: Int): List<ItemStack?>
  @param id target of item/button placement id inside this inventory
  @return List of ItemStack at the given id
  @throws NullPointerException if id does not exist in this InventoryMenuDsl.layout

- getButton(id: Int): ButtonHandler
  @param id target of item/button placement id inside this inventory
  @return ButtonHandler
  @throws NullPointerException if id does not exist

- refresh()
  Refresh or update this button's appearance (reapplies lore/title/material).

- refreshAll()
  refresh() for every button inside this menu.

- rebuild()
  Rebuild this button entirely from ClickableButton.builder. Suggest using refresh() for appearance-only changes.

- rebuildAll()
  Rebuild every button in the menu.

- openNextPage(id: Int)
  Open the next paginator page for the handler at layout id.

- openPrevPage(id: Int)
  Open the previous paginator page for the handler at layout id.
