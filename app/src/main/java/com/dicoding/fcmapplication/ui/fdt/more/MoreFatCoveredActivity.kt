package com.dicoding.fcmapplication.ui.fdt.more

import android.content.Intent
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityMoreFatCoveredBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.adapter.FatHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFatCoveredActivity : BaseActivityBinding<ActivityMoreFatCoveredBinding>() {

    private lateinit var data: List<FdtDetail.FatList>

    private val fatGridAdapter: FatHorizontalAdapter by lazy { FatHorizontalAdapter() }

    override val bindingInflater: (LayoutInflater) -> ActivityMoreFatCoveredBinding
        get() =  { ActivityMoreFatCoveredBinding.inflate(layoutInflater)}

    override fun setupView() {
        data = intent.getParcelableArrayListExtra<FdtDetail.FatList>(EXTRA_FAT_COVERED) as List<FdtDetail.FatList>

        binding.headerMoreFatCovered.tvTitleHeader.text = getString(R.string.fat_covered)
        binding.headerMoreFatCovered.btnBack.setOnClickListener { onBackPressed() }

        fatGridAdapter.submitList(data)

        setFatActions()

    }

    private fun setFatActions() {
        with(binding.rvMoreFatCovered) {
            adapter = fatGridAdapter
            setHasFixedSize(true)

            fatGridAdapter.setOnClickData {
                val intent = Intent(this@MoreFatCoveredActivity, FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                startActivity(intent)
            }
        }
    }

    companion object{
        const val EXTRA_FAT_COVERED = "fat covered"
    }
}