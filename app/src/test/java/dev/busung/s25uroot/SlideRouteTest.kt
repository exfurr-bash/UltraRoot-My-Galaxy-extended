package dev.busung.s25uroot

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SlideRouteTest {
    @Test
    fun parsesKnownRoutes() {
        assertNull(SlideRoute.parse(null))
        assertNull(SlideRoute.parse(""))
        assertEquals(SlideRoute.Default, SlideRoute.parse("default"))
        assertEquals(SlideRoute.Auto, SlideRoute.parse("auto"))
        assertEquals(SlideRoute.Tracefs, SlideRoute.parse("TRACEFS"))
        assertEquals(SlideRoute.Legacy, SlideRoute.parse("legacy"))
        assertEquals(SlideRoute.Legacy, SlideRoute.parse("p0"))
        assertNull(SlideRoute.parse("wormhole"))
    }
}
