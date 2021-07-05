package xyz.ridsoft.hal

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import xyz.ridsoft.hal.api.ApplicationPermissionManager
import xyz.ridsoft.hal.databinding.ActivityMainBinding
import xyz.ridsoft.hal.recommend.RecommendFragment
import xyz.ridsoft.hal.map.MapFragment
import xyz.ridsoft.hal.more.MoreFragment
import xyz.ridsoft.hal.my.MyPlaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mapFragment: MapFragment
    private lateinit var myPlaceFragment: MyPlaceFragment
    private lateinit var recommendFragment: RecommendFragment
    private lateinit var moreFragment: MoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initFragments()
        initView()
        requestPermission()
    }

    private fun initView() {
        binding.mainBottomNav.background = null
        binding.mainBottomNav.isSelected = false
        binding.mainBottomNav.itemTextAppearanceActive

        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navCurrent -> {
                    fragmentTransaction(mapFragment)
                }

                R.id.navMy -> {
                    fragmentTransaction(myPlaceFragment)
                }

                R.id.navFacilities -> {
                    fragmentTransaction(recommendFragment)
                }

                R.id.navMore -> {
                    fragmentTransaction(moreFragment)
                }
            }
            true
        }
    }

    private fun initFragments() {
        mapFragment = MapFragment()

        moreFragment = MoreFragment()
        myPlaceFragment = MyPlaceFragment()
        recommendFragment = RecommendFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.mainContentLayout, mapFragment)
        transaction.commit()
    }

    private fun fragmentTransaction(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContentLayout, fragment)
        transaction.commit()
    }

    public fun hideFloatingActionButton(hide: Boolean) {
        if (hide) {
            binding.mainFAB.hide()
        } else {
            binding.mainFAB.show()
        }
    }

    public fun hideBottomNavigationView(hide: Boolean) {
        if (hide) {
            binding.mainBAB.performHide()
        } else {
            binding.mainBAB.performShow()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mainBottomNav.selectedItemId = 0
    }

    private fun requestPermission() {
        val permissionManager = ApplicationPermissionManager(this)
        if (permissionManager.checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionManager.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}