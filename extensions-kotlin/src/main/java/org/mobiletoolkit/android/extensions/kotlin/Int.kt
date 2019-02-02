package org.mobiletoolkit.android.extensions.kotlin

/**
 * Created by Sebastian Owodzin on 11/12/2018.
 */

/**
 * Builds a list of IntRange
 */
fun Int.toRanges(size: Int): List<IntRange> = (0 until div(size).plus(if (rem(size) > 0) 1 else 0)).map {
    val start = it * size
    val end = with((it * size) + size) {
        if (this > this@toRanges) this@toRanges else this
    } - 1

    IntRange(start, end)
}