package com.hashim.instagram.data.local.prefs

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val prefs: SharedPreferences) {

    companion object {
        const val KEY_USER_ID = "PREF_KEY_USER_ID"
        const val KEY_USER_NAME = "PREF_KEY_USER_NAME"
        const val KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL"
        const val KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        const val KEY_THEME_MODE = "PREF_THEME_MODE"
        const val KEY_THEME_CHANGE = "PREF_THEME_CHANGE"
    }

    fun getThemeMode() : String? =
        prefs.getString(KEY_THEME_MODE,null)

    fun setThemeMode(mode : String) =
        prefs.edit().putString(KEY_THEME_MODE,mode).apply()

    fun isThemeChange() : Boolean =
        prefs.getBoolean(KEY_THEME_CHANGE,false)

    fun setThemeChange(isChange : Boolean) =
        prefs.edit().putBoolean(KEY_THEME_CHANGE,isChange).apply()


    fun getUserId(): String? =
        prefs.getString(KEY_USER_ID, null)

    fun setUserId(userId: String) =
        prefs.edit().putString(KEY_USER_ID, userId).apply()

    fun removeUserId() =
        prefs.edit().remove(KEY_USER_ID).apply()

    fun getUserName(): String? =
        prefs.getString(KEY_USER_NAME, null)

    fun setUserName(userName: String) =
        prefs.edit().putString(KEY_USER_NAME, userName).apply()

    fun removeUserName() =
        prefs.edit().remove(KEY_USER_NAME).apply()

    fun getUserEmail(): String? =
        prefs.getString(KEY_USER_EMAIL, null)

    fun setUserEmail(email: String) =
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()

    fun removeUserEmail() =
        prefs.edit().remove(KEY_USER_EMAIL).apply()

    fun getAccessToken(): String? =
        prefs.getString(KEY_ACCESS_TOKEN, null)

    fun setAccessToken(token: String) =
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()

    fun removeAccessToken() =
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
}