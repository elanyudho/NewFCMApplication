package com.dicoding.fcmapplication.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityMainBinding
import com.dicoding.fcmapplication.ui.fat.main.FatFragment
import com.dicoding.fcmapplication.ui.fdt.main.FdtFragment
import com.dicoding.fcmapplication.ui.main.adapter.MainMenuAdapter
import com.dicoding.fcmapplication.ui.other.main.OtherFragment
import com.dicoding.fcmapplication.utils.extensions.setStatusBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding>() {

    private val fdtFragment: FdtFragment by lazy { FdtFragment() }
    private val fatFragment: FatFragment by lazy { FatFragment() }
    private val otherFragment: OtherFragment by lazy { OtherFragment() }

    private val fragments = listOf(fdtFragment, fatFragment, otherFragment)

    private lateinit var mainMenuAdapter: MainMenuAdapter

    private val mBottomNavigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuFdt -> {
                    setStatusBar(R.color.blue_dim, this)
                    binding.containerMain.setCurrentItem(fragments.indexOf(fdtFragment), false)
                    true
                }
                R.id.menuFat -> {
                    setStatusBar(R.color.blue_dim, this)
                    binding.containerMain.setCurrentItem(fragments.indexOf(fatFragment), false)
                    true
                }
                R.id.menuOther -> {
                    setStatusBar(R.color.blue_dim, this)
                    binding.containerMain.setCurrentItem(fragments.indexOf(otherFragment), false)
                    true
                }

                else -> false
            }
        }
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { ActivityMainBinding.inflate(it) }

    override fun setupView() {
        with(binding) {
            mainMenuAdapter = MainMenuAdapter(fragments, this@MainActivity)
            containerMain.adapter = mainMenuAdapter
            containerMain.isUserInputEnabled = false
            containerMain.offscreenPageLimit = fragments.size

            btmNavMain.setOnNavigationItemSelectedListener(mBottomNavigationListener)

            fdtFragment.setOnRefreshData {
                fdtFragment.getFdtListFromOutside()
            }

            fatFragment.setOnRefreshData {
                fatFragment.getFatListFromOutside()
                Log.d("RefreshData", "DO REFRERSH MAIN")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

}