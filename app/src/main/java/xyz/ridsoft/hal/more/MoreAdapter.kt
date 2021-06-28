package xyz.ridsoft.hal.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.common.EmptyFooter
import xyz.ridsoft.hal.common.SectionHeader
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding
import xyz.ridsoft.hal.databinding.RowMoreMenuBinding
import xyz.ridsoft.hal.databinding.RowMoreUserBinding
import xyz.ridsoft.hal.databinding.RowSectionHeaderBinding
import xyz.ridsoft.hal.model.TableData
import xyz.ridsoft.hal.value.SharedPreferencesKeys

class MoreAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ROW = R.layout.row_more_menu
        private const val VIEW_TYPE_USER = R.layout.row_more_user
        private const val VIEW_TYPE_SECTION = R.layout.row_section_header
        private const val VIEW_TYPE_FOOTER = R.layout.row_empty_footer
    }

    private var data: ArrayList<TableData> = ArrayList()

    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> MoreUserViewHolder(
                RowMoreUserBinding.inflate(LayoutInflater.from(context), parent, false)
            )

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

            // Title
            viewHolder.binding.txtRowMoreMenuTitle.text = data[position].title

            // Content
            data[position].content?.let {
                viewHolder.binding.txtRowMoreMenuContent.text = it
            }

            // Icon
            data[position].icon?.let {
                viewHolder.binding.imgRowMoreMenu.setImageResource(it)
            }

            // Icon background
            data[position].iconBackgroundColor?.let {
                viewHolder.binding.layoutRowMoreMenuIcon.setBackgroundResource(it)
            }

            // Accessory
            if (data[position].accessory) {
                viewHolder.binding.imgRowMoreMenuAccessory.visibility = View.VISIBLE
            }

            // Click
            if (data[position].selectable) {
                viewHolder.binding.layoutRowMoreMenu.setOnClickListener {
                    if (onItemClickListener != null) {
                        onItemClickListener!!(data[position].id)
                    }
                }
            }

            // Background
            when (data[position].itemPosition) {
                TableData.Companion.ItemPosition.TOP -> viewHolder.binding.layoutRowMoreMenu.setBackgroundResource(R.drawable.background_table_item_top)
                TableData.Companion.ItemPosition.MIDDLE -> viewHolder.binding.layoutRowMoreMenu.setBackgroundResource(R.drawable.background_table_item_middle)
                TableData.Companion.ItemPosition.BOTTOM -> {
                    viewHolder.binding.layoutRowMoreMenu.setBackgroundResource(R.drawable.background_table_item_bottom)
                    viewHolder.binding.layoutRowMoreMenuDivider.visibility = View.INVISIBLE
                }
                TableData.Companion.ItemPosition.SINGLE -> viewHolder.binding.layoutRowMoreMenu.setBackgroundResource(R.drawable.background_table_item_single)
            }

        } else if (holder.itemViewType == VIEW_TYPE_SECTION) {
            val viewHolder = holder as SectionHeader

            viewHolder.binding.txtRowSectionTitle.text = data[position].title

        } else if (holder.itemViewType == VIEW_TYPE_USER) {
            val viewHolder = holder as MoreUserViewHolder

            // User icon
            data[position].icon?.let { viewHolder.binding.imgRowMoreUser.setImageResource(it) }

            // User name
            viewHolder.binding.txtRowMoreUserName.text = context.getSharedPreferences(
                SharedPreferencesKeys.USER_PREF,
                0
            ).getString(
                SharedPreferencesKeys.STRING_USER_NAME,
                context.resources.getString(R.string.more_user_default_name)
            )

            // User Description
            // TODO
            viewHolder.binding.txtRowMoreUserContent.text = "즐겨찾기한 항목 0"

            // Edit button
            viewHolder.binding.imgRowMoreUserEdit.setOnClickListener {
                //TODO
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position == 0) VIEW_TYPE_USER else if (position == data.size) VIEW_TYPE_FOOTER else {
            when (data[position].itemType) {
                TableData.Companion.ItemType.DIVIDER -> VIEW_TYPE_SECTION
                TableData.Companion.ItemType.NORMAL -> VIEW_TYPE_ROW
                else -> VIEW_TYPE_ROW
            }
        }
    }

    public fun setData(data: Array<TableData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    public fun addData(data: Array<TableData>) {
        this.data.addAll(data)
        notifyItemInserted(this.data.size - data.size)
    }

    public fun initDefaultData() {
        setData(MoreMenuDefaultData(context).getDefaultData())
    }

}