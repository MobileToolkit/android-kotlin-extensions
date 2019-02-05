package org.mobiletoolkit.android.extensions.android.graphics.color

import android.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by Sebastian Owodzin on 03/02/2019.
 */
class ColorUnitTest {

    @Test
    fun extension_parseColorOrNull() {
        var parsedColor = parseColorOrNull("")
        assertNull(parsedColor)

        parsedColor = parseColorOrNull("x")
        assertEquals(Color.TRANSPARENT, parsedColor)

        parsedColor = parseColorOrNull("1")
        assertEquals(Color.TRANSPARENT, parsedColor)

        parsedColor = parseColorOrNull("#u")
        assertEquals(Color.TRANSPARENT, parsedColor)

        parsedColor = parseColorOrNull("#3")
        assertEquals(Color.TRANSPARENT, parsedColor)

        parsedColor = parseColorOrNull("#ff000000")
        assertEquals(Color.toArgb(0xff000000), parsedColor)

        parsedColor = parseColorOrNull("0000ff")
        assertEquals(Color.toArgb(0x0000ff), parsedColor)

        parsedColor = parseColorOrNull("002e00")
        assertEquals(Color.toArgb(0x002e00), parsedColor)

        parsedColor = parseColorOrNull("#000040")
        assertEquals(Color.toArgb(0x000040), parsedColor)

        parsedColor = parseColorOrNull("#980b0307")
        assertEquals(Color.toArgb(0x980b0307), parsedColor)
    }
}