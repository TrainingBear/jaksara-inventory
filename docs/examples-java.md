Examples (Java)
===============

Cookie Clicker (conceptual)

```java
InventoryMenuDsl menu = CustomMenu.createMenu("Cookie Clicker", plugin);
menu.layout(0,0,0,0,0,0,0,0,0);

AtomicInteger cookies = new AtomicInteger(0);
menu.button(0, b -> {
    b.material(Material.COOKIE);
    b.title(() -> "Cookies: " + cookies.get());
    b.lore("<gray>Left click +1, Right click +10");
    b.onClick(ctx -> {
        if (ctx.invClickEvent.isLeftClick()) cookies.incrementAndGet();
        else cookies.addAndGet(10);
        ctx.refresh();
    });
});
menu.open(player);
```

Note: for per-player state, attach data to player-specific storage rather than global variables.
