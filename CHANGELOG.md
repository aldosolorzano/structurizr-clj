# Changelog

## 0.2.0 - 2021-02-24
### BREAKING CHANGES
- Add `structurizr-clj.view` ns and migrated `structurizr-clj.core` and `structurizr-clj.render` functions related to views
- Add `structurizr-clj.style` ns and migrated `structurizr-clj.core` functions related to styles
- Inside `structurizr-clj.view`, two new functions, `add-element` and `remove-element`.
- Find detailed information about this change [here](https://github.com/aldosolorzano/structurizr-clj/pull/14)

## 0.1.1 - 2021-02-22
### Added
- JSON support, read and save workspace from/to JSON.
- `com.structurizr.model.Tags` in ns `structurizr-clj.tags`
- `com.structurizr.model.Shape` in ns `structurizr-clj.shape`

## 0.1.0 - 2021-02-18
### Added
- Add `defworkscape`, `defmodel`, `defviews` and `defstyles` macros.
- Publish workspace to Structurizr cloud.
- Support `PlantUML and Mermaid renders`.

### Comments
- This is in **alpha** stage, changes are very likely to happen.
