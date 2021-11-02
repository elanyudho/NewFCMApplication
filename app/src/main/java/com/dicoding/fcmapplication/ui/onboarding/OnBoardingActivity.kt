package com.dicoding.fcmapplication.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityOnBoardingBinding
import com.dicoding.fcmapplication.ui.login.LoginActivity
import com.dicoding.fcmapplication.ui.main.MainActivity
import com.dicoding.fcmapplication.ui.onboarding.adapter.OnBoardingPagerAdapter
import com.dicoding.fcmapplication.ui.onboarding.model.BoardingItem
import com.dicoding.fcmapplication.utils.extensions.dp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivityBinding<ActivityOnBoardingBinding>() {

    @Inject
    lateinit var session: Session

    private val placeHolderAdapter: OnBoardingPagerAdapter by lazy { OnBoardingPagerAdapter() }

    private lateinit var boardingItems : List<BoardingItem>

    private var onPageChangeCallback : ViewPager2.OnPageChangeCallback? = null

    override val bindingInflater: (LayoutInflater) -> ActivityOnBoardingBinding
        get() = { ActivityOnBoardingBinding.inflate(it) }

    override fun setupView() {
        if(!session.isFirstTime){
            startActivity(Intent(this@OnBoardingActivity, MainActivity::class.java))
            finish()
        }

        setupBoardingItem()

        setupPager()

        with(binding.tabBoarding) {
            setIndicatorForViewPager(binding.boardingViewPager)
            setTabsMargin((-2).dp, 0, (-2).dp, 0)
        }
    }

    private fun setupPager() {
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                with(binding) {
                    if (position == boardingItems.lastIndex) {
                        btnBoardingNext.text = getString(R.string.get_started)
                        btnBoardingNext.setOnClickListener {
                            session.isFirstTime = false
                            startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        btnBoardingNext.text = getString(R.string.next)
                        btnBoardingNext.setOnClickListener {
                            ++boardingViewPager.currentItem
                        }
                    }
                }
                super.onPageSelected(position)
            }
        }

        with(binding.boardingViewPager) {
            adapter = placeHolderAdapter
            offscreenPageLimit = boardingItems.size
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            onPageChangeCallback?.let {
                registerOnPageChangeCallback(it)
            }
        }
    }

    private fun setupBoardingItem() {

        val boarding1 = BoardingItem(
            R.drawable.ic_logo,
            getString(R.string.welcome)
        )

        val boarding2 = BoardingItem(
            R.drawable.ic_undraw_internet_on_the_go_re_vben_2,
            getString(R.string.boarding2)
        )

        boardingItems = listOf(boarding1, boarding2)

        placeHolderAdapter.submitList(boardingItems)
    }

    override fun onDestroy() {
        onPageChangeCallback?.let {
            binding.boardingViewPager.unregisterOnPageChangeCallback(it)
        }
        onPageChangeCallback = null

        super.onDestroy()
    }

}