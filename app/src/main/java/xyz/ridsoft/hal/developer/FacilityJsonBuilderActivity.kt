package xyz.ridsoft.hal.developer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.ActivityFacilityJsonBuilderBinding
import xyz.ridsoft.hal.model.Place

class FacilityJsonBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacilityJsonBuilderBinding

    private var facility: Place.Companion.Facility? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = arrayOf(
            getString(R.string.facility_cafe),
            getString(R.string.facility_study_room),
            getString(R.string.facility_convenience_store),
            getString(R.string.facility_book_store),
            getString(R.string.facility_stationery_store)
        )
        binding.spinnerFacilityCategory.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)

        binding.buttonFacilityPlace.setOnClickListener {
            startActivity(
                Intent(
                    this@FacilityJsonBuilderActivity,
                    JsonBuilderActivity::class.java
                )
            )
        }

        binding.buttonFacilityGenerate.setOnClickListener {
            generate()
        }
    }

    private fun generate() {
        try {
            val id: Int = binding.editFacilityId.text.toString().toInt()
            val name: String = binding.editFacilityName.text.toString()
            val floor: Int = binding.editFacilityFloor.text.toString().toInt()
            val kr: String = binding.editFacilityKr.text.toString()
            val en: String = binding.editFacilityEn.text.toString()
            val tag: String = binding.editFacilityTag.text.toString()
            val type: Place.Companion.FacilityType =
                Place.Companion.FacilityType.getArray()[binding.spinnerFacilityCategory.selectedItemPosition]

            facility = Place.Companion.Facility(id, type, floor)

            facility!!.name = if (name.isNotEmpty()) name else null
            facility!!.kr = if (kr.isNotEmpty()) name else null
            facility!!.en = if (en.isNotEmpty()) name else null
            facility!!.searchTag = if (tag.isNotEmpty()) name else null

        } catch (e: Exception) {
            e.printStackTrace()
            binding.txtFacility.text = e.localizedMessage
        }
    }
}