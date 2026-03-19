package com.philiprehberger.gracefulshutdown

import kotlin.test.*

class RequestTrackerTest {
    @Test fun `track increments and decrements`() {
        val t = RequestTracker()
        assertEquals(0, t.activeCount)
        t.track { assertEquals(1, t.activeCount) }
        assertEquals(0, t.activeCount)
    }
}
