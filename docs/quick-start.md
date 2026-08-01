# Quick Start — Hello World

This example builds a simple inventory with a single button that updates a counter.

```kotlin
import me.jaksara.inventory.CustomMenu
import org.bukkit.Material

// initialize once in onEnable
CustomMenu.init(plugin)

val menu = CustomMenu.createMenu("Cookie Clicker", plugin) {
    var cookies = 0
    // 1 row (9 slots)
    layout(0,0,0,0,1,0,0,0,0)

    button(1) {
        material(Material.COOKIE)
        title { "<yellow>Cookies: $cookies" }
        lore("<gray>Left click to +1, Right click to +10")

        onClick {
            cookies += if (invClickEvent.isLeftClick) 1 else 10
            refresh() // update button text/lore
        }
    }
}

// open for a player
menu.open(player)
```

Notes

- Use `layout(vararg)` to map logical ids to inventory slots.
- Each `button(id)` maps to every slot mapped to that id in the layout.
- `onClick` receives an `ExecutionContext` with helpers to refresh, get items, open pages, and collect chat input.
