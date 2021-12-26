package xyz.ridsoft.hal.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.ActivitySearchBinding
import xyz.ridsoft.hal.model.Facility

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INT_RESULT_ID = "id"
        const val STRING_QUERY = "query"
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter

    private var recentKeyword = ArrayList<String>()

    private lateinit var onSearchResultListener: ((ArrayList<SearchResult>) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DataManager(this)

        getIntentData()
        initDefaultData()

        initView()
        initSearchView()
        initRecent()
    }

    private fun getIntentData() {
        val query = intent.getStringExtra(STRING_QUERY)
        query?.let {
            binding.searchView.setQuery(query, true)
            recentKeyword.add(it)
            val q = it.split(" ")
            search(q)
        }
    }

    private fun initDefaultData() {
        val mapData = DataManager(this)
        DataManager.places = mapData.getDefaultPlaceData() ?: arrayOf()
        DataManager.facilities = mapData.getDefaultFacilityData() ?: arrayOf()

        onSearchResultListener = { result ->
            if (result.size > 0) {
                adapter.data = result
            } else {
                Snackbar.make(binding.srlSearch, R.string.search_not_found, Snackbar.LENGTH_LONG)
                    .setAction(R.string.confirm) { }
                    .show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.fabSearch.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(30, 70))
            }

            val intent = Intent()
            intent.putExtra("result", "ok")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.srlSearch.setOnRefreshListener {
            binding.srlSearch.isRefreshing = false
            binding.searchView.requestFocus()

            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        adapter = SearchAdapter(this)
        binding.rvSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val inputManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
            }
        })

        adapter.onItemClickListener = { _, position ->
            val intent = Intent().apply {
                this.putExtra(INT_RESULT_ID, adapter.data[position].id)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.rvSearch.adapter = adapter

        runOnUiThread {
            Handler(mainLooper).postDelayed({
                binding.fabSearch.show()
            }, 50)
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    recentKeyword.add(it)
                    val q = it.split(" ")
                    search(q)
                    return true
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }

    private fun initRecent() {

    }

    private fun search(queries: List<String>) {
        CoroutineScope(Dispatchers.Main).async {
            val result = ArrayList<SearchResult>()

            // Search the lecture room number
            queries.forEach { q ->
                // Check if the query is lecture room number.
                val lectureRoomData = decodeLectureRoomNumber(q)

                // If so, add it to the result list.
                if (lectureRoomData != null) {
                    result.add(lectureRoomData)

                    // Also add related building info.
                    lectureRoomData.data?.get("building")?.let {
                        if (DataManager.placesById.contains(it.toInt())) {
                            val relative = SearchResult(
                                it.toInt(),
                                SearchResult.Companion.Reason.LECTURE_ROOM_BUILDING
                            )
                            result.add(relative)
                        }
                    }
                    return@forEach
                }
            }

            // Search the places
            DataManager.places!!.forEach { p ->
                var name = true
                var tag = false

                queries.forEach { q ->
                    if (!p.name.contains(q, true)
                        && !(p.legacyName != null && p.legacyName!!.equals(q, true))) {
                        name = false
                    }
                    val tags = p.searchTag?.lowercase()?.split(",")
                    if (tags?.contains(q.lowercase()) == true) tag = true
                }

                if (name) result.add(SearchResult(p.id, SearchResult.Companion.Reason.NAME))
                else if (tag) result.add(SearchResult(p.id, SearchResult.Companion.Reason.TAG))
            }

            // Search the facilities
            DataManager.facilities!!.forEach { p ->
                var name = true
                var tag = false
                var type = false

                queries.forEach { q ->
                    // Search name
                    if (!p.name.contains(q, true)) name = false

                    // Search type
                    if (Facility.Companion.FacilityType.getString(this@SearchActivity, p.type) == q) type = true

                    // Search tag
                    val tags = p.searchTag?.lowercase()?.split(",")
                    if (tags?.contains(q.lowercase()) == true) tag = true
                }

                if (name) result.add(SearchResult(p.id, SearchResult.Companion.Reason.NAME))
                else if (type) result.add(SearchResult(p.id, SearchResult.Companion.Reason.TYPE))
                else if (tag) result.add(SearchResult(p.id, SearchResult.Companion.Reason.TAG))
            }

            onSearchResultListener(result)

        }
    }

    private fun decodeLectureRoomNumber(q: String): SearchResult? {
        val regex = Regex("^[A-Za-z]?[0-9]{4,5}[A-Za-z]?(-[0-5]{1,2})?\$")
        var lectureRoom = q

        if (lectureRoom.matches(regex)) {
            val data = mutableMapOf<String, String>()

            if (lectureRoom.matches(Regex("^[A-Za-z][0-9]{4,5}[A-Za-z]?(-[0-5]{1,2})?\$"))) {
                data["pre"] = lectureRoom.slice(0 until 1)
                lectureRoom = lectureRoom.removeRange(0 until 1)
            }

            if (lectureRoom.contains(Regex("-[0-5]{1,2}"))) {
                val split = lectureRoom.split("-")
                data["unit"] = "-${split[1]}"
                lectureRoom = lectureRoom.replace(data["unit"]!!, "")
            }

            data["building"] = lectureRoom.slice(0 until lectureRoom.length - 3)
            lectureRoom = lectureRoom.removeRange(0 until lectureRoom.length - 3)

            data["floor"] = lectureRoom.slice(0 until 1)
            lectureRoom = lectureRoom.removeRange(0 until 1)

            data["room"] = lectureRoom

            val result =
                SearchResult(data["building"]!!.toInt(), SearchResult.Companion.Reason.LECTURE_ROOM)
            result.data = data

            return result

        } else {
            return null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
    }

}

data class SearchResult(var id: Int, var reason: Reason) {
    companion object {
        enum class Reason {
            NAME, TAG, LECTURE_ROOM, LECTURE_ROOM_BUILDING, TYPE;

            fun toString(context: Context): String {
                return context.getString(
                    when (this) {
                        NAME -> R.string.search_reason_name
                        TAG -> R.string.search_reason_tag
                        LECTURE_ROOM -> R.string.search_reason_lecture_room
                        TYPE -> R.string.search_reason_type
                        LECTURE_ROOM_BUILDING -> R.string.search_reason_lecture_room_building
                    }
                )
            }
        }
    }

    var data: Map<String, String>? = null
}