package com.example.nancost.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.example.nancost.NancostApplication

object SharedPreUtils {
    private const val SHARED_PREFERENCES = "preferences"

    private fun getPrefs(): SharedPreferences {
        return getPrefs(NancostApplication.instance.baseContext)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun getString(context: Context, key: String?): String {
        return getPrefs(context).getString(key, "")!!
    }

    fun getString(key: String): String? {
        return getPrefs().getString(key, "")
    }

    fun putString(key: String, value: String?) {
        getPrefs().edit().putString(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return getPrefs().getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        getPrefs().edit().putLong(key, value).apply()
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return getPrefs().getFloat(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        getPrefs().edit().putFloat(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return getPrefs().getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        getPrefs().edit().putInt(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getPrefs().getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        getPrefs().edit().putBoolean(key, value).apply()
    }

    fun remove(key: String) {
        getPrefs().edit().remove(key).apply()
    }

    @Synchronized
    fun getListString(key: String): ArrayList<String> {
        val myList = TextUtils.split(getPrefs().getString(key, ""), "‚‗‚")
        val newList = ArrayList<String>()
        for (aMyList in myList) {
            newList.add(aMyList)
        }
        return newList
    }

    @Synchronized
    fun putListString(key: String, list: ArrayList<String>) {
        val myIntList = list.toTypedArray()
        getPrefs().edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
    }

    fun clearPrefs() {
        getPrefs().edit().clear().apply()
    }
}
