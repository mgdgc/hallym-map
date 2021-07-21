package xyz.ridsoft.hal.developer

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import xyz.ridsoft.hal.R
import xyz.ridsoft.hal.databinding.ActivityFacilityJsonBuilderBinding
import xyz.ridsoft.hal.model.Place
import xyz.ridsoft.hal.value.SharedPreferencesKeys
import java.lang.StringBuilder

class FacilityJsonBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacilityJsonBuilderBinding

    private var facility: Place.Companion.Facility? = null

    private var facArr = ArrayList<Place.Companion.Facility>()

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(SharedPreferencesKeys.DEVELOPER_PREF, 0)

        val items = arrayOf(
            getString(R.string.facility_cafe),
            getString(R.string.facility_study_room),
            getString(R.string.facility_convenience_store),
            getString(R.string.facility_book_store),
            getString(R.string.facility_stationery_store),
            getString(R.string.facility_atm),
            getString(R.string.facility_cafeteria),
            getString(R.string.facility_printer),
            getString(R.string.facility_post),
            getString(R.string.facility_vending_machine),
            getString(R.string.facility_etc)
        )
        binding.spinnerFacilityCategory.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)

        binding.editFacilityId.setText((System.currentTimeMillis() % 100000).toString())
        binding.editFacilityId.requestFocus()

        binding.buttonFacilityClear.setOnClickListener {
            binding.editFacilityId.text.clear()
            binding.editFacilityId.setText((System.currentTimeMillis() % 100000).toString())
            binding.editFacilityName.text.clear()
            binding.editFacilityFloor.text.clear()
            binding.editFacilityKr.text.clear()
            binding.editFacilityEn.text.clear()
            binding.editFacilityTag.text.clear()
        }

        binding.buttonFacilityPlace.setOnClickListener {
            startActivity(
                Intent(
                    this@FacilityJsonBuilderActivity,
                    JsonBuilderActivity::class.java
                )
            )
            this.finish()
        }

        binding.buttonFacilityGenerate.setOnClickListener {
            generate()
            show()
        }

        binding.buttonFacilityAdd.setOnClickListener {
            if (facility == null) {
                Snackbar.make(
                    binding.layoutFacilityJsonBuilder,
                    "Generate object first",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Confirm") { }
                    .show()
                return@setOnClickListener
            } else {
                facility?.let { facArr.add(it) }
                saveArr()
            }
        }

        binding.buttonFacilityFromJson.setOnClickListener {
            val edittext = EditText(this)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Recover from JSON")
                .setView(edittext)
                .setPositiveButton("복구") { dialog, _ ->
                    val arr = Gson().fromJson(edittext.text.toString(), Array<Place.Companion.Facility>::class.java)
                    this.facArr.addAll(arr)
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.buttonFacilityShow.setOnClickListener {
            val arr = Array(facArr.size) { i ->
                var string = ""
                string += "[" + facArr[i].id + "]  " + facArr[i].name + "  ( " + facArr[i].type + ", " + facArr[i].floor + "f )"

                string
            }

            val builder = AlertDialog.Builder(this)
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arr
            )
            builder.setAdapter(adapter) { dialog, i ->
                dialog.dismiss()
                val itemMenu = AlertDialog.Builder(this)
                itemMenu.setTitle("[" + facArr[i].id + "] " + facArr[i].name)
                    .setMessage(Gson().toJson(facArr[i]))
                    .setNeutralButton("제거") { d, _ ->
                        facArr.removeAt(i)
                        d.dismiss()
                    }
                    .setPositiveButton("수정") { d, _ ->
                        setContent(facArr[i])
                        d.dismiss()
                    }
                    .setNegativeButton("닫기") { d, _ ->
                        d.dismiss()
                    }
                    .show()
            }
            builder.setPositiveButton("저장") { d, _ ->
                saveArr()
                d.dismiss()
            }
            builder.setNeutralButton("닫기") { d, _ ->
                d.dismiss()
            }
            builder.setNegativeButton("to Json") { d, _ ->
                binding.txtFacility.text = Gson().toJson(facArr.toTypedArray())
                d.dismiss()
            }
            builder.setCancelable(true)
            builder.show()
        }

        initArr()
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
            facility!!.kr = if (kr.isNotEmpty()) kr else null
            facility!!.en = if (en.isNotEmpty()) en else null
            facility!!.searchTag = if (tag.isNotEmpty()) tag else null

        } catch (e: Exception) {
            e.printStackTrace()
            binding.txtFacility.text = e.localizedMessage
        }
    }

    private fun show() {
        if (facility != null) {
            val builder = StringBuilder()
            builder.append("--- Object info ---\n\n")
                .append("id: " + facility!!.id + "\n")
                .append("(name): " + facility!!.name + "\n")
                .append("(kr): " + facility!!.kr + "\n")
                .append("(en): " + facility!!.en + "\n")
                .append("floor: " + facility!!.floor + "\n")
                .append("type: " + facility!!.type + "\n")
                .append("searchTag: " + facility!!.searchTag + "\n\n\n")
                .append("--- Json ---\n\n")
                .append(facility!!.toJson())

            binding.txtFacility.text = builder.toString()
        }
    }

    private fun setContent(obj: Place.Companion.Facility) {
        binding.editFacilityId.setText(obj.id.toString())
        binding.editFacilityName.setText(obj.name)
        binding.editFacilityFloor.setText(obj.floor.toString())
        binding.editFacilityKr.setText(obj.kr)
        binding.editFacilityEn.setText(obj.en)
        binding.editFacilityTag.setText(obj.searchTag)
        var i = 0
        for (f in Place.Companion.FacilityType.getArray()) {
            if (obj.type == f) break
            i++
        }
        binding.spinnerFacilityCategory.setSelection(i)
    }

    private fun initArr() {
        facArr.clear()
        pref.getString(SharedPreferencesKeys.STRING_FACILITY_JSON, null)?.let {
            facArr.addAll(Gson().fromJson(it, Array<Place.Companion.Facility>::class.java))
        }
    }

    private fun saveArr() {
        val edit = pref.edit()
        edit.putString(
            SharedPreferencesKeys.STRING_FACILITY_JSON,
            Gson().toJson(facArr.toTypedArray())
        )
        edit.apply()
    }
}