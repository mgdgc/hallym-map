package xyz.ridsoft.hal.facilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.common.EmptyFooter
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding
import xyz.ridsoft.hal.databinding.RowFacilityBinding
import xyz.ridsoft.hal.model.Facility

class FacilityAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: ((view: View, position: Int) -> Unit)? = null

    var data: ArrayList<Facility> = ArrayList()
        set(value) {
            val prevSize = data.size
            data.clear()
            notifyItemRangeRemoved(0, prevSize)

            data.addAll(value)
            notifyItemRangeInserted(0, value.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FacilityViewHolder.VIEW_TYPE) {
            FacilityViewHolder(RowFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            EmptyFooter(RowEmptyFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FacilityViewHolder) {
            holder.bind(context, data[position])

            holder.onItemClickListener = { v ->
                onItemClickListener?.let {
                    it(v, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position < data.size) FacilityViewHolder.VIEW_TYPE
        else EmptyFooter.VIEW_TYPE
    }
}