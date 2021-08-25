package xyz.ridsoft.hal.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.api.HapticFeedback
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.FragmentFavoriteBinding
import xyz.ridsoft.hal.model.Facility

class FavoriteFragment : Fragment() {

    companion object {
        public const val TAG = "my_place"
    }

    public var onClickListener: ((View) -> Unit) = {

    }

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter(requireContext())

        binding.rvMyPlace.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMyPlace.adapter = adapter

        adapter.onItemClickListener = { _, position ->
            // TODO
            (activity as MainActivity).selectPage(0)
        }

        adapter.onDeleteButtonClickListener = { id, position ->
            HapticFeedback(requireContext()).touchFeedback()

            DataManager(requireContext()).setFavoriteData(id, false)
            adapter.removeItem(position)
        }

        adapter.onItemEmptyListener = {
            binding.layoutFavoriteNoItem.visibility = View.VISIBLE
        }

        adapter.onItemHasItemListener = {
            binding.layoutFavoriteNoItem.visibility = View.GONE
        }
    }

    private fun initData() {
        adapter.clearData()

        val dataManager = DataManager(requireContext())

        for (f in DataManager.facilities!!) {
            if (dataManager.getFavoriteData(f.id)) {
                adapter.addItem(f)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

}