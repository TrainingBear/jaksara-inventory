# Migration Guide

No prior-version migration notes are present in the repository. If you are upgrading from an older version, check release notes (not included here).

If API changes are needed, examine usages of:
- `InventoryMenuDsl` builders
- `ClickableButton` properties and `onClick` signature

If you rely on internal persistent key generation (title-based namespaced keys), update titles accordingly.
