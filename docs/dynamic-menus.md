# Dynamic Menus

Updating contents

- `ClickableButton.refresh()` updates the item meta (title, lore, material) and re-registers the executor mapping; use `ExecutionContext.refresh()` to call it from within onClick.
- `refreshAll()` updates all buttons in the current handler list (ExecutionContext.refreshAll()).
- `rebuild()` and `rebuildAll()` re-run the button `builder` logic and then refresh — useful if you change `builder` behavior.

Pagination

- Use `ButtonHandler.fill(...)` to supply a list of ClickableButton instances. The handler will use an internal `Paginator` to display the current page.
- Use `ExecutionContext.openNextPage(id)` / `openPrevPage(id)` to navigate pages from callbacks.

Animations

- No explicit animation API exists. You can create animations by scheduling repeated updates (Calling refresh/rebuild via Bukkit scheduler) but be careful with performance.

Examples

```kotlin
onClick {
  // update a backing counter and refresh appearance
  counter++
  refresh()
}
```
