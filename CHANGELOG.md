# Changelog

## 0.1.3 (2026-03-22)

- Standardize CHANGELOG format

## 0.1.2 (2026-03-20)

- Standardize README: fix title, badges, version sync, remove Requirements section

## 0.1.1 (2026-03-18)

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## 0.1.0 (2026-03-18)

- `gracefulShutdown {}` DSL for defining shutdown handlers
- Priority-ordered handler execution
- JVM shutdown hook installation
- Idempotent trigger (safe to call multiple times)
- `RequestTracker` for draining in-flight work
- Global timeout for all handlers
