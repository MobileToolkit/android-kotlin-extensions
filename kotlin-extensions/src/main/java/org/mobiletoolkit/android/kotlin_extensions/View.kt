package org.mobiletoolkit.android.kotlin_extensions

import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by Sebastian Owodzin on 23/05/2018.
 */

inline fun View.waitForLayout(crossinline block: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            removeOnGlobalLayoutListener(this)
            block()
        }
    })
}

inline fun <T : View> T.afterMeasured(crossinline block: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}
