# FAQ

Q: Where do I register listeners?
A: Call `CustomMenu.init(plugin)` in `onEnable()`.

Q: How do I get per-player menu state?
A: Use `Player.jplayer()` internally; for your plugin, store state external to the menu and reference it from `onClick` handlers.

Q: Can I use this from Java?
A: Yes — Java-friendly overloads accepting Consumer/BiConsumer exist for many builders; `ExampleUsage.java` shows common patterns.

Q: Are drag events supported?
A: Not explicitly. The listener cancels clicks and maps items via persistent metadata; complex drag behavior is not provided out-of-the-box.
