package xyz.ridsoft.hal

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import xyz.ridsoft.hal.api.ApplicationPermissionManager
import xyz.ridsoft.hal.databinding.ActivityMainBinding
import xyz.ridsoft.hal.map.MapFragment
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.more.MoreFragment
import xyz.ridsoft.hal.my.MyPlaceFragment
import xyz.ridsoft.hal.recommend.RecommendFragment
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapFragment: MapFragment
    private lateinit var myPlaceFragment: MyPlaceFragment
    private lateinit var recommendFragment: RecommendFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var bottomSheetFragment: MainBottomSheetFragment

    private lateinit var searchIcon: Icon
    private lateinit var addIcon: Icon
    private lateinit var editIcon: Icon
    private lateinit var likeIcon: Icon

    var activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        runOnUiThread {
            Handler(mainLooper).postDelayed({
                performCircularHideAnimation()
            }, 50)
        }
        if (it.resultCode == RESULT_OK) {
            it.data?.let { intent ->
                if (intent.hasExtra("result")) {
//                    Toast.makeText(this, intent.getStringExtra("result"), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initFragments()
        initView()
        requestPermission()

        behavior = BottomSheetBehavior.from(view.findViewById(R.id.layoutMainBottomSheet))
        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetFragment = MainBottomSheetFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.layoutMainBottomSheet, bottomSheetFragment)
        transaction.commit()

    }

    private fun initView() {
        binding.mainBottomNav.background = null
        binding.mainBottomNav.isSelected = false
        binding.mainBottomNav.itemTextAppearanceActive

        searchIcon = Icon.createWithResource(this, R.drawable.baseline_search_24)
        addIcon = Icon.createWithResource(this, R.drawable.baseline_add_24)
        editIcon = Icon.createWithResource(this, R.drawable.baseline_edit_24)
        likeIcon = Icon.createWithResource(this, R.drawable.baseline_thumb_up_24)

        binding.mainBottomNav.setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.navCurrent -> {
                    fragmentTransaction(mapFragment)
                    binding.mainFAB.setImageIcon(searchIcon)
                    true
                }

                R.id.navMy -> {
                    fragmentTransaction(myPlaceFragment)
                    binding.mainFAB.setImageIcon(editIcon)
                    true
                }

                R.id.navFacilities -> {
                    fragmentTransaction(recommendFragment)
                    binding.mainFAB.setImageIcon(searchIcon)
                    true
                }

                R.id.navMore -> {
                    fragmentTransaction(moreFragment)
                    binding.mainFAB.setImageIcon(likeIcon)
                    true
                }

                else -> false
            }
        }

        binding.mainFAB.setOnClickListener {
            when (binding.mainBottomNav.selectedItemId) {
                R.id.navCurrent -> mapFragment.onClickListener(it)
                R.id.navMy -> myPlaceFragment.onClickListener(it)
                R.id.navFacilities -> recommendFragment.onClickListener(it)
                R.id.navMore -> moreFragment.onClickListener(it)
                else -> return@setOnClickListener
            }
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

    fun hideFloatingActionButton(hide: Boolean) {
        if (hide) {
            binding.mainFAB.hide()
        } else {
            binding.mainFAB.show()
        }
    }

    fun hideBottomNavigationView(hide: Boolean) {
        if (hide) {
            binding.mainBAB.performHide()
        } else {
            binding.mainBAB.performShow()
        }
    }

    fun performCircularRevealAnimation() {
        val cx = binding.layoutMainCircularReveal.width / 2
        val cy = binding.layoutMainCircularReveal.height / 2

        val finalRadius = hypot(cx.toDouble() * 2, cy.toDouble() * 2).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.layoutMainCircularReveal,
            binding.mainFAB.x.toInt() + binding.mainFAB.width / 2,
            binding.mainFAB.y.toInt() + binding.mainFAB.height / 2,
            0f,
            finalRadius
        )

        binding.layoutMainCircularReveal.visibility = View.VISIBLE
        anim.start()
    }

    fun performCircularHideAnimation() {
        val cx = binding.layoutMainCircularReveal.width / 2
        val cy = binding.layoutMainCircularReveal.height / 2

        val initialRadius = hypot(cx.toDouble() * 2, cy.toDouble() * 2).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.layoutMainCircularReveal,
            binding.mainFAB.x.toInt() + binding.mainFAB.width / 2,
            binding.mainFAB.y.toInt() + binding.mainFAB.height / 2,
            initialRadius,
            0f
        )

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.layoutMainCircularReveal.visibility = View.INVISIBLE
            }
        })

        anim.start()
    }

    fun showBottomSheet(mapPoint: MapPoint) {
        bottomSheetFragment.setData(mapPoint)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun hideBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
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