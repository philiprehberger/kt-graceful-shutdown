package com.philiprehberger.gracefulshutdown

import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

class GracefulShutdownTest {
    @Test fun `ordered teardown`() = runTest {
        val order = mutableListOf<String>()
        val sm = gracefulShutdown {
            timeout(5.seconds)
            onShutdown("first", priority = 10) { order.add("first") }
            onShutdown("second", priority = 5) { order.add("second") }
            onShutdown("third", priority = 1) { order.add("third") }
        }
        sm.trigger()
        assertEquals(listOf("first", "second", "third"), order)
    }
    @Test fun `idempotent`() = runTest {
        var count = 0
        val sm = gracefulShutdown { onShutdown("x") { count++ } }
        sm.trigger(); sm.trigger()
        assertEquals(1, count)
    }
    @Test fun `isShuttingDown`() = runTest {
        val sm = gracefulShutdown { onShutdown("x") {} }
        assertFalse(sm.isShuttingDown())
        sm.trigger()
        assertTrue(sm.isShuttingDown())
    }
    @Test fun `error handling`() = runTest {
        var errorName = ""
        val sm = gracefulShutdown {
            onShutdown("fail") { throw RuntimeException("boom") }
            onShutdown("ok") {}
            onError { name, _ -> errorName = name }
        }
        sm.trigger()
        assertEquals("fail", errorName)
    }
}
