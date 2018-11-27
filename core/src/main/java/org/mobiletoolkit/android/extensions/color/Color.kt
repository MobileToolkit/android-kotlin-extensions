package org.mobiletoolkit.android.extensions.color

import android.graphics.Color
import android.support.annotation.ColorInt

/**
 * Created by Sebastian Owodzin on 23/05/2018.
 */

@ColorInt
fun parseColorOrNull(colorString: String?): Int? {
    if (colorString?.isNotBlank() == true) {
        try {
            return Color.parseColor(colorString)
        } catch (exception: IllegalArgumentException) {
        }
    }

    return null
}
