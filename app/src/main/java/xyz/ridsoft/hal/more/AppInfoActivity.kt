package xyz.ridsoft.hal.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import xyz.ridsoft.hal.databinding.ActivityAppInfoBinding
import xyz.ridsoft.hal.developer.FacilityJsonBuilderActivity
import xyz.ridsoft.hal.developer.JsonBuilderActivity

class AppInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppInfoBinding

    private var clicked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtAppName.setOnClickListener {
            if (clicked >= 7) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Developer Tools")
                    .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Place Json Builder") { dialog, _ ->
                        this.startActivity(
                            Intent(this@AppInfoActivity, JsonBuilderActivity::class.java)
                        )
                        dialog.dismiss()
                    }
                    .setNegativeButton("Facility Json Builder") { dialog, _ ->
                        this.startActivity(
                            Intent(
                                this@AppInfoActivity,
                                FacilityJsonBuilderActivity::class.java
                            )
                        )
                        dialog.dismiss()
                    }
                    .show()
                clicked = 0
            } else {
                clicked++
            }
        }
    }
}