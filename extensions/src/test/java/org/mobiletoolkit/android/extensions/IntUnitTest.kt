package org.mobiletoolkit.android.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class IntUnitTest {

    @Test
    fun toIntRanges() {
        assertEquals(
            listOf(
                0..19,
                20..39,
                40..44
            ), 44.toIntRanges(20)
        )

        assertEquals(
            listOf(
                0..7,
                8..15,
                16..21
            ), 21.toIntRanges(8)
        )
    }
}