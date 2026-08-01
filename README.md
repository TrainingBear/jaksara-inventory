jaksara-inventory
===================

A lightweight Kotlin-first inventory menu framework for Bukkit/Spigot plugins. It provides a Kotlin DSL and Java-friendly APIs to build interactive GUIs (menus) with clickable buttons, paginators, and helper ExecutionContext utilities.

Quick Cookie Clicker example (Kotlin DSL)

```kotlin
val menu = CustomMenu.createMenu("Cookie Clicker", plugin) {
  // 1 row (9 slots)
  layout(0,0,0,0,0,0,0,0,0)

  // id 0 occupies slot(s) associated with layout id 0
  button(0) {
    var cookies = 0
    material(Material.COOKIE)
    title { "<yellow>Cookies: $cookies" }
    lore("<gray>Left click to +1, Right click to +10")

    onClick {
      cookies += if (invClickEvent.isLeftClick) 1 else 10
      refresh() // update display
    }
  }
}

menu.open(player)
```

Java equivalent (simple)

```java
InventoryMenuDsl menu = CustomMenu.createMenu("Cookie Clicker", plugin);
menu.layout(0,0,0,0,0,0,0,0,0);
menu.button(0, b -> {
    b.material(Material.COOKIE);
    b.title(() -> "Cookies: 0");
    b.lore("<gray>Left click to +1, Right click to +10");
    b.onClick(ctx -> {
        // update state and refresh; use player-specific storage when needed
        ctx.refresh();
    });
});
menu.open(player);
```

See docs/ for full usage, API reference and more examples (Kotlin + Java).
