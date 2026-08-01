ClickableButton
================

Overview
--
ClickableButton represents a clickable slot/button inside an InventoryMenuDsl. Configure appearance and behavior via builder lambdas.

Key properties
- handler: ButtonHandler — owner handler
- slotIndex: Int — slot index in the inventory
- builder: ClickableButton.() -> Unit — builder lambda invoked on build
- event: InventoryOpenEvent — derived from handler.root.event
- executor: ExecutionContext.() -> Unit — the click callback
- material: () -> Material — supplier of material (supports dynamic values)
- title: () -> String — supplier for title (supports dynamic values)
- lore: () -> List<Component> — supplier for lore
- item: ItemStack — the underlying ItemStack
- border: Boolean — when true, display flags applied
- visible: Boolean — visibility flag

Functions (doc comments from source)

- title(title: String)
  Sets a fixed title for this button.
  @param title The title to display.

- title(title: () -> String)
  Sets the title provider. The supplied lambda is invoked whenever the button's title is requested, allowing the title to be determined dynamically at runtime.
  @param title A function that returns the title to display.

- title(titleSupplier: Supplier<String>)
  Java-friendly overload: set the title using a Supplier<String>.

- getTitleString(): String
  Java-friendly getter for the computed title.

- material(material: Material)
  Set this button to a fixed material.

- material(material: () -> Material)
  Sets the material supplier. The lambda is invoked whenever the material is needed, allowing dynamic materials.

- material(materialSupplier: Supplier<Material>)
  Java-friendly overload.

- getMaterialType(): Material
  Returns the resolved material.

- onClick(exec: ExecutionContext.() -> Unit)
  Set the execution callback when button is clicked.
  @param exec callback block

- onClick(consumer: Consumer<ExecutionContext>)
  Java-friendly overload: accept Consumer<ExecutionContext>.

- lore(vararg lines: Component)
  Set lore using Component objects.

- loreAsComponents(lines: List<Component>)
  Set lore using a list of Components.

- lore(vararg lines: String)
  Set lore using MiniMessage strings.

- lore(lines: List<String>)
  Set lore using a list of MiniMessage strings.

- setVisible(state: Boolean)
  Make this button disappear or appear. visible is true by default.
  @param state when true it will appear; otherwise it will disappear. Calls refresh() on change.

- builder(init: ClickableButton.() -> Unit)
  Set a button builder lambda. builder will get initialized on build.
  Java-friendly overload accepts Consumer<ClickableButton>.

- internal fun build()
  Invokes builder and refresh().

- internal fun rebuildAll()
  Triggers handler.build(true) to rebuild all pages for this handler.

- internal fun refresh()
  Refresh or update this ClickableButton: resolves material/title/lore, writes persistent data key, and places the ItemStack into the inventory. If not visible, clears the slot.

- getHead(): ItemStack
  Get player head texture (PLAYER_HEAD with owningPlayer set to the menu opener).
