API Reference (short)
=====================

CustomMenu
- init(plugin)
- createMenu(name, plugin, init)
- createMenu(name, plugin) // returns editable menu
- createButton(handler?, init)

InventoryMenuDsl
- Core DSL class representing a menu. Build layout, buttons, and open inventories.

ClickableButton
- Represents a clickable slot. Configure appearance and onClick behavior.

ButtonHandler
- Manages collections/paginators of ClickableButton and builds pages.

ExecutionContext
- The runtime context available inside onClick handlers (player, event, helper methods).

Paginator
- Simple paginator utility used by ButtonHandler to page through elements.
