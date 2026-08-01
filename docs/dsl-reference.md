DSL Reference
=============

InventoryMenuDsl
- layout(vararg Int)
- exit(id, material)
- border(id, material)
- optionButton(...) overloads
- listButton(...) overloads
- button(id, init)
- createButton(id, init)
- fill(id, buttons)
- open(player, forceUpdate=false)

ClickableButton
- title(String) / title(() -> String) / title(Supplier<String>)
- material(Material) / material(() -> Material)
- lore(vararg String) / lore(vararg Component)
- onClick(ExecutionContext.() -> Unit) / onClick(Consumer<ExecutionContext>)
- setVisible(Boolean)
- getHead()

ButtonHandler
- add / add(init)
- remove(button)
- fill(...) overloads
- openNextPage(), openPrevPage()

ExecutionContext
- getPlayerChatInput(...)
- getItem(id)
- getButton(id)
- refresh(), refreshAll()
- rebuild(), rebuildAll()
- openNextPage(id), openPrevPage(id)

Paginator<T>
- page, totalPages, get(), next(), prev()
