package xyz.ridsoft.hal.facilities

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.RowFacilityBinding
import xyz.ridsoft.hal.model.Facility

class FacilityViewHolder(val binding: RowFacilityBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_facility
    }
    var onItemClickListener: ((view: View) -> Unit)? = null

    fun bind(context: Context, data: Facility) {
        val related = DataManager.placesById[data.buildingNo]

        binding.txtRowFacilityTitle.text = data.getLocalizedString(context)

        if (related != null) {
            binding.txtRowFacilityDescription.text =
                context.getString(R.string.search_facility_related_description)
                .replace("<name>", related.getLocalizedString(context))
                .replace("<floor>", data.floor.toString())
        } else {
            binding.txtRowFacilityDescription.text = context.getString(R.string.search_facility_default_description)
        }

        binding.imgRowFacilityIcon.setImageResource(
            Facility.Companion.FacilityType.getIconId(data.type)
        )

        binding.layoutRowFacility.setOnClickListener { v ->
            onItemClickListener?.let {
                it(v)
            }
        }
    }
}