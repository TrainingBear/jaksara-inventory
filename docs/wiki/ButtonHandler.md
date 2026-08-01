ButtonHandler
=============

Overview
--
ButtonHandler manages a collection of ClickableButton instances for a single layout id. It uses a Paginator to page through elements when there are more buttons than slots.

Properties
- root: InventoryMenuDsl
- id: Int (layout id)
- buttons: Paginator<ClickableButton> (elements shown in the layout)
- lastPage: Int — used to avoid unnecessary rebuilds

Functions (doc comments from source)

- fill(elements: List<ItemStack>, action: (Int, ItemStack) -> ClickableButton.() -> Unit)
  Fill a whole layout with slot id to elements.
  Parameters: elements - the items to place; action - builder for each item/button.
  See openNextPage and openPrevPage for paginator navigation.

- fill(elements: List<ItemStack>, action: BiFunction<Integer, ItemStack, Consumer<ClickableButton>>) (Java overload)
  Java-friendly overload.

- fill(size: Int, action: (Int) -> ClickableButton.() -> Unit)
  Create `size` buttons using a builder that receives the index.

- fill(size: Int, action: Function<Integer, Consumer<ClickableButton>>) (Java overload)

- fill(vararg buttons: ClickableButton)
  Fill the handler with a vararg list of ClickableButton; handler will assign itself to each button.

- fill(buttons: List<ClickableButton>)
  Fill the handler with an existing list of ClickableButton.

- add(button: ClickableButton): ClickableButton
  Add an already-initialized button to the handler and return it.

- add(init: ClickableButton.() -> Unit): ClickableButton
  @return uninitialized button. Adds a new ClickableButton with builder `init` and returns it (Java overload accepts Consumer).

- remove(button: ClickableButton): Boolean
  Remove the button from the handler.

- openNextPage()
  Open the next page of the paginator and rebuild the handler.

- openPrevPage()
  Open the previous page of the paginator and rebuild the handler.

- internal fun build(force: Boolean = false)
  Internal build method: if the page hasn't changed and force=false, does nothing. Otherwise it iterates current page elements, sets slotIndex for each and invokes button.build(); empty slots are cleared.
