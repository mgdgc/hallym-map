package xyz.ridsoft.hal.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private lateinit var adapter: MoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoreBinding.bind(view)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = MoreAdapter(requireContext())

        binding.rvMore.adapter = adapter
        binding.rvMore.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.onItemClickListener = { id ->
            handleOnItemClickListener(id)
        }

        adapter.initDefaultData()
    }

    private fun handleOnItemClickListener(id: String) {
        when (id) {
            "app_info" -> {

            }


        }
    }
}