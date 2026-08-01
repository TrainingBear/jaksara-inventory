Java interoperability guide — DSL-style builders

Overview
This project exposes the Kotlin DSL for inventory menus and now provides Java-friendly overloads that mirror the Kotlin DSL shape. Java users can use the same builder calls via Consumer/Supplier functional interfaces.

Quick start (example)

InventoryMenuDsl menu = CustomMenu.createMenu("<blue>Menu", plugin);
menu.layout( // vararg layout
  0,0,0,0,0,0,0,0,0,
  0,0,0,0,0,0,0,0,0
);

menu.button(13, btn -> {
  btn.material(Material.DIAMOND);
  btn.title("<green>Click me");
  btn.onClick(ctx -> { ctx.getPlayer().sendMessage("Clicked"); });
});

Option/list helper overloads
- optionButton(..., Consumer<String>) — receives selected option (note: Java must pass all non-default params, include lore param if skipping)
- listButton(..., Consumer<List<String>>) — receives current list whenever changed

Notes
- No Lombok is required; overloads are implemented in Kotlin and delegate to the existing DSL builders.
- Default Kotlin parameters are not visible to Java; Java callers must pass values for parameters that have defaults in Kotlin.

API reference pointers
- InventoryMenuDsl.button(int, Consumer<ClickableButton>)
- InventoryMenuDsl.optionButton(...) overloads
- InventoryMenuDsl.listButton(...) overloads
- ClickableButton.onClick(Consumer<ExecutionContext>)
- ClickableButton.title(Supplier<String>) and title(String)
- ButtonHandler.add(Consumer<ClickableButton>) and fill(...) overloads
