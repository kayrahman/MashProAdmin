package com.nkr.mashpro.util

import android.content.Context
import android.content.SharedPreferences
import com.nkr.mashpro.R


class SharedPrefsHelper(context: Context) {
    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)

    private val TOKEN_KEY = "token"

    fun putToken(token: String) {
        mSharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }
/*

    fun getToken(): String {
      //  return mSharedPreferences.getString(TOKEN_KEY,"")
    }
*/

    fun delToken() {
        mSharedPreferences.edit().putString(TOKEN_KEY, "").apply()
    }

    fun put(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun put(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    fun put(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    operator fun get(key: String, defaultValue: String): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Int): Int? {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Float): Float? {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean? {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun deleteSavedData(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    companion object {

        var PREF_KEY_ACCESS_TOKEN = "access-token"
    }
}