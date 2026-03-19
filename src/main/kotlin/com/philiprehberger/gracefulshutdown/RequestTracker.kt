package com.philiprehberger.gracefulshutdown

import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** Tracks in-flight requests for graceful draining. */
public class RequestTracker {
    private val count = AtomicInteger(0)

    /** Number of currently active requests. */
    public val activeCount: Int get() = count.get()

    /** Track a block of work. Increments count on entry, decrements on exit. */
    public fun <T> track(block: () -> T): T {
        count.incrementAndGet()
        try { return block() } finally { count.decrementAndGet() }
    }

    /** Suspend until all tracked work completes or [timeout] expires. */
    public suspend fun awaitDrained(timeout: Duration) {
        val deadline = System.currentTimeMillis() + timeout.inWholeMilliseconds
        while (count.get() > 0 && System.currentTimeMillis() < deadline) {
            delay(50.milliseconds)
        }
    }
}
