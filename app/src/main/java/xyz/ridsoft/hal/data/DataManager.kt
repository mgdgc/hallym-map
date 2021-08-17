package xyz.ridsoft.hal.data

import android.content.Context
import com.google.gson.Gson
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.Place
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
}