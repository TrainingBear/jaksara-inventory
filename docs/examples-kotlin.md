Examples (Kotlin)
=================

Cookie Clicker (simple)

```kotlin
val menu = CustomMenu.createMenu("Cookie Clicker", plugin) {
  layout(0,0,0,0,0,0,0,0,0)

  var cookies = 0

  button(0) {
    material(Material.COOKIE)
    title { "<yellow>Cookies: $cookies" }
    lore("<gray>Left click +1, Right click +10")
    onClick {
      cookies += if (invClickEvent.isLeftClick) 1 else 10
      player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
      refresh()
    }
  }
}

menu.open(player)
```

Paginator example and listButton/optionButton usage are shown in the full docs.
