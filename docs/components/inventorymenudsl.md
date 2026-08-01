# Component: InventoryMenuDsl

Responsibility

InventoryMenuDsl is the menu builder and InventoryHolder. It defines the layout, holds ButtonHandler / futureButton, and builds Inventory instances.

Key points

- Constructor: internal InventoryMenuDsl(title: String, plugin: Plugin) — create menus via CustomMenu.createMenu(name, plugin) instead of direct construction.
- Properties: inv (Inventory), layout (IntArray), indexedLayout (map of id -> slot indices), executor (map of delta->ClickableButton), futureButton (map id->ButtonHandler)
- Functions: layout(vararg), button(id, init), createButton, fill, open(player, forceUpdate), build(), close(event)

Lifecycle

- Use CustomMenu.createMenu to obtain an instance. `build()` creates the Inventory sized to layout.
- `open(player)` uses per-player cache to provide independent instances.
- On InventoryOpenEvent, InventoryMenuListener calls `ButtonHandler.build(true)` for each configured handler.

Thread-safety and limitations

- Designed for main-thread use on the Bukkit scheduler. Do not call menu building methods from asynchronous threads.
- InventoryMenuDsl uses persistent metadata keys derived from the menu title to map items to executors; titles should be unique per menu to avoid key collisions.

Usage

```kotlin
val menu = CustomMenu.createMenu("Title", plugin) {
  layout( ... )
  button(1) { ... }
}
menu.open(player)
```