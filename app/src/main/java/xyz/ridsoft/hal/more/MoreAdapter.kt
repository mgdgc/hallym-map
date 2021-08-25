package xyz.ridsoft.hal.more

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.common.EmptyFooter
import xyz.ridsoft.hal.common.SectionHeader
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding
import xyz.ridsoft.hal.databinding.RowMoreMenuBinding
import xyz.ridsoft.hal.databinding.RowSectionHeaderBinding
import xyz.ridsoft.hal.model.TableData

class MoreAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ROW = R.layout.row_more_menu
        private const val VIEW_TYPE_SECTION = R.layout.row_section_header
        private const val VIEW_TYPE_FOOTER = R.layout.row_empty_footer
    }

    private var data: ArrayList<TableData> = ArrayList()

    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_ROW -> MoreMenuViewHolder(
                RowMoreMenuBinding.inflate(LayoutInflater.from(context), parent, false)
            )

            VIEW_TYPE_SECTION -> SectionHeader(
                RowSectionHeaderBinding.inflate(LayoutInflater.from(context), parent, false)
            )

            VIEW_TYPE_FOOTER -> EmptyFooter(
                RowEmptyFooterBinding.inflate(LayoutInflater.from(context), parent, false)
            )

            else -> MoreMenuViewHolder(
                RowMoreMenuBinding.inflate(LayoutInflater.from(context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ROW) {
            val viewHolder = holder as MoreMenuViewHolder

            viewHolder.bind(data[position])

            if (onItemClickListener != null) {
                viewHolder.onItemClickListener = this.onItemClickListener
            }

        } else if (holder.itemViewType == VIEW_TYPE_SECTION) {
            val viewHolder = holder as SectionHeader

            viewHolder.binding.txtRowSectionTitle.text = data[position].title

        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position == data.size) VIEW_TYPE_FOOTER else {
            when (data[position].itemType) {
                TableData.Companion.ItemType.DIVIDER -> VIEW_TYPE_SECTION
                TableData.Companion.ItemType.NORMAL -> VIEW_TYPE_ROW
                else -> VIEW_TYPE_ROW
            }
        }
    }

    fun setData(data: Array<TableData>) {
        val size = data.size
        this.data.clear()
        notifyItemRangeRemoved(0, size)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    public fun addData(data: Array<TableData>) {
        this.data.addAll(data)
        notifyItemInserted(this.data.size - data.size)
    }

    public fun initDefaultData() {
        setData(MoreMenuDefaultData(context).getDefaultData())
    }

}