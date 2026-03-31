# graceful-shutdown

[![Tests](https://github.com/philiprehberger/kt-graceful-shutdown/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-graceful-shutdown/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/graceful-shutdown.svg)](https://central.sonatype.com/artifact/com.philiprehberger/graceful-shutdown)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-graceful-shutdown)](https://github.com/philiprehberger/kt-graceful-shutdown/commits/main)

Graceful application shutdown with signal handling and ordered teardown.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:graceful-shutdown:0.1.4")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>graceful-shutdown</artifactId>
    <version>0.1.4</version>
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

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-graceful-shutdown)

🐛 [Report issues](https://github.com/philiprehberger/kt-graceful-shutdown/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-graceful-shutdown/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
