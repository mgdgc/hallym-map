package xyz.ridsoft.hal.common

import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding

class EmptyFooter(val binding: RowEmptyFooterBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_empty_footer
    }
}