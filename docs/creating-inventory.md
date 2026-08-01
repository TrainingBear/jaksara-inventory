Creating an Inventory
=====================

Use CustomMenu.createMenu to build an InventoryMenuDsl instance.

Kotlin example:

```kotlin
val menu = CustomMenu.createMenu("My Menu", plugin) {
  // layout must be divisible by 9 (rows * 9)
  layout(
    0,0,0, 0,0,0, 0,0,0
  )

  // add buttons tied to layout ids
  button(0) {
    material(Material.DIAMOND)
    title("<green>Example")
    onClick { /* handle click */ }
  }
}

menu.open(player)
```

Java example uses overloads that accept Consumer lambdas:

```java
InventoryMenuDsl menu = CustomMenu.createMenu("My Menu", plugin);
menu.layout(0,0,0,0,0,0,0,0,0);
menu.button(0, b -> {
    b.material(Material.DIAMOND);
    b.title(() -> "Example");
    b.onClick(ctx -> {});
});
menu.open(player);
```

Tips:
- layout(vararg Int) maps ids to slot indices; each unique id groups slots for the same button/handler.
- Use menu.open(player, forceUpdate=true) to force a fresh instance.
