package org.mobiletoolkit.android.extensions.android.view.view

import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by Sebastian Owodzin on 23/05/2018.
 */

/**
 * Will execute the given block() ONLY ONCE onGlobalLayout
 *
 * @see ViewTreeObserver.OnGlobalLayoutListener.onGlobalLayout
 */
inline fun View.afterGlobalLayout(crossinline block: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            block()
        }
    })
}

/**
 * Will execute the given block() ONLY ONCE onGlobalLayout & when the View is measured
 *
 * @see ViewTreeObserver.OnGlobalLayoutListener.onGlobalLayout
 */
inline fun <T : View> T.afterMeasured(crossinline block: T.() -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}