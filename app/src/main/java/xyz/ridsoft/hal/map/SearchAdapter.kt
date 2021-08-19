package xyz.ridsoft.hal.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.common.EmptyFooter
import xyz.ridsoft.hal.databinding.RowEmptyFooterBinding
import xyz.ridsoft.hal.databinding.RowSearchBinding
import xyz.ridsoft.hal.databinding.RowSearchLectureRoomBinding
import xyz.ridsoft.hal.model.MapPoint

class SearchAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: ((view: View, position: Int) -> Unit)? = null

    var data: ArrayList<SearchResult> = ArrayList()
        set(value) {
            val prevSize = data.size
            data.clear()
            notifyItemRangeRemoved(0, prevSize)

            data.addAll(value)
            notifyItemRangeInserted(0, data.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchViewHolder.VIEW_TYPE ->
                SearchViewHolder(
                    RowSearchBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )

            SearchLectureRoomViewHolder.VIEW_TYPE ->
                SearchLectureRoomViewHolder(
                    RowSearchLectureRoomBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                )

            else -> EmptyFooter(
                RowEmptyFooterBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == SearchViewHolder.VIEW_TYPE) {
            val viewHolder = holder as SearchViewHolder
            viewHolder.bind(context, data[position])

            viewHolder.onItemClickListener = { v ->
                this.onItemClickListener?.let {
                    it(v, position)
                }
            }

        } else if (holder.itemViewType == SearchLectureRoomViewHolder.VIEW_TYPE) {
            val viewHolder = holder as SearchLectureRoomViewHolder
            viewHolder.bind(context, data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position < data.size) {
            when (data[position].reason) {
                SearchResult.Companion.Reason.LECTURE_ROOM -> SearchLectureRoomViewHolder.VIEW_TYPE
                else -> SearchViewHolder.VIEW_TYPE
            }
        } else EmptyFooter.VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

}