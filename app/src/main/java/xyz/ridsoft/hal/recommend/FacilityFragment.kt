package xyz.ridsoft.hal.recommend

import android.annotation.SuppressLint
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.FragmentFacilityBinding
import xyz.ridsoft.hal.model.Facility

class FacilityFragment : Fragment() {

    public var onClickListener: ((View) -> Unit) = {
        // TODO
    }

    private lateinit var binding: FragmentFacilityBinding
    private lateinit var adapter: FacilityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFacilityBinding.bind(view)

        initView()
    }

    private fun initView() {
        initRecyclerView()
        initChipGroup()
    }

    private fun initRecyclerView() {
        adapter = FacilityAdapter(requireContext())

        binding.rvFacility.adapter = adapter
        binding.rvFacility.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.onItemClickListener = { _, position ->
            (activity as MainActivity).selectPage(0)
            // TODO
        }
    }

    @SuppressLint("ResourceType")
    private fun initChipGroup() {
        val types = Facility.Companion.FacilityType.getArray()

        types.forEach { type ->
            val chip = Chip(context)

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

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val data = DataManager.getFacility(type)
                    adapter.data = data
                }
            }

            binding.chipFacility.addView(chip)
        }

        binding.chipFacility.isSingleSelection = true
        binding.chipFacility.isSelectionRequired = true
        binding.chipFacility.isSingleLine = true
        (binding.chipFacility.getChildAt(0) as Chip).isChecked = true
    }
}