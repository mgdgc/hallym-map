package xyz.ridsoft.hal.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.common.EmptyFooter
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding
import xyz.ridsoft.hal.databinding.RowFavoriteBinding
import xyz.ridsoft.hal.model.Facility

class FavoriteAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<Facility> = ArrayList()

    var onItemClickListener: ((view: View, position: Int) -> Unit)? = null
    var onDeleteButtonClickListener: ((id: Int, position: Int) -> Unit)? = null
    var onItemEmptyListener: (() -> Unit)? = null
    var onItemHasItemListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == FavoriteViewHolder.VIEW_TYPE) {
            FavoriteViewHolder(RowFavoriteBinding.inflate(inflater, parent, false))
        } else {
            EmptyFooter(RowEmptyFooterBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FavoriteViewHolder) {
            holder.bind(context, data[position])

            holder.onItemClickListener = { v ->
                onItemClickListener?.let {
                    it(v, position)
                }
            }

            holder.onDeleteButtonClickListener = {
                onDeleteButtonClickListener?.let {
                    it(data[position].id, position)
                }
            }
        }
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)

        if (data.size == 0) {
            onItemEmptyListener?.let { it() }
        }
    }

    fun clearData() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
        onItemEmptyListener?.let { it() }
    }

    fun addItem(item: Facility, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
        onItemHasItemListener?.let { it() }
    }

    fun addItem(item: Facility) {
        addItem(item, data.size)
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position < data.size) FavoriteViewHolder.VIEW_TYPE
        else EmptyFooter.VIEW_TYPE
    }
}