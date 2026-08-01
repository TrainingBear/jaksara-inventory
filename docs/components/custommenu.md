# Component: CustomMenu (object)

Purpose

Factory and global configuration for the library.

API

- CustomMenu.init(plugin: Plugin) — registers internal listeners (must be called from onEnable)
- CustomMenu.createMenu(name: String, plugin: Plugin, init: InventoryMenuDsl.() -> Unit): InventoryMenuDsl — create + build a menu
- CustomMenu.createMenu(name: String, plugin: Plugin): InventoryMenuDsl — Java-friendly overload returning editable InventoryMenuDsl
- CustomMenu.createButton(handler: ButtonHandler? = null, init: ClickableButton.() -> Unit) — helper to create reusable ClickableButton instances
- CustomMenu.expireAfterAccessDuration: Duration — controls per-player inventory cache expiry

Notes

- Call `CustomMenu.init(plugin)` once per plugin instance.
- Use `createMenu` to construct menus; do not instantiate InventoryMenuDsl directly.
