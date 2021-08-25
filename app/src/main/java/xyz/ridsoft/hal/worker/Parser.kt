package xyz.ridsoft.hal.worker

import android.util.Log
import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place
import xyz.ridsoft.hal.value.DefaultURLs
import java.lang.Exception

open class Parser<T> {

    open fun parse(urlString: String): Document? {
        return try {
            Jsoup.connect(urlString).get()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    open fun parseData(doc: Document): ArrayList<T> {
        return arrayListOf()
    }
}

open class MapDataParser : Parser<MapPoint>() {

    companion object {
        const val URL_MAP_DATA = DefaultURLs.SPREADSHEET_DATA_PLACE
    }

    override fun parseData(doc: Document): ArrayList<MapPoint> {
        val data = ArrayList<MapPoint>()

        val tr = doc.select("table.waffle tbody tr")

        for (i in tr.indices) {
            // Skip header
            if (i == 0) continue

            val td = tr[i].select("td")

            try {
                val type = td[0].text()
                val json = td[1].text()

                when (type) {
                    "Place" -> {
                        data.add(Gson().fromJson(json, Place::class.java))
                    }
                    "Facility" -> {
                        data.add(Gson().fromJson(json, Facility::class.java))
                    }
                    else -> {
                        data.add(Gson().fromJson(json, MapPoint::class.java))
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return data
    }
}
