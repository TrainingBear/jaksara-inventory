# Core Concepts

Overview

The library centers on a small set of cooperating types:

- InventoryMenuDsl — top-level menu builder and inventory holder
- ClickableButton — describes a button (material, title, lore, executor)
- ButtonHandler — manages a slot group and pagination (fills many slots)
- ExecutionContext — runtime context passed into click callbacks
- CustomMenu — factory + initialization object
- PlayerData / cache — per-player session storage (chat callbacks, inventory cache)

How they work together (brief)

1. Define a menu with `CustomMenu.createMenu(name, plugin) { ... }` using `layout` and `button` builders.
2. When a player opens the inventory, InventoryMenuListener calls `ButtonHandler.build` for each configured id to prepare visible items.
3. Each ClickableButton, when built, writes a persistent integer into the item meta (NamespacedKey derived from menu title) so the listener can map clicks back to the ClickableButton.
4. When a click happens, InventoryMenuListener constructs an ExecutionContext and invokes the ClickableButton.executor block.
5. ExecutionContext helpers (refresh, rebuild, pagination, chat input) allow dynamic updates and cross-menu operations.

Menu lifecycle

- Build: menu.build() creates an Inventory sized to layout.
- Open: menu.open(player) uses a per-player cache so each player can have independent menu instances.
- Click: handled by InventoryMenuListener — events are cancelled and executor invoked.
- Close: menu.close(event) is called on InventoryCloseEvent; tasks (Closeables) are closed and leftover items are returned to player inventory when appropriate.

Session management

- PlayerData stored in a Caffeine cache keyed by player UUID (internal `cache` and `Player.jplayer()` helpers).
- Each player has a `chatInputCallback` and an `inventories` cache for per-player Inventory instances.
- CustomMenu.expireAfterAccessDuration controls how long the per-player inventory cache keeps entries.

Event handling

- InventoryMenuListener handles open/close/click events and delegates to the builders and ExecutionContext.
- ChatInputListener listens to Paper's AsyncChatEvent and forwards text to a stored `chatInputCallback` while ensuring Bukkit scheduler runs the callback on the main thread.

Dynamic updates

- ClickableButton.refresh updates item meta (title, lore, material) and re-registers executor in the menu's executor map.
- ExecutionContext.refresh/refreshAll/rebuild/rebuildAll expose these operations to callbacks.
