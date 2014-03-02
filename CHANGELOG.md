# Changelog

## [0.1.0] - 2026-03-18

### Added

- `gracefulShutdown {}` DSL for defining shutdown handlers

- Priority-ordered handler execution

- JVM shutdown hook installation

- Idempotent trigger (safe to call multiple times)

- `RequestTracker` for draining in-flight work

- Global timeout for all handlers
