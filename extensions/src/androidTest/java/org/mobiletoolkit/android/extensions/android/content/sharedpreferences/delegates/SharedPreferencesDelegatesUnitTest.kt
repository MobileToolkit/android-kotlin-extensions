package org.mobiletoolkit.android.extensions.android.content.sharedpreferences.delegates

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mobiletoolkit.android.extensions.android.content.sharedpreferences.edit

/**
 * Created by Sebastian Owodzin on 05/02/2019.
 */
@RunWith(AndroidJUnit4::class)
class SharedPreferencesDelegatesUnitTest {

    private lateinit var sharedPreferences: SharedPreferences

    class TestClass(
        override val sharedPreferences: SharedPreferences
    ) : SharedPreferencesProvider {
        var booleanProperty by SharedPreferencesDelegates.boolean(defaultValue = false)
        var floatProperty by SharedPreferencesDelegates.float(key = "Float", defaultValue = 0f)
        var intProperty by SharedPreferencesDelegates.int(defaultValue = 0)
        var longProperty by SharedPreferencesDelegates.long("Long", 0L)
        var stringProperty by SharedPreferencesDelegates.string(defaultValue = "")
        var stringSetProperty by SharedPreferencesDelegates.stringSet(defaultValue = setOf())
    }

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getTargetContext()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Test
    fun delegates_get() {
        sharedPreferences.edit {
            it.putBoolean("booleanProperty", true)
            it.putFloat("Float", 3.1415927f)
            it.putInt("intProperty", -123)
            it.putLong("Long", 789L)
            it.putString("stringProperty", "qwerty")
            it.putStringSet("stringSetProperty", setOf("qaz", "wsx", "edc"))
        }

        val actual = TestClass(sharedPreferences)

        assertEquals(true, actual.booleanProperty)
        assertEquals(3.1415927f, actual.floatProperty)
        assertEquals(-123, actual.intProperty)
        assertEquals(789L, actual.longProperty)
        assertEquals("qwerty", actual.stringProperty)
        assertEquals(setOf("qaz", "wsx", "edc"), actual.stringSetProperty)
    }

    @Test
    fun delegates_set() {
        val test = TestClass(sharedPreferences)

        test.booleanProperty = true
        test.floatProperty = 13.1415927f
        test.intProperty = -1123
        test.longProperty = 1789L
        test.stringProperty = "1qwerty"
        test.stringSetProperty = setOf("1qaz", "wsx", "edc")

        assertEquals(true, sharedPreferences.getBoolean("booleanProperty", false))
        assertEquals(13.1415927f, sharedPreferences.getFloat("Float", 0f))
        assertEquals(-1123, sharedPreferences.getInt("intProperty", 0))
        assertEquals(1789L, sharedPreferences.getLong("Long", 0L))
        assertEquals("1qwerty", sharedPreferences.getString("stringProperty", ""))
        assertEquals(setOf("1qaz", "wsx", "edc"), sharedPreferences.getStringSet("stringSetProperty", setOf()))
    }

    @After
    fun after() {
        with(sharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}