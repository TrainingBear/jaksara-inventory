# Introduction

What this framework is

Jaksara Inventory is a lightweight Kotlin-first DSL to build interactive inventory GUIs for Bukkit/Paper plugins. It provides a concise Kotlin DSL and Java-friendly overloads to declare menus, buttons and paginators without manually handling InventoryClickEvent wiring.

Features

- Kotlin DSL for declaring menus and buttons
- Java-friendly overloads for most builders
- Clickable buttons with dynamic title/lore/material providers
- Paginators and ButtonHandler for multi-item layouts
- Option and list helper buttons (optionButton, listButton)
- Chat-input helper to collect free-form text from players

Design philosophy

- Make common GUI patterns trivial to express (buttons, paginators, confirmations)
- Minimize boilerplate and event plumbing — library registers listeners and maps clicks to button executors using persistent item metadata
- Keep API small and explicit: only a few building blocks (InventoryMenuDsl, ClickableButton, ButtonHandler, ExecutionContext)

Why use this instead of manually handling InventoryClickEvent

- Correctness: listeners and persistent metadata avoid slot-id mixing and accidental item movement
- Productivity: DSL and helpers reduce repetitive code for layouts, pagination, and chat input
- Reusability: ButtonHandler and builder patterns make pages and components easy to reuse


Notes

This documentation is generated from the current repository sources. All examples use the exact public APIs present in the codebase.
