package xyz.ridsoft.hal.model

import com.google.gson.Gson

class Place(val id: Int, val name: String, val latitude: Double, val longitude: Double) {

    companion object {
        public const val DEFAULT_LANG = "default"

        public const val DEFAULT_BUILDING_NUMBER = 0

        enum class FacilityType {
            CAFE, STUDY_ROOM, STORE_CONVENIENCE, STORE_BOOK, STORE_STATIONERY;

            companion object {
                fun getArray(): Array<FacilityType> {

                    return arrayOf(
                        CAFE,
                        STUDY_ROOM,
                        STORE_CONVENIENCE,
                        STORE_BOOK,
                        STORE_STATIONERY
                    )
                }
            }
        }

        class Facility(val id: Int, val type: FacilityType, val floor: Int) {
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