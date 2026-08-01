# Performance

Allocations

- ClickableButton clones ItemStacks when cloning buttons; reuse builders to avoid excessive object churn.
- Paginator stores lists of ClickableButton; prefer reusing ClickableButton builder functions where possible.

Updates

- `refresh()` only rewrites item meta and re-registers the executor mapping — cheaper than a full `rebuild()`.
- When updating many buttons, use `refreshAll()` or batch updates via scheduler ticks.

Event handling

- The library cancels InventoryClickEvent and routes execution to mapped handlers; the mapping is O(1) lookup against a HashMap of executors.
- Avoid heavy synchronous operations in handlers.

Object reuse

- Use `CustomMenu.createButton` to prepare reusable ClickableButton instances instead of creating new instances each time.
