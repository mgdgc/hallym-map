package xyz.ridsoft.hal.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.OverlayItem
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DefaultMapData
import xyz.ridsoft.hal.data.GeoCoordinate
import xyz.ridsoft.hal.databinding.FragmentMapBinding
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place
import java.util.*
import kotlin.collections.ArrayList

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    private lateinit var places: Array<Place>
    private lateinit var facilities: Array<Facility>

    private var overlays: MutableMap<Facility.Companion.FacilityType, Overlay> = mutableMapOf()

    public var onClickListener: ((View) -> Unit) = {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(15, 70))
        }

        (activity as MainActivity).performCircularRevealAnimation()
        val intent = Intent(requireActivity(), SearchActivity::class.java)
//            intent.putExtra(SearchActivity.EXTRA_FAB_X, it.x + (it.width / 2))
//            intent.putExtra(SearchActivity.EXTRA_FAB_Y, it.y + (it.height / 2))
//            intent.putExtra(SearchActivity.EXTRA_FAB_Y, it.marginBottom)
        (activity as MainActivity).activityResultLauncher.launch(intent)

        activity?.overridePendingTransition(0, R.anim.anim_fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapBinding.bind(view)

        initDefaultData()
        initMapView()
        initChipView()

        binding.buttonMapCurrent.setOnClickListener {
            // TODO
        }
    }

    private fun initDefaultData() {
        val mapData = DefaultMapData(requireContext())
        places = mapData.getDefaultPlaceData() ?: arrayOf()
        facilities = mapData.getDefaultFacilityData() ?: arrayOf()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initMapView() {
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)

        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.isClickable = true
        binding.mapView.controller.setZoom(18.0)
        val startPoint =
            GeoPoint(GeoCoordinate.UNIVERSITY.latitude, GeoCoordinate.UNIVERSITY.longitude)
        binding.mapView.controller.setCenter(startPoint)

        val box = BoundingBox(37.8898, 127.7431, 37.8827, 127.7333)
        binding.mapView.setScrollableAreaLimitDouble(box)

        val receiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                binding.mapView.overlays.clear()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return true
            }
        }
        binding.mapView.overlays.add(MapEventsOverlay(receiver))
    }

    @SuppressLint("ResourceType")
    private fun initChipView() {
        val types = Facility.Companion.FacilityType.getArray()

        for (type in types) {
            val chip = Chip(requireContext())

            chip.text = Facility.Companion.FacilityType.getString(requireContext(), type)
            chip.isCheckable = true
            chip.isCloseIconVisible = false
            chip.chipBackgroundColor =
                requireContext().resources.getColorStateList(R.drawable.background_tag, null)

            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                Facility.Companion.FacilityType.getIconId(type)
            )
            val layerDrawable = LayerDrawable(arrayOf(drawable))
            layerDrawable.setLayerInset(0, 4, 4, 4, 4)
            chip.chipIcon = layerDrawable
            chip.iconStartPadding = 16f

            val facList = ArrayList<Facility>()
            for (f in facilities) {
                if (f.type == type) {
                    facList.add(f)
                }
            }
            overlays[type] = convertToOverlay(facList.toTypedArray())

            chip.setOnClickListener {
                val c = it as Chip
                overlays[type]?.let { overlays ->
                    if (c.isChecked) {
                        addOverlayPin(overlays)
                    } else {
                        removeOverlayPin(overlays)
                    }
                }

            }

            binding.chipMapTag.addView(chip)
        }



    }

    private fun addOverlayPin(overlay: Overlay) {
        binding.mapView.overlays.add(overlay)
    }

    private fun removeOverlayPin(overlay: Overlay) {
        binding.mapView.overlays.remove(overlay)
    }

    private fun convertToOverlay(mapPoints: Array<MapPoint>): Overlay {
        val overlayList = ArrayList<OverlayItem>()

        val locales = requireContext().resources.configuration.locales
        var locale = "en"
        for (j in 0 until locales.size()) {
            if (arrayOf("ko", "en").contains(locales[j].toString())) {
                locale = locales[j].toString()
            }
        }

        for (i in mapPoints.indices) {
            val geoPoint = GeoPoint(mapPoints[i].latitude, mapPoints[i].longitude)

            val name = when (locale) {
                "ko" -> mapPoints[i].kr
                "en" -> mapPoints[i].en
                else -> mapPoints[i].name
            }

            val overlayItem = OverlayItem(name, "snippet", geoPoint)
            val markerDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_marker)
            overlayItem.setMarker(markerDrawable)
            overlayList.add(overlayItem)
        }

        return ItemizedIconOverlay(
            overlayList,
            object : OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                    binding.mapView.controller.setZoom(19.5)
                    val startPoint =
                        GeoPoint(
                            item?.point?.latitude ?: GeoCoordinate.UNIVERSITY.latitude,
                            item?.point?.longitude ?: GeoCoordinate.UNIVERSITY.longitude
                        )
                    binding.mapView.controller.setCenter(startPoint)
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                    return true
                }

            },
            requireActivity().applicationContext
        )
    }

    override fun onDetach() {
        super.onDetach()
    }
}