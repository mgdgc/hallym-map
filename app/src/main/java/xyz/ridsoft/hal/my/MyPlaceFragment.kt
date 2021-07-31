package xyz.ridsoft.hal.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ridsoft.hal.MainActivity
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.FragmentMyPlaceBinding

class MyPlaceFragment : Fragment() {

    companion object {
        public const val TAG = "my_place"
    }

    public var onClickListener: ((View) -> Unit) = {

    }

    private lateinit var binding: FragmentMyPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPlaceBinding.bind(view)
    }
}