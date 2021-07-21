package xyz.ridsoft.hal.model

import android.content.Context
import com.google.gson.Gson
import xyz.ridsoft.hal.R

data class Place(val id: Int, val name: String, val latitude: Double, val longitude: Double) {

    companion object {
        public const val DEFAULT_LANG = "default"

        public const val DEFAULT_BUILDING_NUMBER = 0

        enum class FacilityType {
            CAFE, STUDY_ROOM, STORE_CONVENIENCE, STORE_BOOK, STORE_STATIONERY,
            ATM, CAFETERIA, PRINTER, POST, VENDING_MACHINE, ETC;

            companion object {
                fun getArray(): Array<FacilityType> {

                    return arrayOf(
                        CAFE, STUDY_ROOM, STORE_CONVENIENCE, STORE_BOOK, STORE_STATIONERY,
                        ATM, CAFETERIA, PRINTER, POST, VENDING_MACHINE, ETC
                    )
                }

                fun getString(context: Context, facilityType: FacilityType): String {
                    return when (facilityType) {
                        CAFE -> context.resources.getString(R.string.facility_cafe)
                        STUDY_ROOM -> context.resources.getString(R.string.facility_study_room)
                        STORE_CONVENIENCE -> context.resources.getString(R.string.facility_convenience_store)
                        STORE_BOOK -> context.resources.getString(R.string.facility_book_store)
                        STORE_STATIONERY -> context.resources.getString(R.string.facility_stationery_store)
                        ATM -> context.resources.getString(R.string.facility_atm)
                        CAFETERIA -> context.resources.getString(R.string.facility_cafeteria)
                        PRINTER -> context.resources.getString(R.string.facility_printer)
                        POST -> context.resources.getString(R.string.facility_post)
                        VENDING_MACHINE -> context.resources.getString(R.string.facility_vending_machine)
                        ETC -> context.resources.getString(R.string.facility_etc)
                    }
                }

                fun getIconId(facilityType: FacilityType): Int {
                    return when (facilityType) {
                        CAFE -> R.drawable.baseline_local_cafe_24
                        STUDY_ROOM -> R.drawable.baseline_groups_24
                        STORE_CONVENIENCE -> R.drawable.baseline_store_24
                        STORE_BOOK -> R.drawable.baseline_auto_stories_24
                        STORE_STATIONERY -> R.drawable.baseline_architecture_24
                        ATM -> R.drawable.baseline_atm_24
                        CAFETERIA -> R.drawable.baseline_dining_24
                        PRINTER -> R.drawable.baseline_print_24
                        POST -> R.drawable.baseline_local_post_office_24
                        VENDING_MACHINE -> R.drawable.baseline_local_drink_24
                        ETC -> R.drawable.baseline_more_24
                    }
                }
            }
        }

        data class Facility(val id: Int, val type: FacilityType, val floor: Int) {
            var name: String? = null
            var kr: String? = null
            var en: String? = null
            var searchTag: String? = null

            fun toJson(): String {
                return Gson().toJson(this)
            }
        }
    }

    var kr: String? = null
    var en: String? = null
    var legacyName: String? = null
    var buildingNo: Int = DEFAULT_BUILDING_NUMBER
    var facilityId: Array<Int> = arrayOf()
    var searchTag: String? = null

    fun toJson(): String {
        return Gson().toJson(this)
    }

}