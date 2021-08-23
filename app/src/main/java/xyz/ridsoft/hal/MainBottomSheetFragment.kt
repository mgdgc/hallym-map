package xyz.ridsoft.hal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ridsoft.hal.api.HapticFeedback
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.FragmentMainBottomSheetBinding
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place

class MainBottomSheetFragment : Fragment() {

    private lateinit var binding: FragmentMainBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBottomSheetBinding.bind(view)

        binding.layoutMainBottomSheetHeader.setOnClickListener {
            (requireActivity() as MainActivity)
        }
    }

    fun setData(mapPoint: MapPoint) {
        handleCommonData(mapPoint)
        if (mapPoint is Facility) {
            handleFacilityData(mapPoint)
        } else if (mapPoint is Place) {
            handlePlaceData(mapPoint)
        }
    }

    private fun handleCommonData(mapPoint: MapPoint) {
        binding.txtMapBottomSheetTitle.text = mapPoint.getLocalizedString(requireContext())

    }

    private fun handleFacilityData(facility: Facility) {
        binding.imgMapBottomSheetIcon.setImageResource(
            Facility.Companion.FacilityType.getIconId(
                facility.type
            )
        )

        val dataManager = DataManager(requireContext())
        binding.imgMapBottomSheetFavorite.setImageResource(
            if (dataManager.getFavoriteData(facility.id)) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        binding.layoutMapBottomSheetFavorite.setOnClickListener {
            HapticFeedback(requireContext()).touchFeedback()

            val manager = DataManager(requireContext())
            binding.imgMapBottomSheetFavorite.setImageResource(
                if (!manager.getFavoriteData(facility.id)) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            manager.setFavoriteData(facility.id, !manager.getFavoriteData(facility.id))
        }
    }

    private fun handlePlaceData(place: Place) {
        binding.imgMapBottomSheetIcon.setImageResource(R.drawable.baseline_business_24)
        binding.layoutMapBottomSheetFavorite.visibility = View.GONE
    }

}