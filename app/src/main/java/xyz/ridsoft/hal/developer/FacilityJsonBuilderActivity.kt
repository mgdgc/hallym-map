package xyz.ridsoft.hal.developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.ActivityFacilityJsonBuilderBinding

class FacilityJsonBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacilityJsonBuilderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}