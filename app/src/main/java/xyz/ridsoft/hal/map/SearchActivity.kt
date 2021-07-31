package xyz.ridsoft.hal.map

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DefaultMapData
import xyz.ridsoft.hal.databinding.ActivitySearchBinding
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place
import kotlin.math.hypot

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var places: Array<Place>
    private lateinit var facilities: Array<Facility>

    private var recentKeyword = ArrayList<String>()

    private lateinit var onSearchResultListener: ((Array<MapPoint>) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        initDefaultData()

        initView()
        initSearchView()
        initRecent()
    }

    private fun getIntentData() {

    }

    private fun initDefaultData() {
        val mapData = DefaultMapData(this)
        places = mapData.getDefaultPlaceData() ?: arrayOf()
        facilities = mapData.getDefaultFacilityData() ?: arrayOf()

        onSearchResultListener = { r ->
            val result = ArrayList<String>()
            r.forEach {
                result.add("[${it.id}] ${it.name} (${it.latitude}|${it.longitude})")
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle("검색 결과")
                .setAdapter(
                    ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        result.toTypedArray()
                    )
                ) { d, _ ->
                    d.dismiss()
                }
                .setNegativeButton("닫기") { d, _ ->
                    d.dismiss()
                }
                .setCancelable(true)
                .show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.fabSearch.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(15, 70))
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

        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val inputManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
            }
        })

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

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO
                return true
            }

        })
    }

    private fun initRecent() {

    }

    private fun search(query: List<String>) {
        CoroutineScope(Dispatchers.Main).async {
            val result = ArrayList<MapPoint>()

            query.forEach { q ->
                places.forEach { p ->
                    if (p.name.contains(q) || p.legacyName?.contains(q) == true || p.searchTag?.contains(q) == true) {
                        result.add(p)
                    }
                }
            }

            query.forEach { q ->
                facilities.forEach { p ->
                    if (p.name.contains(q) || p.searchTag?.contains(q) == true
                        || Facility.Companion.FacilityType.getString(this@SearchActivity, p.type).contains(q)) {
                        result.add(p)
                    }
                }
            }

            onSearchResultListener(result.distinct().toTypedArray())

        }
    }

}