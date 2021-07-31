package xyz.ridsoft.hal.more

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.RowMoreMenuBinding
import xyz.ridsoft.hal.model.TableData

class MoreMenuViewHolder(val binding: RowMoreMenuBinding): RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((String) -> Unit)? = null

    fun bind(data: TableData) {
        // Title
        binding.txtRowMoreMenuTitle.text = data.title

        // Content
        data.content?.let {
            binding.txtRowMoreMenuContent.text = it
        }

        // Icon
        data.icon?.let {
            binding.imgRowMoreMenu.setImageResource(it)
        }

        // Icon background
        data.iconBackgroundColor?.let {
            binding.layoutRowMoreMenuIcon.setBackgroundResource(it)
        }

        // Accessory
        if (data.accessory) {
            binding.imgRowMoreMenuAccessory.visibility = View.VISIBLE
        }

        // Background
        when (data.itemPosition) {
            TableData.Companion.ItemPosition.TOP -> binding.layoutRowMoreMenu.setBackgroundResource(
                R.drawable.background_table_item_top
            )
            TableData.Companion.ItemPosition.MIDDLE -> binding.layoutRowMoreMenu.setBackgroundResource(
                R.drawable.background_table_item_middle
            )
            TableData.Companion.ItemPosition.BOTTOM -> {
                binding.layoutRowMoreMenu.setBackgroundResource(R.drawable.background_table_item_bottom)
                binding.layoutRowMoreMenuDivider.visibility = View.INVISIBLE
            }
            TableData.Companion.ItemPosition.SINGLE -> binding.layoutRowMoreMenu.setBackgroundResource(
                R.drawable.background_table_item_single
            )
        }

        // Click
        if (data.selectable) {
            binding.layoutRowMoreMenu.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!(data.id)
                }
            }
        }
    }
}