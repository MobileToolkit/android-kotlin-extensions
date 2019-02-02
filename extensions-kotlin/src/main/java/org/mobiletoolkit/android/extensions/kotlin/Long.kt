package org.mobiletoolkit.android.extensions.kotlin

/**
 * Created by Sebastian Owodzin on 11/12/2018.
 */

/**
 * Builds a list of LongRange objects
 */
fun Long.toRanges(size: Long): List<LongRange> = (0 until div(size).plus(if (rem(size) > 0) 1 else 0)).map {
    val start = it * size
    val end = (it * size) + size - 1

    LongRange(start, if (end > this) this else end)
}