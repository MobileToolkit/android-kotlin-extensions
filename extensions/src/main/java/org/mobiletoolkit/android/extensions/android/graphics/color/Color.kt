package org.mobiletoolkit.android.extensions.android.graphics.color

import android.graphics.Color
import android.support.annotation.ColorInt

/**
 * Created by Sebastian Owodzin on 23/05/2018.
 */

/**
 * Parses the color string and return the corresponding color-int value.
 *
 * @see Color.parseColor
 */
@ColorInt
fun parseColorOrNull(colorString: String?): Int? {
    if (colorString?.isNotBlank() == true) {
        try {
            return Color.parseColor(colorString)
        } catch (exception: IllegalArgumentException) { }
    }

    return null
}
