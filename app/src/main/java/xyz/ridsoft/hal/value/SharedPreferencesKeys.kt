package xyz.ridsoft.hal.value

import android.content.Context

class SharedPreferencesKeys {
    companion object {
        const val APP_PREF = "app"
        const val INT_LAST_UPDATE = "last_update"

        const val MAP_PREF = "map"
        const val STRING_UPDATED_PLACES = "updated_places"
        const val STRING_UPDATED_FACILITIES = "updated_facilities"

        const val DEVELOPER_PREF = "developer"
        const val BOOL_DEV_MENU_ENABLE = "dev_menu"
        const val STRING_FACILITY_JSON = "facilities"
        const val STRING_PLACE_JSON = "places"

        const val FAVORITE_PREF = "favorites"
    }
}

class SharedPreferencesManager(val context: Context) {

    fun removeAll() {
        val appPref = context.getSharedPreferences(SharedPreferencesKeys.APP_PREF, 0).edit()
        appPref.clear()
        appPref.apply()

        removeFavorites()
    }

    fun removeFavorites() {
        val favPref = context.getSharedPreferences(SharedPreferencesKeys.FAVORITE_PREF, 0).edit()
        favPref.clear()
        favPref.apply()
    }

}