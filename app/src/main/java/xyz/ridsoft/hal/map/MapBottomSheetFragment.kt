package xyz.ridsoft.hal.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.etc.HapticFeedback
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.FragmentMainBottomSheetBinding
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place
import java.lang.StringBuilder

class MapBottomSheetFragment : Fragment() {

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

        binding.buttonBottomSheetClose.setOnClickListener {
            (requireActivity() as MainActivity).hideBottomSheet()
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

        val builder = StringBuilder()

        builder.append(requireContext().getString(R.string.bottom_sheet_type) + " ")
            .append(Facility.Companion.FacilityType.getString(requireContext(), facility.type))
            .append("\n\n")

        val related = DataManager.placesById[facility.buildingNo]
        if (related != null) {
            builder.append(requireContext().getString(R.string.bottom_sheet_locate))
                .append(
                    requireContext().getString(R.string.bottom_sheet_located_in)
                        .replace("<building>", related.getLocalizedString(requireContext()))
                )
                .append("\n\n")
        }

        facility.searchTag?.let {
            val tags = it.split(",")
            for (i in 0 until 5) {
                if (i < tags.size) {
                    builder.append("#").append(tags[i]).append(" ")
                }
            }
        }


        binding.txtBottomSheetContent.text = builder.toString()
    }

    private fun handlePlaceData(place: Place) {
        binding.imgMapBottomSheetIcon.setImageResource(R.drawable.baseline_business_24)
        binding.layoutMapBottomSheetFavorite.visibility = View.GONE
    }

}