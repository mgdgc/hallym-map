package xyz.ridsoft.hal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if (mapPoint is Facility) {
            handleFacilityData(mapPoint as Facility)
        } else if (mapPoint is Place) {
            handlePlaceData(mapPoint as Place)
        }
    }

    private fun handleFacilityData(facility: Facility) {
        binding.imgMapBottomSheetIcon.setImageResource(
            Facility.Companion.FacilityType.getIconId(
                facility.type
            )
        )
        binding.txtMapBottomSheetTitle.text = facility.getLocalizedString(requireContext())
    }

    private fun handlePlaceData(place: Place) {

    }

}