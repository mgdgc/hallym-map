package xyz.ridsoft.hal.developer

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
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

    private var facArr = ArrayList<String>()

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
            getString(R.string.facility_stationery_store)
        )
        binding.spinnerFacilityCategory.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)

        binding.editFacilityId.requestFocus()

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
                facility?.let { facArr.add(it.toJson()) }
            }
        }

        binding.buttonFacilityShow.setOnClickListener {
            val arr = Array(facArr.size) { i ->
                val item = Gson().fromJson(facArr[i], Place.Companion.Facility::class.java)
                var string = ""
                string += "[" + item.id + "]  " + item.name + "  ( " + item.type + ", " + item.floor + "f )"

                string
            }

            val dialog = AlertDialog.Builder(this)
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arr
            )
            dialog.setAdapter(adapter) { dialog, which ->
                dialog.dismiss()
                val item = Gson().fromJson(facArr[which], Place.Companion.Facility::class.java)
                val itemMenu = AlertDialog.Builder(this)
                itemMenu.setTitle("[" + item.id + "] " + item.name)
                    .setMessage(facArr[which])
                    .setNeutralButton("제거") { d, _ ->
                        facArr.removeAt(which)
                        d.dismiss()
                    }
                    .setPositiveButton("수정") { d, _ ->
                        setContent(item)
                        d.dismiss()
                    }
                    .setNegativeButton("닫기") { d, _ ->
                        d.dismiss()
                    }
                    .show()
            }
            dialog.setPositiveButton("저장") { dialog, _ ->
                saveArr()
                dialog.dismiss()
            }
            dialog.setNeutralButton("닫기") { dialog, _ ->
                dialog?.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
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
            facility!!.kr = if (kr.isNotEmpty()) name else null
            facility!!.en = if (en.isNotEmpty()) name else null
            facility!!.searchTag = if (tag.isNotEmpty()) name else null

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
            val arr = Gson().fromJson(it, Array<String>::class.java)
            facArr.addAll(arr)
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