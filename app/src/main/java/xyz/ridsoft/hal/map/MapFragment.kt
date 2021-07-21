package xyz.ridsoft.hal.map

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.util.SizeF
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.chip.Chip
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.GeoCoordinate
import xyz.ridsoft.hal.data.MapPoint
import xyz.ridsoft.hal.databinding.FragmentMapBinding
import xyz.ridsoft.hal.model.Place
import java.util.*

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).registerFavClickListener {
            // TODO: on fab click
        }
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

        initMapView()
        initChipView()

        binding.buttonMapCurrent.setOnClickListener {
            // TODO
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initMapView() {
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)

        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.isClickable = true
        binding.mapView.controller.setZoom(17.5)
        val startPoint =
            GeoPoint(GeoCoordinate.UNIVERSITY.latitude, GeoCoordinate.UNIVERSITY.longitude)
        binding.mapView.controller.setCenter(startPoint)
    }

    private fun initPlaces() {
        val places = MapPoint().getPlaces()


    }

    @SuppressLint("ResourceType")
    private fun initChipView() {
        val facilities = Place.Companion.FacilityType.getArray()

        for (f in facilities) {
            val chip = Chip(requireContext())

            chip.text = Place.Companion.FacilityType.getString(requireContext(), f)
            chip.isCheckable = true
            chip.isCloseIconVisible = false
            chip.chipBackgroundColor =
                requireContext().resources.getColorStateList(R.drawable.background_tag, null)

            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                Place.Companion.FacilityType.getIconId(f)
            )
            val layerDrawable = LayerDrawable(arrayOf(drawable))
            layerDrawable.setLayerInset(0, 4, 4, 4, 4)
            chip.chipIcon = layerDrawable
            chip.iconStartPadding = 16f

            chip.setOnClickListener { view ->

            }

            binding.chipMapTag.addView(chip)
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}