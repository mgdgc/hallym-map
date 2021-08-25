package xyz.ridsoft.hal.data

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place
import xyz.ridsoft.hal.value.SharedPreferencesKeys
import xyz.ridsoft.hal.worker.MapDataParser
import java.util.*
import kotlin.collections.ArrayList
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

    }

    init {
        if (facilities == null) {
            facilities = getDefaultFacilityData()
        }

        if (places == null) {
            places = getDefaultPlaceData()
        }
    }

    fun isUpdateNecessary(): Boolean {
        val pref = context.getSharedPreferences(SharedPreferencesKeys.APP_PREF, 0)
        val lastUpdate = pref.getInt(SharedPreferencesKeys.INT_LAST_UPDATE, 0)

        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_YEAR)

        return today - lastUpdate > 2 || today - lastUpdate < 0
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

    fun getDefaultPlaceData(): Array<Place> {
        val placeArr = ArrayList<Place>()

        val json = readDefaultFile(DEFAULT_PLACE_FILE_NAME)
        placeArr.addAll(Gson().fromJson(json, Array<Place>::class.java))

        val mapPref = context.getSharedPreferences(SharedPreferencesKeys.MAP_PREF, 0)

        val placeKeysJson = mapPref.getString(SharedPreferencesKeys.STRING_UPDATED_PLACES, null)
        if (placeKeysJson != null) {
            val placeKeys = Gson().fromJson(placeKeysJson, Array<Int>::class.java)

            for (key in placeKeys) {
                mapPref.getString(key.toString(), null)?.let {
                    placeArr.add(Gson().fromJson(it, Place::class.java))
                }
            }
        }

        return placeArr.toTypedArray()
    }

    fun getDefaultFacilityData(): Array<Facility> {
        val facilityArr = ArrayList<Facility>()
        val json = readDefaultFile(DEFAULT_FACILITY_FILE_NAME)

        facilityArr.addAll(Gson().fromJson(json, Array<Facility>::class.java))

        val mapPref = context.getSharedPreferences(SharedPreferencesKeys.MAP_PREF, 0)

        val keysJson = mapPref.getString(SharedPreferencesKeys.STRING_UPDATED_FACILITIES, null)
        if (keysJson != null) {
            val placeKeys = Gson().fromJson(keysJson, Array<Int>::class.java)

            for (key in placeKeys) {
                mapPref.getString(key.toString(), null)?.let {
                    facilityArr.add(Gson().fromJson(it, Facility::class.java))
                }
            }
        }

        return facilityArr.toTypedArray()
    }

    fun getFavoriteData(): MutableMap<Int, Boolean> {
        val favoriteData: MutableMap<Int, Boolean> = mutableMapOf()
        val pref = context.getSharedPreferences(SharedPreferencesKeys.FAVORITE_PREF, 0)

        for (f in facilities!!) {
            favoriteData[f.id] = pref.getBoolean(f.id.toString(), false)
        }

        return favoriteData
    }

    fun getFavoriteData(id: Int): Boolean {
        val data = getFavoriteData()
        return data[id] ?: false
    }

    fun setFavoriteData(id: Int, favorite: Boolean) {
        val pref = context.getSharedPreferences(SharedPreferencesKeys.FAVORITE_PREF, 0)
        val edit = pref.edit()

        edit.putBoolean(id.toString(), favorite)

        edit.apply()
    }

    // UpdateData listeners
    var onUpdateStartListener: (() -> Unit)? = null
    var onUpdateFinishListener: (() -> Unit)? = null
    fun updateData() {
        // Notify update started
        onUpdateStartListener?.let { it() }

        CoroutineScope(Dispatchers.IO).launch {
            // Parse data from web
            val parser = MapDataParser()
            val document = parser.parse(MapDataParser.URL_MAP_DATA) ?: return@launch
            val result = parser.parseData(document)

            // If no data returned, finish update process
            if (result.size == 0) return@launch

            // Separate data (Place::class or Facility::class)
            val places = mutableMapOf<Int, Place>()
            val facilities = mutableMapOf<Int, Facility>()
            for (point in result) {
                if (point is Place) places[point.id] = point
                else if (point is Facility) facilities[point.id] = point
            }

            // Save keys for later data initialization
            val mapPref = context.getSharedPreferences(SharedPreferencesKeys.MAP_PREF, 0)
            val mapPrefEdit = mapPref.edit()
            mapPrefEdit.putString(SharedPreferencesKeys.STRING_UPDATED_PLACES, Gson().toJson(places.keys.toTypedArray()))
            mapPrefEdit.putString(SharedPreferencesKeys.STRING_UPDATED_FACILITIES, Gson().toJson(facilities.keys.toTypedArray()))

            // Save Place data
            for (place in places.values) {
                mapPrefEdit.putString(place.id.toString(), Gson().toJson(place))
            }

            // Save Facility data
            for (facility in facilities.values) {
                mapPrefEdit.putString(facility.id.toString(), Gson().toJson(facility))
            }

            mapPrefEdit.apply()

            // Reinitialize global data
            DataManager.places = getDefaultPlaceData()
            DataManager.facilities = getDefaultFacilityData()

            // Save updated date
            val pref = context.getSharedPreferences(SharedPreferencesKeys.APP_PREF, 0)
            val prefEdit = pref.edit()

            val cal = Calendar.getInstance()
            val today = cal.get(Calendar.DAY_OF_YEAR)

            prefEdit.putInt(SharedPreferencesKeys.INT_LAST_UPDATE, today)
            prefEdit.apply()

            // Notify update finished
            onUpdateFinishListener?.let { it() }

        }
    }

}