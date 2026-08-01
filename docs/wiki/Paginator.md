Paginator
=========

Overview
--
Paginator is a small utility used by ButtonHandler to page through collections of elements.

Constructor
- Paginator(viewSize: Int, elements: Collection<T>)

Properties
- page: Int (current page, default 1)
- totalPages: Int (computed)

Functions
- get(): List<T>
  Returns the sublist for the current page. Calculates start = (page-1) * viewSize and end = min(start + viewSize, size).

- next(): List<T>
  Advances page (clamped to totalPages) and returns the current page list.

- prev(): List<T>
  Moves page backward (clamped to 1) and returns the current page list.
