package xyz.ridsoft.hal.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.FragmentMoreBinding
import xyz.ridsoft.hal.developer.FacilityJsonBuilderActivity
import xyz.ridsoft.hal.developer.JsonBuilderActivity
import xyz.ridsoft.hal.value.SharedPreferencesManager
import xyz.ridsoft.hal.value.URL

class MoreFragment : Fragment() {

    companion object {
        public const val TAG = "more"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).registerFavClickListener {
            // TODO: on fab click
        }
    }

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
        binding.rvMore.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.onItemClickListener = { id ->
            handleOnItemClickListener(id)
        }

        binding.rvMore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (activity as MainActivity).hideBottomNavigationView(dy > 0)
                (activity as MainActivity).hideFloatingActionButton(dy > 0)
            }
        })

        adapter.initDefaultData()
    }

    private fun handleOnItemClickListener(id: String) {
        when (id) {
            "app_info" -> {
                startActivity(Intent(context, AppInfoActivity::class.java))
            }

            "dev_menu" -> {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle("개발자 메뉴")
                    .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Place Json Builder") { dialog, _ ->
                        this.startActivity(
                            Intent(requireContext(), JsonBuilderActivity::class.java)
                        )
                        dialog.dismiss()
                    }
                    .setNegativeButton("Facility Json Builder") { dialog, _ ->
                        this.startActivity(
                            Intent(
                                requireContext(),
                                FacilityJsonBuilderActivity::class.java
                            )
                        )
                        dialog.dismiss()
                    }
                    .show()
            }

            "privacy" -> {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle(R.string.more_privacy_policy)
                    .setMessage(R.string.more_privacy_policy_message)
                    .setPositiveButton(R.string.open) { dialog, _ ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL.PRIVACY_POLICY)))
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .show()
            }

            "delete_fav" -> {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle(R.string.more_delete_favorite)
                    .setMessage(R.string.more_delete_favorite_message)
                    .setPositiveButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton(R.string.proceed) { dialog, _ ->
                        SharedPreferencesManager(requireContext()).removeAll()
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .show()
                SharedPreferencesManager(requireContext()).removeFavorites()
            }

            "delete_all" -> {
                val alert = AlertDialog.Builder(requireContext())
                alert.setTitle(R.string.more_delete_all)
                    .setMessage(R.string.more_delete_all_message)
                    .setPositiveButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton(R.string.proceed) { dialog, _ ->
                        SharedPreferencesManager(requireContext()).removeAll()
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .show()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        this.adapter.initDefaultData()
    }
}