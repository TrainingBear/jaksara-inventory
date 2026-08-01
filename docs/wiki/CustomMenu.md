CustomMenu
=========

Overview
--
CustomMenu is the top-level object that initializes listeners and provides factory methods for menus and buttons.

Properties & functions (doc comments from source)

- expireAfterAccessDuration: Duration
  Expire duration after player access any Custom Inventory.
  When the time has come, it automatically closes the inventory and frees it from memory.

- init(plugin: Plugin)
  Registers required listeners (ChatInputListener and InventoryMenuListener) with the plugin manager and logs successful initialization.

- createMenu(name: String, plugin: Plugin, init: InventoryMenuDsl.() -> Unit): InventoryMenuDsl
  Create a new inventory menu.
  @param name inventory title
  @param plugin the plugin instance (required for inventory creation)
  @param init builder block
  @return the built menu

- createButton(handler: ButtonHandler? = null, init: ClickableButton.() -> Unit): ClickableButton
  Create a ClickableButton instance (optionally bound to a handler). The builder lambda is invoked immediately and assigned as the button.builder as well. Java-friendly Consumer overload exists.

- createMenu(name: String, plugin: Plugin): InventoryMenuDsl
  Java-friendly createMenu that returns an editable InventoryMenuDsl instance (no init lambda).
