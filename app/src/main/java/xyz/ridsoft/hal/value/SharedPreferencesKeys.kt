package xyz.ridsoft.hal.value

import android.content.Context

class SharedPreferencesKeys {
    companion object {
        public const val USER_PREF = "user"
        public const val STRING_USER_NAME = "name"

        public const val DEVELOPER_PREF = "developer"
        public const val BOOL_DEV_MENU_ENABLE = "dev_menu"
        public const val STRING_FACILITY_JSON = "facilities"

        public const val FAVORITE_PREF = "favorites"
    }
}

class SharedPreferencesManager(val context: Context) {

    fun removeAll() {
        val userPref = context.getSharedPreferences(SharedPreferencesKeys.USER_PREF, 0).edit()
        userPref.remove(SharedPreferencesKeys.STRING_USER_NAME)
        removeFavorites()
        userPref.apply()
    }

    fun removeFavorites() {
        val favPref = context.getSharedPreferences(SharedPreferencesKeys.FAVORITE_PREF, 0).edit()
        favPref.apply()
    }

}