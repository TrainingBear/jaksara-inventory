# Best Practices

- Initialize `CustomMenu` once in `onEnable()`.
- Keep menu titles unique when you rely on persistent namespaced keys.
- Use providers (title { ... }) for dynamic text and call `refresh()` to update the UI.
- Prefer `refresh()` over full `rebuild()` when changing only title/lore/material.
- Avoid heavy computation in `onClick`; schedule background work and update menu on completion.
- Use Java-friendly overloads in Java code to preserve readability.
