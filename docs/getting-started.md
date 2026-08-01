Getting Started
===============

1. Initialize the framework in your plugin's onEnable():

```kotlin
override fun onEnable() {
  CustomMenu.init(this)
}
```

2. Build a menu using the DSL or Java API (see examples).
3. Open the menu with menu.open(player) to show it to a player.

Notes:
- CustomMenu.init registers necessary listeners.
- Menus are created with CustomMenu.createMenu(name, plugin) { ... }
- Use menu.open(player) to open an instance; pass forceUpdate = true to rebuild.
