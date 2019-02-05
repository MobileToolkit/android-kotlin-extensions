package org.mobiletoolkit.android.extensions.android.content.sharedpreferences.delegates

import org.mobiletoolkit.android.extensions.android.content.sharedpreferences.delegates.internal.*
import kotlin.properties.ReadWriteProperty

/**
 * Created by Sebastian Owodzin on 04/02/2019.
 */
object SharedPreferencesDelegates {

    fun boolean(
        key: String? = null,
        defaultValue: Boolean
    ): ReadWriteProperty<SharedPreferencesProvider, Boolean> = BooleanProperty(key, defaultValue)

    fun float(
        key: String? = null,
        defaultValue: Float
    ): ReadWriteProperty<SharedPreferencesProvider, Float> = FloatProperty(key, defaultValue)

    fun int(
        key: String? = null,
        defaultValue: Int
    ): ReadWriteProperty<SharedPreferencesProvider, Int> = IntProperty(key, defaultValue)

    fun long(
        key: String? = null,
        defaultValue: Long
    ): ReadWriteProperty<SharedPreferencesProvider, Long> = LongProperty(key, defaultValue)

    fun string(
        key: String? = null,
        defaultValue: String
    ): ReadWriteProperty<SharedPreferencesProvider, String> = StringProperty(key, defaultValue)

    fun stringSet(
        key: String? = null,
        defaultValue: Set<String>
    ): ReadWriteProperty<SharedPreferencesProvider, Set<String>> = StringSetProperty(key, defaultValue)
}