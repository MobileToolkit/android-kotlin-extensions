package org.mobiletoolkit.android.extensions

/**
 * Created by Sebastian Owodzin on 11/12/2018.
 */
fun Int.toIntRanges(size: Int): List<IntRange> {
    val steps = div(size).plus(if (rem(size) > 0) 1 else 0)

    return (0 until steps).map {
        val start = it * size
        val end = (it * size) + size - 1

        IntRange(start, if (end > this) this else end)
    }
}