package org.mobiletoolkit.android.extensions.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

class IntUnitTest {

    @Test
    fun toRanges() {
        assertEquals(
            listOf(
                0..19,
                20..39,
                40..43
            ), 44.toRanges(20)
        )

        assertEquals(
            listOf(
                0..7,
                8..15,
                16..20
            ), 21.toRanges(8)
        )

        assertEquals(
            listOf(
                0..0
            ), 1.toRanges(100)
        )
    }
}