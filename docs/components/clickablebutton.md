# Component: ClickableButton

Responsibility

Defines a single clickable item in the menu. A ClickableButton holds providers for material, title and lore, plus an executor block invoked on click.

Public surface (high level)

- Constructors: ClickableButton(handler: ButtonHandler) and a parameterless internal constructor (library constructs buttons for you)
- Properties:
  - handler: ButtonHandler
  - builder: ClickableButton.() -> Unit
  - executor: ExecutionContext.() -> Unit
  - material: () -> Material
  - title: () -> String
  - lore: () -> List<Component>
  - item: ItemStack
  - border: Boolean
  - visible: Boolean
- Methods:
  - title(String) / title(() -> String)
  - material(Material) / material(() -> Material)
  - onClick(ExecutionContext.() -> Unit)
  - lore(vararg String) / lore(vararg Component)
  - setVisible(Boolean)
  - getHead(): ItemStack

Lifecycle

1. A ClickableButton is created via `button(id)` or `ButtonHandler.add`.
2. `builder` is invoked during `build()` to initialize it.
3. `refresh()` writes the persistent metadata (an integer delta) and sets the Inventory slot item.
4. On click, the InventoryMenuListener maps that persistent integer back to `handler.root.executor` and constructs an ExecutionContext for the executor.

Thread safety

- Designed for main-thread operation. Modifying providers or calling `refresh()` should be performed on the server thread.

Limitations

- ItemStack instances are cloned when a button is cloned; if you mutate shared ItemStacks, take care to avoid unexpected sharing.
