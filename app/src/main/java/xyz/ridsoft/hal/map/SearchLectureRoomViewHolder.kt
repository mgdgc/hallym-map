package xyz.ridsoft.hal.map

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.RowSearchLectureRoomBinding

class SearchLectureRoomViewHolder(val binding: RowSearchLectureRoomBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_search_lecture_room
    }

    fun bind(context: Context, data: SearchResult) {
        Log.e("result", "bind()")
        var building: String? = null
        var floor: String? = null
        var room: String? = null
        var pre: String? = null
        var unit: String? = null

        data.data?.let {
            building = it["building"] as String?
            floor = it["floor"] as String?
            room = it["room"] as String?
            pre = it["pre"] as String?
            unit = it["unit"] as String?
        }

        if (building != null) {
            binding.txtSearchLectureRoomBuilding.text = building
        } else {
            binding.layoutSearchLectureRoomBuilding.visibility = View.GONE
        }

        if (floor != null) {
            binding.txtSearchLectureRoomFloor.text = floor
        } else {
            binding.layoutSearchLectureRoomFloor.visibility = View.GONE
        }

        if (room != null) {
            binding.txtSearchLectureRoomNumber.text = room
        } else {
            binding.layoutSearchLectureRoomNumber.visibility = View.GONE
        }

        if (pre != null) {
            binding.txtSearchLectureRoomPre.text = pre
        } else {
            binding.layoutSearchLectureRoomPre.visibility = View.GONE
        }

        if (unit != null) {
            binding.txtSearchLectureRoomUnit.text = unit
        } else {
            binding.layoutSearchLectureRoomUnit.visibility = View.GONE
        }
    }
}