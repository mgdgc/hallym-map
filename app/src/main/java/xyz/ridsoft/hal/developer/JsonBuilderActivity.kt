package xyz.ridsoft.hal.developer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.ridsoft.hal.databinding.ActivityJsonBuilderBinding

class JsonBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJsonBuilderBinding

    private var clicked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editJsonBuilderId.requestFocus()
    }
}