package xyz.ridsoft.hal.data

import android.content.Context
import com.google.gson.Gson
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.Place
import xyz.ridsoft.hal.value.SharedPreferencesKeys
import kotlin.reflect.typeOf

class DataManager(val context: Context) {

    companion object {
        const val DEFAULT_FACILITY_FILE_NAME = "default_facility_data.json"
        const val DEFAULT_PLACE_FILE_NAME = "default_place_data.json"

        var facilities: Array<Facility>? = null
        var places: Array<Place>? = null
        val facilitiesById: Map<Int, Facility>
            get() {
                val map = mutableMapOf<Int, Facility>()
                for (f in facilities!!) {
                    map[f.id] = f
                }
                return map
            }

        val placesById: Map<Int, Place>
            get() {
                val map = mutableMapOf<Int, Place>()
                for (p in places!!) {
                    map[p.id] = p
                }
                return map
            }

        fun getFacility(type: Facility.Companion.FacilityType): ArrayList<Facility> {
            val data = ArrayList<Facility>()
            facilities!!.forEach { f ->
                if (f.type == type) data.add(f)
            }
            return data
        }

        private var favoriteData: MutableMap<Int, Boolean>? = null
    }

    init {
        if (facilities == null) {
            facilities = getDefaultFacilityData()
        }

        if (places == null) {
            places = getDefaultPlaceData()
        }
    }

    private fun readDefaultFile(filename: String): String? {
        return try {
            val iStream = context.assets.open(filename)
            val buffer = ByteArray(iStream.available())
            iStream.read(buffer)
            iStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getDefaultPlaceData(): Array<Place>? {
        val json = readDefaultFile(DEFAULT_PLACE_FILE_NAME)
        return Gson().fromJson(json, Array<Place>::class.java)
    }

    fun getDefaultFacilityData(): Array<Facility>? {
        val json = readDefaultFile(DEFAULT_FACILITY_FILE_NAME)
        return Gson().fromJson(json, Array<Facility>::class.java)
    }

    fun getFavoriteData(): MutableMap<Int, Boolean> {
        if (favoriteData == null) {
            val pref = context.getSharedPreferences(SharedPreferencesKeys.USER_PREF, 0)
            favoriteData = mutableMapOf<Int, Boolean>()

            for (f in facilities!!) {
                favoriteData!![f.id] = pref.getBoolean(f.id.toString(), false)
            }
        }
        return favoriteData as MutableMap<Int, Boolean>
    }

    fun getFavoriteData(id: Int): Boolean {
        val data = getFavoriteData()
        return data[id] ?: false
    }

    fun setFavoriteData(id: Int, favorite: Boolean) {
        val pref = context.getSharedPreferences(SharedPreferencesKeys.USER_PREF, 0)
        val edit = pref.edit()

        edit.putBoolean(id.toString(), favorite)

        edit.apply()

        if (favoriteData?.get(id) != null) {
            favoriteData!![id] = favorite
        }
    }

}