package xyz.ridsoft.hal.favorite

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.RowFavoriteBinding
import xyz.ridsoft.hal.model.Facility

class FavoriteViewHolder(val binding: RowFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_favorite
    }

    var onItemClickListener: ((view: View) -> Unit)? = null
    var onDeleteButtonClickListener: (() -> Unit)? = null

    fun bind(context: Context, data: Facility) {
        binding.txtRowFavoriteTitle.text = data.getLocalizedString(context)

        val related = DataManager.placesById[data.buildingNo]

        if (related != null) {
            binding.txtRowFavoriteDescription.text =
                context.getString(R.string.search_facility_related_description)
                    .replace("<name>", related.getLocalizedString(context))
                    .replace("<floor>", data.floor.toString())
        } else {
            binding.txtRowFavoriteDescription.text = context.getString(R.string.search_facility_default_description)
        }

        binding.imgRowFavoriteIcon.setImageResource(
            Facility.Companion.FacilityType.getIconId(data.type)
        )

        binding.layoutRowFavorite.setOnClickListener { v ->
            onItemClickListener?.let {
                it(v)
            }
        }

        binding.layoutRowFavoriteButton.setOnClickListener {
            onDeleteButtonClickListener?.let {
                it()
            }
        }
    }
}