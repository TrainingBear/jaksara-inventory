# Component: Paginator<T>

Purpose

A small helper for paging over collections of elements.

Public surface

- Constructor: Paginator(viewSize: Int, elements: Collection<T>)
- Properties: page: Int, totalPages: Int
- Methods: get(): List<T>, next(), prev()

Notes

- Buttons use Paginator to display a subset of ClickableButton items mapped to layout slots.
- When using ButtonHandler.fill, the Paginator's viewSize is set to the number of mapped slots for that id.
