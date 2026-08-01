Jaksara Inventory
===================
A lightweight Kotlin-first inventory menu framework for Bukkit/Spigot plugins. It provides a Kotlin DSL and Java-friendly APIs to build interactive GUIs (menus) with clickable buttons, paginators, and helper ExecutionContext utilities.
# Installation

Requirements

- PaperMC API: 1.20.1 or newer
- Kotlin: plugin uses Kotlin JVM plugin `2.2.20` or newer
- Java: JDK 17 or newer

Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.trainingbear:jaksara-inventory:1.0.0") // replace with published coordinates
}
```

Gradle (Groovy)

```groovy
dependencies {
    implementation 'io.github.trainingbear:jaksara-inventory:1.0.0'
}
```

Maven

```xml
<dependency>
  <groupId>io.github.trainingbear</groupId>
  <artifactId>jaksara-inventory</artifactId>
  <version>1.0.0</version>
</dependency>
```

Initialization

Call CustomMenu.init(plugin) in your plugin `onEnable()` to register internal listeners (inventory / chat input).

```kotlin
class MyPlugin: JavaPlugin() {
  override fun onEnable() {
    CustomMenu.init(this)
  }
}
```


# Quick Cookie Clicker example (Kotlin DSL)

```kotlin
val menu = CustomMenu.createMenu("Cookie Clicker", plugin) {
    var cookies = 0
    // 1 row (9 slots)
    layout(0, 0, 0, 0, 1, 0, 0, 0, 0)

    // id 1 occupies slot(s) associated with layout id 1
    button(1) {
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
fun createMenu(player Player) {
    InventoryMenuDsl menu = CustomMenu.createMenu("Cookie Clicker", plugin);
    int cookie = 0;
    menu.layout(0, 0, 0, 0, 1, 0, 0, 0, 0);
    menu.button(0, b -> {
        b.material(Material.COOKIE);
        b.title(() -> "<yellow>Cookies: "+cookie);
        b.lore("<gray>Left click to +1, Right click to +10");
        b.onClick(ctx -> {
            // update state and refresh; use player-specific storage when needed
            cookies += invClickEvent.isLeftClick ? 1 : 10;
            ctx.refresh();
        });
    });
    menu.open(player);
}
```

See docs/ for full usage, API reference and more examples (Kotlin + Java).
