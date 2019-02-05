package org.mobiletoolkit.android.extensions.android.content.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.*
import com.google.gson.reflect.TypeToken

/**
 * Created by Sebastian Owodzin on 25/03/2018.
 */
inline fun SharedPreferences.edit(
    crossinline block: (SharedPreferences.Editor) -> Unit
) = with(edit()) {
    block(this)
    apply()
}

val SharedPreferences.gson: Gson by lazy {
    GsonBuilder().serializeNulls().create()
}

@Suppress("UNCHECKED_CAST")
inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T = when (defaultValue) {
    is Boolean -> getBoolean(key, defaultValue as? Boolean ?: false) as? T ?: defaultValue
    is Float -> getFloat(key, defaultValue as? Float ?: -1F) as? T ?: defaultValue
    is Int -> getInt(key, defaultValue as? Int ?: -1) as? T ?: defaultValue
    is Long -> getLong(key, defaultValue as? Long ?: -1L) as? T ?: defaultValue
    is String -> getString(key, defaultValue as? String) as? T ?: defaultValue
    else -> {
        val isStringSet = (defaultValue as? Set<String>) != null
        if (isStringSet) {
            getStringSet(key, defaultValue as? Set<String> ?: setOf()) as? T ?: defaultValue
        } else {
            try {
                if (defaultValue is Collection<*>) {
                    gson.fromJson(getString(key, ""), object : TypeToken<T>() {}.type) as? T ?: defaultValue
                } else {
                    gson.fromJson(getString(key, ""), T::class.java) ?: defaultValue
                }
            } catch (e: JsonParseException) {
                throw UnsupportedOperationException()
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
operator fun SharedPreferences.set(key: String, value: Any?) = edit {
    when (value) {
        is Boolean -> it.putBoolean(key, value)
        is Float -> it.putFloat(key, value)
        is Int -> it.putInt(key, value)
        is Long -> it.putLong(key, value)
        is String -> it.putString(key, value)
        else -> {
            val isStringSet = (value as? Set<String>) != null
            if (isStringSet) {
                it.putStringSet(key, value as Set<String>)
            } else {
                try {
                    it.putString(key, gson.toJson(value))
                } catch (e: JsonIOException) {
                    throw UnsupportedOperationException()
                }
            }
        }
    }
}

//inline fun <reified T> SharedPreferences.isSet(key: String): Boolean {
//    return get<T?>(key, null) != null
//}
