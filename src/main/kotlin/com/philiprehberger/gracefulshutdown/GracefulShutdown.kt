package com.philiprehberger.gracefulshutdown

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/** Build a shutdown manager. */
public fun gracefulShutdown(block: ShutdownBuilder.() -> Unit): ShutdownManager {
    val b = ShutdownBuilder()
    b.block()
    return ShutdownManager(b.timeout, b.handlers.sortedByDescending { it.priority }, b.errorHandler)
}

public class ShutdownBuilder {
    internal var timeout: Duration = 30.seconds
    internal val handlers = mutableListOf<HandlerDef>()
    internal var errorHandler: ((String, Throwable) -> Unit)? = null

    public fun timeout(duration: Duration) { timeout = duration }
    public fun onShutdown(name: String, priority: Int = 0, block: suspend () -> Unit) { handlers.add(HandlerDef(name, priority, block)) }
    public fun onError(block: (String, Throwable) -> Unit) { errorHandler = block }
}

internal data class HandlerDef(val name: String, val priority: Int, val block: suspend () -> Unit)

/** Manages graceful application shutdown. */
public class ShutdownManager internal constructor(
    private val timeout: Duration,
    private val handlers: List<HandlerDef>,
    private val errorHandler: ((String, Throwable) -> Unit)?,
) {
    private val triggered = AtomicBoolean(false)

    /** Returns true if shutdown has been triggered. */
    public fun isShuttingDown(): Boolean = triggered.get()

    /** Trigger shutdown. Idempotent — safe to call multiple times. */
    public suspend fun trigger() {
        if (!triggered.compareAndSet(false, true)) return
        withTimeoutOrNull(timeout) {
            for (handler in handlers) {
                try { handler.block() }
                catch (e: Throwable) { errorHandler?.invoke(handler.name, e) }
            }
        }
    }

    /** Install a JVM shutdown hook that triggers this manager. */
    public fun installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            runBlocking { trigger() }
        })
    }
}
