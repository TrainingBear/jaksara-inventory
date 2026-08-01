# Events

InventoryMenuListener

- Listens to InventoryClickEvent, InventoryOpenEvent and InventoryCloseEvent.
- Click processing:
  1. Identify the clicked item by reading the persistent integer stored in item meta (namespacedKey derived from menu title).
  2. Cancel the raw event and invoke the mapped ClickableButton.executor with an ExecutionContext.
- Open processing: builds futureButton handlers for display.
- Close processing: calls InventoryMenuDsl.close(event) which closes any registered Closeables and returns loose items if necessary.

ChatInputListener

- Listens to Paper's AsyncChatEvent and, if a per-player chatInputCallback is present, cancels the chat and forwards plain text to the callback on the server thread.

Drag handling

- The current implementation does not register a specialized drag handler in InventoryMenuListener. Drag behavior is implicitly covered by cancelling clicks — complex drag-related interactions are not directly supported by library helpers.

Reopen behavior

- `ExecutionContext.getPlayerChatInput` closes the inventory to allow chat, then re-opens the original inventory for the player after receiving input.
