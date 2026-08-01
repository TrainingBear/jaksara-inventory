# API Reference

This file documents every public class present in the repository. Content is derived from source code and exact signatures.

## InventoryMenuDsl
Purpose: Menu builder and InventoryHolder. Create via `CustomMenu.createMenu(name, plugin)`.
Constructor: internal InventoryMenuDsl(title: String, plugin: Plugin)
Public properties and functions (high level):
- var inv: Inventory
- fun layout(vararg layout: Int)
- fun exit(id: Int, material: Material): ClickableButton
- fun border(id: Int, material: Material): ClickableButton
- fun optionButton(...): ClickableButton (multiple overloads exist)
- fun listButton(...): ClickableButton
- fun button(id: Int, init: ClickableButton.() -> Unit): ClickableButton
- fun createButton(id: Int, init: ClickableButton.() -> Unit): ClickableButton
- fun fill(id: Int, buttons: List<ClickableButton>): ButtonHandler
- fun open(player: Player, forceUpdate: Boolean = false): Inventory
- internal fun build()

Usage example is shown in Quick Start.

## ClickableButton
Purpose: Encapsulates a clickable item and its click executor.
Constructors: ClickableButton(handler: ButtonHandler) and internal default constructor
Public properties:
- var handler: ButtonHandler
- var builder: ClickableButton.() -> Unit
- var executor: ExecutionContext.() -> Unit
- var material: () -> Material
- var title: () -> String
- var lore: () -> List<Component>
- var item: ItemStack
- var border: Boolean
- var visible: Boolean
Public methods:
- title(String) / title(() -> String)
- material(Material) / material(() -> Material)
- onClick(ExecutionContext.() -> Unit)
- lore(vararg String) / lore(vararg Component)
- setVisible(Boolean)
- getHead(): ItemStack

## ButtonHandler
Purpose: Manages a paginated group of ClickableButton instances.
Constructor: ButtonHandler(root: InventoryMenuDsl, id: Int)
Public methods: fill(...), add(...), remove(...), openNextPage(), openPrevPage()

## ExecutionContext
Purpose: Runtime context inside `onClick` handler
Constructor: internal ExecutionContext(invClickEvent: InventoryClickEvent, source: ClickableButton)
Properties: invClickEvent, source, player
Methods: getPlayerChatInput(...), getItem(id), getButton(id), refresh(), refreshAll(), rebuild(), rebuildAll(), openNextPage(id), openPrevPage(id)

## CustomMenu (object)
Purpose: Factory and initialization
Methods: init(plugin), createMenu(name, plugin, init), createButton(...), createMenu(name, plugin)
Properties: expireAfterAccessDuration

## Paginator<T>
Purpose: simple paging helper
Constructor: Paginator(viewSize: Int, elements: Collection<T>)
Properties: page, totalPages; methods: get(), next(), prev()

## PlayerData
Purpose: Per-player storage.
Properties: chatInputCallback: ((String) -> Unit)?, inventories: Caffeine Cache<String, Inventory>

## ConfirmationMenu
Purpose: Small helper menu exposing `open(title, description, player, plugin, callback)`.


Notes

- Java-friendly overloads exist for many builders accepting Consumer/BiConsumer.
- Internal helpers (namespacedKey, cache, jplayer) are internal utilities and not part of the public stable API.
