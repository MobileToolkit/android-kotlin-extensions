package org.mobiletoolkit.android.extensions.android.content.sharedpreferences

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Sebastian Owodzin on 03/02/2019.
 */
@RunWith(AndroidJUnit4::class)
class SharedPreferencesUnitTest {

    private enum class PrefsKey {
        Boolean, Float, Int, Long, String, StringSet, Custom
    }

    private data class Custom(
        val booleanParam: Boolean,
        val floatParam: Float,
        val intParam: Int,
        val longParam: Long,
        val stringParam: String,
        val stringSetParam: Set<String>
    )

    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getTargetContext()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Test
    fun extension_edit() {
        sharedPreferences.edit {
            it.putBoolean(PrefsKey.Boolean.name, true)
            it.putFloat(PrefsKey.Float.name, 3.1415927f)
            it.putInt(PrefsKey.Int.name, -123)
            it.putLong(PrefsKey.Long.name, 789L)
            it.putString(PrefsKey.String.name, "qwerty")
            it.putStringSet(PrefsKey.StringSet.name, setOf("qaz", "wsx", "edc"))
        }

        assertEquals(true, sharedPreferences.getBoolean(PrefsKey.Boolean.name, false))
        assertEquals(3.1415927f, sharedPreferences.getFloat(PrefsKey.Float.name, 0f))
        assertEquals(-123, sharedPreferences.getInt(PrefsKey.Int.name, 0))
        assertEquals(789L, sharedPreferences.getLong(PrefsKey.Long.name, 0L))
        assertEquals("qwerty", sharedPreferences.getString(PrefsKey.String.name, ""))
        assertEquals(setOf("qaz", "wsx", "edc"), sharedPreferences.getStringSet(PrefsKey.StringSet.name, setOf()))
    }

    @Test
    fun extension_get_set() {
        sharedPreferences[PrefsKey.Boolean.name] = true
        sharedPreferences[PrefsKey.Float.name] = 33.1415927f
        sharedPreferences[PrefsKey.Int.name] = -4123
        sharedPreferences[PrefsKey.Long.name] = 1789L
        sharedPreferences[PrefsKey.String.name] = "asdfgh"
        sharedPreferences[PrefsKey.StringSet.name] = setOf("rfv", "tgb", "yhn")

        assertEquals(true, sharedPreferences[PrefsKey.Boolean.name, false])
        assertEquals(33.1415927f, sharedPreferences[PrefsKey.Float.name, 0f])
        assertEquals(-4123, sharedPreferences[PrefsKey.Int.name, 0])
        assertEquals(1789L, sharedPreferences[PrefsKey.Long.name, 0L])
        assertEquals("asdfgh", sharedPreferences[PrefsKey.String.name, ""])
        assertEquals(setOf("rfv", "tgb", "yhn"), sharedPreferences[PrefsKey.StringSet.name, setOf<String>()])

        val expected = Custom(true, 123.1415927f, 49123, 6789L, "zxcvbn", setOf("ujm", "ik,", "ol."))

        sharedPreferences[PrefsKey.Custom.name] = expected

        val actual = sharedPreferences.get<Custom?>(PrefsKey.Custom.name, null)

        assertEquals(expected.booleanParam, actual?.booleanParam)
        assertEquals(expected.floatParam, actual?.floatParam)
        assertEquals(expected.intParam, actual?.intParam)
        assertEquals(expected.longParam, actual?.longParam)
        assertEquals(expected.stringParam, actual?.stringParam)
        assertEquals(expected.stringSetParam, actual?.stringSetParam)

        val expectedList = listOf(
            Custom(true, 3.1415927f, -123, 789L, "qwerty", setOf("qaz", "wsx", "edc")),
            Custom(true, 33.1415927f, -4123, 1789L, "asdfgh", setOf("rfv", "tgb", "yhn")),
            Custom(true, 123.1415927f, 49123, 6789L, "zxcvbn", setOf("ujm", "ik,", "ol."))
        )

        sharedPreferences[PrefsKey.Custom.name] = expectedList

        val actualList = sharedPreferences[PrefsKey.Custom.name, listOf<Custom>()]

        actualList.forEachIndexed { index, actualItem ->
            val expectedItem = expectedList[index]

            assertEquals(expectedItem.booleanParam, actualItem.booleanParam)
            assertEquals(expectedItem.floatParam, actualItem.floatParam)
            assertEquals(expectedItem.intParam, actualItem.intParam)
            assertEquals(expectedItem.longParam, actualItem.longParam)
            assertEquals(expectedItem.stringParam, actualItem.stringParam)
            assertEquals(expectedItem.stringSetParam, actualItem.stringSetParam)
        }
    }

//    @Test
//    fun extension_isSet() {
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.Boolean.name))
//        sharedPreferences[PrefsKey.Boolean.name] = true
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.Boolean.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.Float.name))
//        sharedPreferences[PrefsKey.Float.name] = 33.1415927f
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.Float.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.Int.name))
//        sharedPreferences[PrefsKey.Int.name] = -4123
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.Int.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.Long.name))
//        sharedPreferences[PrefsKey.Long.name] = 1789L
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.Long.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.String.name))
//        sharedPreferences[PrefsKey.String.name] = "asdfgh"
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.String.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.StringSet.name))
//        sharedPreferences[PrefsKey.StringSet.name] = setOf("rfv", "tgb", "yhn")
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.StringSet.name))
//
//        assertFalse(sharedPreferences.isSet<Boolean>(PrefsKey.Custom.name))
//        sharedPreferences[PrefsKey.Custom.name] = Custom(true, 123.1415927f, 49123, 6789L, "zxcvbn", setOf("ujm", "ik,", "ol."))
//        assertTrue(sharedPreferences.isSet<Boolean>(PrefsKey.Custom.name))
//    }

    @After
    fun after() {
        with(sharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}