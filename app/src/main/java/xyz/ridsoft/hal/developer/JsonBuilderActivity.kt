package xyz.ridsoft.hal.developer

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import xyz.ridsoft.hal.databinding.ActivityJsonBuilderBinding
import xyz.ridsoft.hal.model.Place
import xyz.ridsoft.hal.value.SharedPreferencesKeys
import java.lang.StringBuilder

class JsonBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJsonBuilderBinding

    private var place: Place? = null
    private var placeArr = ArrayList<String>()

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(SharedPreferencesKeys.DEVELOPER_PREF, 0)

        binding.editJsonBuilderId.requestFocus()

        binding.buttonJsonBuilderGenerate.setOnClickListener {
            generate()
            show()
        }

        binding.buttonJsonBuilderFacility.setOnClickListener {
            startActivity(Intent(this@JsonBuilderActivity, FacilityJsonBuilderActivity::class.java))
            this.finish()
        }

        binding.buttonJsonBuilderAddArr.setOnClickListener {
            if (place == null) {
                Snackbar.make(
                    binding.layoutJsonBuilder,
                    "Generate object first",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Confirm") { }
                    .show()
            } else {
                placeArr.add(Gson().toJson(place))
            }
        }

        binding.buttonJsonBuilderSeeArr.setOnClickListener {
            val arr = Array(placeArr.size) { i ->
                val item = Gson().fromJson(placeArr[i], Place::class.java)
                var string = ""
                string += "[" + item.id + "]  " + item.name + "  ( #" + item.buildingNo + " )"

                string
            }

            val dialog = AlertDialog.Builder(this)
            val adapter = ArrayAdapter<String>(
                this,
                R.layout.simple_list_item_1,
                arr
            )
            dialog.setAdapter(adapter) { dialog, which ->
                dialog.dismiss()
                val item = Gson().fromJson(placeArr[which], Place::class.java)
                val itemMenu = AlertDialog.Builder(this)
                itemMenu.setTitle("[" + item.id + "] " + item.name)
                    .setMessage(placeArr[which])
                    .setNeutralButton("제거") { d, _ ->
                        placeArr.removeAt(which)
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
            val id = binding.editJsonBuilderId.text.toString().toInt()
            val name = binding.editJsonBuilderName.text.toString()
            val buildingNo = binding.editJsonBuilderBuildingNo.text.toString().toInt()
            val legacy = binding.editJsonBuilderLat.text.toString()
            val kr = binding.editJsonBuilderNameKr.text.toString()
            val en = binding.editJsonBuilderNameEn.text.toString()
            val lat = binding.editJsonBuilderLat.text.toString().toDouble()
            val long = binding.editJsonBuilderLon.text.toString().toDouble()
            val tag = binding.editJsonBuilderTags.text.toString()

            place = Place(id, name, lat, long)

            val facilities = binding.editJsonBuilderFacilities.text.toString().split(",")
            for (i in facilities.indices) {
                place!!.facilityId[i] = facilities[i].toInt()
            }

            place!!.buildingNo = buildingNo
            place!!.legacyName = legacy
            place!!.kr = kr
            place!!.en = en
            place!!.searchTag = tag

        } catch (e: Exception) {
            e.printStackTrace()
            binding.txtJsonBuilder.text = e.localizedMessage
        }
    }

    private fun show() {
        if (place != null) {
            val builder = StringBuilder()
            builder.append("--- Object info ---\n\n")
                .append("id: " + place!!.id + "\n")
                .append("name: " + place!!.name + "\n")
                .append("buildingNo: " + place!!.buildingNo + "\n")
                .append("(legacyName): " + place!!.legacyName + "\n")
                .append("(kr): " + place!!.kr + "\n")
                .append("(en): " + place!!.en + "\n")
                .append("latitude: " + place!!.latitude + "\n")
                .append("longitude: " + place!!.longitude + "\n")
                .append("(searchTag): " + place!!.searchTag + "\n\n\n")
                .append("--- Json ---\n\n")
                .append(Gson().toJson(place))

            binding.txtJsonBuilder.text = builder.toString()
        }
    }

    private fun setContent(obj: Place) {
        binding.editJsonBuilderId.setText(obj.id.toString())
        binding.editJsonBuilderName.setText(obj.name)
        binding.editJsonBuilderBuildingNo.setText(obj.buildingNo.toString())
        binding.editJsonBuilderLegacyName.setText(obj.legacyName)
        binding.editJsonBuilderNameKr.setText(obj.kr)
        binding.editJsonBuilderNameEn.setText(obj.en)
        binding.editJsonBuilderLat.setText(obj.latitude.toString())
        binding.editJsonBuilderLon.setText(obj.longitude.toString())
        binding.editJsonBuilderTags.setText(obj.searchTag)
        val builder = StringBuilder()
        for (i in obj.facilityId.indices) {
            builder.append(obj.facilityId[i])
            if (i < obj.facilityId.size - 1) builder.append(", ")
        }
        binding.editJsonBuilderFacilities.setText(builder.toString())
    }

    private fun initArr() {
        placeArr.clear()
        pref.getString(SharedPreferencesKeys.STRING_FACILITY_JSON, null)?.let {
            val arr = Gson().fromJson(it, Array<String>::class.java)
            placeArr.addAll(arr)
        }
    }

    private fun saveArr() {
        val edit = pref.edit()
        edit.putString(
            SharedPreferencesKeys.STRING_FACILITY_JSON,
            Gson().toJson(placeArr.toTypedArray())
        )
        edit.apply()
    }
}