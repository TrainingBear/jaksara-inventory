# Component: ButtonHandler

Responsibility

Manages a group of slots represented by a single layout id. It provides pagination over a list of ClickableButton instances.

Public surface

- Constructor: ButtonHandler(root: InventoryMenuDsl, id: Int)
- Properties: buttons: Paginator<ClickableButton>
- Key functions:
  - fill(vararg / List / size/action) — populate buttons
  - add(button) / add(init)
  - remove(button)
  - openNextPage(), openPrevPage()

Lifecycle

- ButtonHandler is stored in InventoryMenuDsl.futureButton and built on InventoryOpenEvent.
- `build(force: Boolean = false)` populates actual inventory slots for the current paginator page.

Notes

- Use `fill(elements) { index, item -> ... }` overload to create buttons from raw ItemStacks.
- Pagination state is kept in the Paginator instance (buttons.page).
