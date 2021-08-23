package xyz.ridsoft.hal

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import xyz.ridsoft.hal.api.ApplicationPermissionManager
import xyz.ridsoft.hal.data.DataManager
import xyz.ridsoft.hal.databinding.ActivityMainBinding
import xyz.ridsoft.hal.map.MapFragment
import xyz.ridsoft.hal.model.MapPoint
import xyz.ridsoft.hal.more.MoreFragment
import xyz.ridsoft.hal.favorite.FavoriteFragment
import xyz.ridsoft.hal.recommend.FacilityFragment
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapFragment: MapFragment
    private lateinit var favoriteFragment: FavoriteFragment
    private lateinit var facilityFragment: FacilityFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var bottomSheetFragment: MainBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        DataManager(this)

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

        binding.mainBottomNav.setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.navCurrent -> {
                    replaceFragment(mapFragment)
                    true
                }

                R.id.navMy -> {
                    replaceFragment(favoriteFragment)
                    true
                }

                R.id.navFacilities -> {
                    replaceFragment(facilityFragment)
                    true
                }

                R.id.navMore -> {
                    replaceFragment(moreFragment)
                    true
                }

                else -> false
            }
        }

    }

    private fun initFragments() {
        mapFragment = MapFragment()
        moreFragment = MoreFragment()
        favoriteFragment = FavoriteFragment()
        facilityFragment = FacilityFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.mainContentLayout, mapFragment)
        transaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContentLayout, fragment)
        transaction.commit()
    }

    fun setBottomNavigationViewVisibility(visible: Boolean) {
        if (visible) {
            binding.mainBAB.performShow()
        } else {
            binding.mainBAB.performHide()
        }
    }

    fun performCircularRevealAnimation(view: View) {
        runOnUiThread {
            val cx = binding.layoutMainCircularReveal.width / 2
            val cy = binding.layoutMainCircularReveal.height / 2

            val finalRadius = hypot(cx.toDouble() * 2, cy.toDouble() * 2).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(
                binding.layoutMainCircularReveal,
                view.x.toInt() + view.height / 2,
                view.y.toInt() + view.height / 2,
                0f,
                finalRadius
            )

            binding.layoutMainCircularReveal.visibility = View.VISIBLE
            anim.start()
        }
    }

    fun performCircularHideAnimation(view: View) {
        runOnUiThread {
            val cx = binding.layoutMainCircularReveal.width / 2
            val cy = binding.layoutMainCircularReveal.height / 2

            val initialRadius = hypot(cx.toDouble() * 2, cy.toDouble() * 2).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(
                binding.layoutMainCircularReveal,
                view.x.toInt() + view.height / 2,
                view.y.toInt() + view.height / 2,
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
    }

    fun showBottomSheet(mapPoint: MapPoint) {
        bottomSheetFragment.setData(mapPoint)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun hideBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun selectPage(position: Int) {
        when (position) {
            0 -> {
                replaceFragment(mapFragment)
                binding.mainBottomNav.selectedItemId = R.id.navCurrent
            }
            1 -> {
                replaceFragment(favoriteFragment)
                binding.mainBottomNav.selectedItemId = R.id.navMy
            }
            2 -> {
                replaceFragment(facilityFragment)
                binding.mainBottomNav.selectedItemId = R.id.navFacilities
            }
            3 -> {
                replaceFragment(moreFragment)
                binding.mainBottomNav.selectedItemId = R.id.navMore
            }
        }
    }

    override fun onResume() {
        super.onResume()
        selectPage(0)
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
            Toast.makeText(this, R.string.granted, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.denied, Toast.LENGTH_SHORT).show()
        }
    }

}