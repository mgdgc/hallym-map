package xyz.ridsoft.hal.model

class Place(val id: Int, val name: String, val latitude: Double, val longitude: Double) {

    companion object {
        public const val DEFAULT_LANG = "default"

        public const val DEFAULT_BUILDING_NUMBER = 0

        enum class FacilityType {
            CAFE, STUDY_ROOM, STORE_CONVENIENCE, STORE_BOOK, STORE_FANCY
        }

        class Facility(val id: Int, val type: FacilityType, val floor: Int) {

        }
    }

    var nameKr: String? = null
    var nameEn: String? = null
    var legacyName: String? = null
    var buildingNum: Int = DEFAULT_BUILDING_NUMBER
    var facility: Array<Facility> = arrayOf()


}