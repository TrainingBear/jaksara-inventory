InventoryMenuDsl
=================

Overview
--
Do not construct a Custom Menu manually. Instead use CustomMenu.createMenu to start making a Custom Menu.

Properties
- title: String — menu title
- plugin: Plugin — plugin used to create inventories
- inv: Inventory — the backing Inventory instance
- layout: IntArray — layout mapping (internal)
- indexedLayout: Map<Int, List<Int>> — maps layout id -> slot indices
- event: InventoryOpenEvent — the open event for the current instance
- executor: Map<Int, ClickableButton> — mapping of persistent data id -> ClickableButton
- futureButton: Map<Int, ButtonHandler> — handlers for layout ids

Functions (doc comments from source)

- layout(vararg layout: Int)
  Create layout that represent [buttons]/item placement. with each element of layout,
  identified as id of [buttons] or item.

  The size of layout must be divisible by 9 (size % 9 == 0), and layout size not exceed 9x6 (54).
  Parameters: layout - Layout id's
  Throws: IllegalArgumentException if the layout size is not divisible by 9, or if the layout size exceed 9x6

- internal fun getItem(id: Int): List<ItemStack?>
  @param id target of item/button placement id
  @return List of ItemStack at the given id
  @throws NullPointerException if id is not exist in layout

- exit(id: Int, material: Material): ClickableButton
  Template button for exit (close inventory)
  Throws: NullPointerException if id is not exist in layout

- border(id: Int, material: Material): ClickableButton
  Template button for border

- optionButton(...) : ClickableButton
  Create a template button for option selection.
  Parameters: id, material, title, lore, options, selectedIndex, visible, callback
  Returns: ClickableButton
  Throws: NullPointerException if id is not exist in layout
  See: listButton
  Notes: There are Java-friendly BiConsumer overloads and overloads that accept a selected element string.

- listButton(...) : ClickableButton
  Create a template button for list editing and selection.
  Parameters: id, material, title, lore, list, visible, callback
  Returns: ClickableButton
  Throws: NullPointerException if id is not exist in layout
  Behavior: Left click adds (chat input), right click removes specified (chat input), middle click removes last.

- button(id: Int, init: ClickableButton.() -> Unit): ClickableButton
  Create a button to Custom menu bound to layout id. Returns the created ClickableButton.
  Note: Java-friendly Consumer overload is available.

- createButton(id: Int, init: ClickableButton.() -> Unit): ClickableButton
  Alias for button; Java overload available.

- fill(id: Int, buttons: List<ClickableButton>): ButtonHandler
  Fill a whole layout with slot id to buttons as Paginator. Returns a ButtonHandler with the filled buttons.
  See ButtonHandler.openNextPage/openPrevPage for navigation.

- internal fun getButton(id: Int): ButtonHandler
  Returns the ButtonHandler for layout id. Throws NullPointerException if id not exist.

- open(player: Player, forceUpdate: Boolean = false): Inventory
  Make player open this inventory with an individually instance.
  If forceUpdate is true, always creates a new inventory instance for the player.

- internal fun build()
  Build a new inventory instance (creates Inventory with layout.size and title)

- close(event: InventoryCloseEvent)
  Called when player closes this inventory instance. Cleans up tasks and returns items that aren't managed by futureButton back to the player.

- getInventory()
  InventoryHolder implementation; returns the backing Inventory.
