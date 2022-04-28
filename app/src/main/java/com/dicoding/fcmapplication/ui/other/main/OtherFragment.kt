package com.dicoding.fcmapplication.ui.other.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentOtherBinding
import com.dicoding.fcmapplication.ui.other.adddata.AddDataActivity
import com.dicoding.fcmapplication.ui.other.companyprofile.CompanyProfileActivity
import com.dicoding.fcmapplication.ui.other.dialog.LogoutDialogFragment
import com.dicoding.fcmapplication.ui.other.regionadmin.RegionAdminActivity
import com.dicoding.fcmapplication.ui.other.repairlist.RepairListActivity
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtherFragment : BaseFragmentBinding<FragmentOtherBinding>() {

    @Inject
    lateinit var session: Session

    private var refreshDataNotify: (() -> Unit)? = null

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOtherBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentOtherBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun setupView() {
        callOnceWhenCreated {

            when {
                session.user?.isCenterAdmin == true -> {
                    centerAdminState()
                }
                session.user?.isAdmin == true -> {
                    adminState()
                }
                else -> {
                    publicState()
                }
            }

            setResultLauncher()

            with(binding) {
                headerOther.btnBack.invisible()
                headerOther.tvTitleHeader.text = getString(R.string.others)

                tvUserName.text = session.user?.username

                rowCompanyProfile.setOnClickListener {
                    startActivity(Intent(requireContext(), CompanyProfileActivity::class.java))
                }

                rowRepairList.setOnClickListener {
                    startActivity(Intent(requireContext(), RepairListActivity::class.java))
                }

                rowAddFdtFat.setOnClickListener {
                    resultLauncher?.launch(Intent(requireContext(), AddDataActivity::class.java))
                }

                rowRegionAdmin.setOnClickListener {
                    startActivity(Intent(requireContext(), RegionAdminActivity::class.java))
                }

                rowLogout.setOnClickListener {
                    val dialogFragment = LogoutDialogFragment()
                    dialogFragment.show(childFragmentManager, "dialog")
                }
            }

        }
    }

    fun setOnRefreshData(action: () -> Unit) {
        this.refreshDataNotify = action
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshDataNotify?.invoke()
            }
        }
    }

    private fun centerAdminState() {
        binding.rowRegionAdmin.visible()
    }

    private fun adminState() {
        binding.rowAddFdtFat.visible()
    }

    private fun publicState() {
        binding.rowAddFdtFat.gone()
        binding.rowRegionAdmin.gone()
    }

}