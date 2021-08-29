package xyz.ridsoft.hal.map

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.RowSearchBinding
import xyz.ridsoft.hal.model.Facility
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.model.Place

class SearchViewHolder(val binding: RowSearchBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_search
    }

    var onItemClickListener: ((view: View) -> Unit)? = null

    fun bind(context: Context, data: SearchResult) {
        val mapPoint: MapPoint =
            when {
                DataManager.placesById[data.id] != null -> {
                    DataManager.placesById[data.id]!!
                }
                DataManager.facilitiesById[data.id] != null -> {
                    DataManager.facilitiesById[data.id]!!
                }
                else -> return
            }

        binding.txtSearchTitle.text = mapPoint.getLocalizedString(context)

        if (mapPoint is Place) {

            binding.imgSearchIcon.setImageResource(R.drawable.baseline_business_24)
            binding.txtSearchDescription.text = data.reason.toString(context)

        } else if (mapPoint is Facility) {
            val related = DataManager.placesById[mapPoint.buildingNo]

            related?.let {
                binding.txtSearchDescription.text = context.getString(R.string.search_facility_related_description)
                        .replace("<name>", it.getLocalizedString(context))
                        .replace("<floor>", mapPoint.floor.toString())
                binding.txtSearchDescription.append("\n\n")
            }
            binding.txtSearchDescription.append(data.reason.toString(context))

            binding.txtSearchContent.text =
                Facility.Companion.FacilityType.getString(context, mapPoint.type)
            binding.imgSearchIcon.setImageResource(
                Facility.Companion.FacilityType.getIconId(
                    mapPoint.type
                )
            )
        }

        binding.layoutRowSearch.setOnClickListener { v ->
            onItemClickListener?.let {
                it(v)
            }
        }
    }
}