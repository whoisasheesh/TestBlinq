package com.example.testblinq.helper

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener

class SharedPreferenceHelper private constructor(context: Context) : SharedPreferences {
    private val prefs: SharedPreferences
    init {
      prefs  = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
    }
    override fun getAll(): Map<String, *> {
        return prefs.all
    }

    override fun getString(s: String, s2: String?): String? {
        return prefs.getString(s, s2)
    }

    override fun getStringSet(s: String, strings: Set<String>?): Set<String>? {
        return prefs.getStringSet(s, strings)
    }

    override fun getInt(s: String, i: Int): Int {
        return prefs.getInt(s, i)
    }

    override fun getLong(s: String, l: Long): Long {
        return prefs.getLong(s, l)
    }

    override fun getFloat(s: String, v: Float): Float {
        return prefs.getFloat(s, v)
    }

    override fun getBoolean(s: String, b: Boolean): Boolean {
        return prefs.getBoolean(s, b)
    }

    override fun contains(s: String): Boolean {
        return prefs.contains(s)
    }

    override fun edit(): SharedPreferences.Editor {
        return prefs.edit()
    }

    override fun registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener) {}
    override fun unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener) {}
    val isAlreadyInvited: Boolean
        get() = prefs.getBoolean(IS_ALREADY_INVITED, false)
    val email: String?
        get() = prefs.getString(EMAIL, "")

    companion object {
        const val APP_SHARED_PREFS = "blink_prefs"
        const val IS_ALREADY_INVITED = "is_already_invited"
        const val EMAIL = "email"
        private var helper: SharedPreferenceHelper? = null
        fun getInstance(context: Context): SharedPreferenceHelper {
            return if (helper != null) {
                helper!!
            } else {
                helper = SharedPreferenceHelper(context)
                helper!!
            }
        }
    }

}