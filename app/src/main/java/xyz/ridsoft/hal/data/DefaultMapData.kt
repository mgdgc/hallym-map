package xyz.ridsoft.hal.data

import android.content.Context
import xyz.ridsoft.hal.model.Place

class DefaultMapData(val context: Context) {

    fun getDefaultPlaceData(): Array<Place>? {
        return null
    }

    fun getDefaultFacilityData(): Array<Place.Companion.Facility>? {
        return null
    }
}