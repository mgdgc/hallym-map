package xyz.ridsoft.hal

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    private var onFavClickListeners = ArrayList<((View) -> Unit)>()

    private lateinit var searchIcon: Icon
    private lateinit var addIcon: Icon
    private lateinit var likeIcon: Icon

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

        searchIcon = Icon.createWithResource(this, R.drawable.baseline_search_24)
        addIcon = Icon.createWithResource(this, R.drawable.baseline_add_24)
        likeIcon = Icon.createWithResource(this, R.drawable.baseline_thumb_up_24)

        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navCurrent -> {
                    fragmentTransaction(mapFragment)
                    binding.mainFAB.setImageIcon(searchIcon)
                }

                R.id.navMy -> {
                    fragmentTransaction(myPlaceFragment)
                    binding.mainFAB.setImageIcon(addIcon)
                }

                R.id.navFacilities -> {
                    fragmentTransaction(recommendFragment)
                    binding.mainFAB.setImageIcon(searchIcon)
                }

                R.id.navMore -> {
                    fragmentTransaction(moreFragment)
                    binding.mainFAB.setImageIcon(likeIcon)
                }
            }
            true
        }

        binding.mainFAB.setOnClickListener {
            onFavClickListeners[binding.mainBottomNav.selectedItemId](it)
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

    public fun registerFavClickListener(listener: ((View) -> Unit)) {
        onFavClickListeners.add(listener)
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

}