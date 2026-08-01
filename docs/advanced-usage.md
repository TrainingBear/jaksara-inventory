# Advanced Usage

Nested menus

- Opening another menu from a click handler is simple: call another menu's `open(player)` inside `onClick`.

Reusable components

- Use `CustomMenu.createButton` to build reusable ClickableButton instances and add them to multiple handlers.

Custom buttons and DSL extensions

- The DSL is Kotlin code: extend it with your own extension functions. For example, write an extension that creates a common settings row.

Asynchronous loading

- The library expects menu construction and inventory modifications on the server (main) thread.
- For long-running operations (e.g., remote lookups), perform the work asynchronously and then schedule a main-thread task to `rebuild()` or `refresh()` the menu when data arrives.

Chat input

- `ExecutionContext.getPlayerChatInput` opens a chat prompt using Paper's AsyncChatEvent interception; your callback receives the plain string and the original inventory is re-opened when input completes.

Extending the DSL safely

- Add extension functions that call into `InventoryMenuDsl` and build ClickableButton templates. Avoid reflection or mutating internal maps unless you know the internals.
