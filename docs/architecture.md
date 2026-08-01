# Architecture

The library is intentionally small. Core runtime pieces:

- Menu (InventoryMenuDsl)
- Session (PlayerData cache)
- Listener (InventoryMenuListener)
- Button (ClickableButton)
- ExecutionContext (callback runtime)

Mermaid diagram

```mermaid
graph TD
    Player -- open --> Menu[CustomMenu]
    Player -- click button --> Listener[InventoryMenuListener]
    Player -- have --> session[Session]
    Menu -- store cache --> session
    Menu -- contains --> ButtonHandler
    ButtonHandler -- contains --> buttons[Buttons]
    buttons  --> Item[ItemStack]
    buttons  --> title[Title]
    buttons  --> lore[Lore]
    buttons -- ExecutionContext --> executor[onClick Callback]
    Listener -- invokes --> executor
    executor -- execute --> Action
```

Notes

- Persistent item metadata (NamespacedKey derived from menu title) is used to map ItemStacks to ClickableButton instances so that InventoryClickEvent handlers can find the correct executor.
- Per-player inventory cache allows independent menu instances per player.
