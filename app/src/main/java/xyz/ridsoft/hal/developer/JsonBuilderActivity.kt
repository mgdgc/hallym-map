package xyz.ridsoft.hal.developer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
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
    private var placeArr = ArrayList<Place>()

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJsonBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(SharedPreferencesKeys.DEVELOPER_PREF, 0)

        binding.editJsonBuilderId.requestFocus()

        binding.buttonJsonBuilderClear.setOnClickListener {
            binding.editJsonBuilderId.text.clear()
            binding.editJsonBuilderId.setText((System.currentTimeMillis() % 100000).toString())
            binding.editJsonBuilderName.text.clear()
            binding.editJsonBuilderBuildingNo.text.clear()
            binding.editJsonBuilderLegacyName.text.clear()
            binding.editJsonBuilderNameKr.text.clear()
            binding.editJsonBuilderNameEn.text.clear()
            binding.editJsonBuilderLat.text.clear()
            binding.editJsonBuilderLon.text.clear()
            binding.editJsonBuilderTags.text.clear()
        }

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
                placeArr.add(place!!)
                saveArr()
            }
        }

        binding.buttonJsonBuilderFromJson.setOnClickListener {
            val edittext = EditText(this)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Recover from JSON")
                .setView(edittext)
                .setPositiveButton("복구") { dialog, _ ->
                    val arr = Gson().fromJson(edittext.text.toString(), Array<Place>::class.java)
                    this.placeArr.addAll(arr)
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.buttonJsonBuilderSeeArr.setOnClickListener {
            val arr = Array(placeArr.size) { i ->
                var string = ""
                string += "[" + placeArr[i].id + "]  " + placeArr[i].name + "  ( #" + placeArr[i].buildingNo + " )"

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
                itemMenu.setTitle("[" + placeArr[i].id + "] " + placeArr[i].name)
                    .setMessage(Gson().toJson(placeArr[i]))
                    .setNeutralButton("제거") { d, _ ->
                        placeArr.removeAt(i)
                        d.dismiss()
                    }
                    .setPositiveButton("수정") { d, _ ->
                        setContent(placeArr[i])
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
                binding.txtJsonBuilder.text = Gson().toJson(placeArr.toTypedArray())
                d.dismiss()
            }
            builder.setCancelable(true)
            builder.show()
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

            if (binding.editJsonBuilderFacilities.text.isNotEmpty()) {
                val facilities = binding.editJsonBuilderFacilities.text.toString().split(",")
                place!!.facilityId = Array<Int>(facilities.size) {
                    facilities[it].toInt()
                }
            }

            place!!.buildingNo = buildingNo
            place!!.legacyName = legacy
            place!!.kr = kr
            place!!.en = en
            place!!.searchTag = tag

        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(
                binding.layoutJsonBuilder,
                e.localizedMessage,
                Snackbar.LENGTH_LONG
            )
                .setAction("닫기") { }
                .show()
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

        obj.facilityId?.let {
            val builder = StringBuilder()
            for (i in it.indices) {
                builder.append(it[i])
                if (i < it.size - 1) builder.append(", ")
            }
            binding.editJsonBuilderFacilities.setText(builder.toString())
        }

    }

    private fun initArr() {
        placeArr.clear()
        pref.getString(SharedPreferencesKeys.STRING_PLACE_JSON, null)?.let {
            val arr = Gson().fromJson(it, Array<Place>::class.java)
            placeArr.addAll(arr)
        }
    }

    private fun saveArr() {
        val edit = pref.edit()
        edit.putString(
            SharedPreferencesKeys.STRING_PLACE_JSON,
            Gson().toJson(placeArr.toTypedArray())
        )
        edit.apply()
    }
}