# graceful-shutdown

[![CI](https://github.com/philiprehberger/kt-graceful-shutdown/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-graceful-shutdown/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/graceful-shutdown)](https://central.sonatype.com/artifact/com.philiprehberger/graceful-shutdown)

Graceful application shutdown with signal handling and ordered teardown.

## Requirements

- Kotlin 1.9+ / Java 17+

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.philiprehberger:graceful-shutdown:0.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>graceful-shutdown</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

```kotlin
import com.philiprehberger.gracefulshutdown.*

val shutdown = gracefulShutdown {
    timeout(30.seconds)
    onShutdown("http", priority = 10) { server.stop() }
    onShutdown("db", priority = 5) { dataSource.close() }
    onError { name, err -> logger.error("$name failed: $err") }
}
shutdown.installShutdownHook()

val tracker = RequestTracker()
// In request handler:
tracker.track { handleRequest(req) }
```

## API

| Function / Class | Description |
|------------------|-------------|
| `gracefulShutdown { }` | Build a shutdown manager |
| `onShutdown(name, priority) { }` | Register a shutdown handler |
| `ShutdownManager.trigger()` | Trigger shutdown (idempotent) |
| `ShutdownManager.installShutdownHook()` | Install JVM shutdown hook |
| `ShutdownManager.isShuttingDown()` | Check if shutdown triggered |
| `RequestTracker` | Track in-flight work |
| `RequestTracker.track { }` | Track a block of work |
| `RequestTracker.awaitDrained(timeout)` | Wait for all work to complete |

## Development

```bash
./gradlew test
./gradlew build
```

## License

MIT
